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

// Intercepteur de réponses
API.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        // Gestion d'autres erreurs (ex : 500, 404, etc.)
        if (error.response) {
            console.error("Erreur HTTP : ", error.response.status);
        } else if (error.request) {
            console.error("Aucune réponse reçue du serveur", error.request);
        } else {
            console.error("Erreur lors de la configuration de la requête", error.message);
        }

        return Promise.reject(error);
    }
);
};

export default API;