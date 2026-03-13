import http from "k6/http";
import { check, sleep } from "k6";

const tokens = JSON.parse(open("./tokens.json"));

if (tokens.length === 0) {
  throw new Error("tokens.json is empty — run generate_tokens.js first");
}

export const options = {
  stages: [
    { duration: "20s", target: 200 },   // ramp up to 200 VUs
    { duration: "30s", target: 500 },   // ramp up to 500 VUs
    { duration: "40s", target: 833 },   // ramp up to ~50k req/min (833 req/s)
    { duration: "60s", target: 833 },   // sustain 50k req/min for 1 minute
    { duration: "20s", target: 0 },     // ramp down
  ],

  thresholds: {
    http_req_duration: ["p(95)<2000"],          // 95% of requests under 2s
    http_req_failed: ["rate<0.05"],             // less than 5% hard failures (not 429s)
    "checks{check:no 401s}": ["rate==1.00"],    // zero 401s allowed
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:30080";

const ENDPOINTS = [
  "/api/likes/test",
  "/api/comments/test",
  "/api/share/test",
];

export default function () {
  const user = tokens[(__VU - 1) % tokens.length];

  if (!user || !user.token) {
    throw new Error(`No token for VU ${__VU}`);
  }

  const endpoint = ENDPOINTS[Math.floor(Math.random() * ENDPOINTS.length)];

  const res = http.get(`${BASE_URL}${endpoint}`, {
    headers: { Authorization: `Bearer ${user.token}` },
  });

  if (__VU === 1 && __ITER < 3) {
    console.log(`━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`);
    console.log(`ENDPOINT : ${endpoint}`);
    console.log(`STATUS   : ${res.status}`);
    console.log(`TOKEN    : ${user.token.substring(0, 30)}...`);
    console.log(`BODY     : ${res.body.substring(0, 300)}`);
    console.log(`━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━`);
  }

  check(res, {
    "status is 200 or 429": (r) => r.status === 200 || r.status === 429,
    "no 401s": (r) => r.status !== 401,
    "no 5xx errors": (r) => r.status < 500,
  });

  sleep(0.1);
}