import axios from "axios";

const BASE_URL = "http://localhost:5000/api/";
const TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0OWFmOGU0NjRjYzJiOGRlMTZhNTE2ZCIsImlzQWRtaW4iOnRydWUsImlhdCI6MTY4OTUwMTE1MiwiZXhwIjoxNjg5NzYwMzUyfQ.iPgy11n7FrtEKgau3CD4iDXHIOKC2srG_jckWg6kjlE";
//const TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0YTkwMTQ4MGRiNWRmZmNlMjVlYjQ1ZCIsImlzQWRtaW4iOmZhbHNlLCJpYXQiOjE2ODg3OTgxNTMsImV4cCI6MTY4OTA1NzM1M30.BfbBdLUtPHhKo5gsIC9TI-IVPsksKNnFNiazf_3HYKc";         //user3
export const publicRequest = axios.create({
    baseURL : BASE_URL,
});

export const userRequest = axios.create({
    baseURL : BASE_URL,
    headers: {token: `Bearer ${TOKEN}`},
});
