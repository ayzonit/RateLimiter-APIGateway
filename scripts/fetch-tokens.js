const fs = require("fs");
const axios = require("axios");

const BASE_URL = "http://localhost:8081/auth";

async function fetchTokens() {

    const tokens = [];

    for (let i = 1; i <= 50; i++) {

        try {

            const res = await axios.post(`${BASE_URL}/login`, {
                username: `user${i}`,
                password: "password"
            });

            const token = res.data.token;

            tokens.push(token);

            console.log(`Token fetched for user${i}`);

        } catch (err) {
            console.log(`Login failed for user${i}`);
        }
    }

    fs.writeFileSync("./tests/tokens.json", JSON.stringify(tokens, null, 2));

    console.log("tokens.json created successfully");
}

fetchTokens();