import http from "k6/http";

const BASE_URL = "http://localhost:8081/auth";

export default function () {

    for (let i = 1; i <= 50; i++) {

        let payload = JSON.stringify({
            username: `user${i}`,
            password: "password"
        });

        http.post(`${BASE_URL}/register`, payload, {
            headers: { "Content-Type": "application/json" }
        });
    }
}