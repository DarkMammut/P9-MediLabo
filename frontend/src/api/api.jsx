import axios from "axios";

const getTokenFromCookie = () => {
    console.log(document.cookie)
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + encodeURIComponent("token") + "=([^;]*)"
    ));
    return matches ? matches[1] : null;
};

const API = axios.create({
    baseURL: "http://localhost:8080", // API Gateway URL
    withCredentials: true, // Permet d'envoyer les cookies si nécessaire
});

API.interceptors.request.use((config) => {
    const token = getTokenFromCookie();
    if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
}, (error) => Promise.reject(error));

export default API;
