const axios = require("axios");
const fs = require("fs");
const path = require("path");

const BASE_URL = "http://localhost:30080/auth/register";
const USERS = 50;

const tokens = [];
const failed = [];

async function registerUser(i) {
  const username = `user_${i}_${Math.random().toString(36).slice(2, 7)}`;

  try {
    const res = await axios.post(BASE_URL, {
      username,
      password: "password123",
    });

    if (!res.data?.token) {
      throw new Error("Response missing token field");
    }

    tokens.push({ username, token: res.data.token });
    console.log(`[OK]  ${username} -> status ${res.status}`);
  } catch (err) {
    const status = err.response?.status ?? "ERR";
    const body = err.response?.data
      ? JSON.stringify(err.response.data)
      : err.message;
    console.log(`[FAIL] ${username} -> ${status}: ${body}`);
    failed.push({ username, reason: body });
  }
}

async function main() {
  console.log(`Registering ${USERS} users...`);

  const promises = Array.from({ length: USERS }, (_, i) =>
    registerUser(i + 1)
  );

  await Promise.all(promises);

  console.log(`\nResults: ${tokens.length} succeeded, ${failed.length} failed`);

  if (failed.length > 0) {
    console.log("\nFailed users:");
    failed.forEach((f) => console.log(`  - ${f.username}: ${f.reason}`));
  }

  if (tokens.length === 0) {
    console.error("No tokens generated — aborting. Check your auth service.");
    process.exit(1);
  }

  const filePath = path.join(__dirname, "tokens.json");
  fs.writeFileSync(filePath, JSON.stringify(tokens, null, 2));
  console.log(`\nSaved ${tokens.length} tokens to ${filePath}`);

  if (tokens.length < USERS) {
    console.warn(
      `Warning: only ${tokens.length}/${USERS} tokens saved. Some VUs in k6 will reuse tokens.`
    );
  }
}

main();