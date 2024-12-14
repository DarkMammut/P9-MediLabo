import axios from "axios";
import { useAuth } from "../context/AuthContext"; // Import du contexte

const API = axios.create({
    baseURL: "http://localhost:8080/", // API Gateway
    withCredentials: true,
    timeout: 10000,
});

export const useApiInterceptor = () => {
    const { token } = useAuth(); // Récupère le token depuis le contexte

    API.interceptors.request.use(
        (config) => {
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        },
        (error) => Promise.reject(error)
    );
};

export default API;
