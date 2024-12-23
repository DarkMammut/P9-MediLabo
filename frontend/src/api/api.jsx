import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080", // URL de l'API Gateway
    withCredentials: true, // Inclut automatiquement les cookies
});

export const setupAxiosInterceptors = () => {
    API.interceptors.request.use(
        (config) => config,
        (error) => Promise.reject(error)
    );

    API.interceptors.response.use(
        (response) => response,
        (error) => Promise.reject(error)
    );
};

export default API;