import http from "k6/http";
import { SharedArray } from "k6/data";

const tokens = new SharedArray("tokens", function () {
    return JSON.parse(open("./tokens.json"));
});

export const options = {
    vus: 20,
    duration: "10s"
};

export default function () {

    let token = tokens[Math.floor(Math.random() * tokens.length)];

    const params = {
        headers: {
            Authorization: `Bearer ${token}`
        }
    };

    http.get("http://localhost:8080/api/comments/test", params);
    http.get("http://localhost:8080/api/likes/test", params);
    http.get("http://localhost:8080/api/share/test", params);
}