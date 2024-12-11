import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/', // API Gateway
    withCredentials: true, // Permet l'envoi de cookies (si nécessaire)
    timeout: 10000, // Temps d'attente avant l'abandon d'une requête
});

// Intercepteur pour ajouter le token JWT à chaque requête
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("accessToken");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Intercepteur pour gérer les erreurs globales (optionnel)
api.interceptors.response.use(
    (response) => response,
    (error) => {
        // Exemple : rediriger si erreur 401 (non autorisé)
        if (error.response?.status === 401) {
            localStorage.removeItem("accessToken");
            window.location.href = "/login"; // Redirection
        }
        return Promise.reject(error);
    }
);

export default api;