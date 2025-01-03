import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: true,
});

export const setupAxiosInterceptors = (setIsAuthenticated, navigate) => {
    API.interceptors.request.use(
        (config) => config,
        (error) => Promise.reject(error)
    );

    API.interceptors.response.use(
        (response) => {
            if (response.status === 200) {
                setIsAuthenticated(true); // L'utilisateur est authentifié
            }
            return response;
        },
        async (error) => {
            // Gestion d'autres erreurs (ex : 500, 404, etc.)
            if (error.response) {
                console.error("Erreur HTTP : ", error.response.status);
                if (error.response.status === 401) {
                    setIsAuthenticated(false);
                    navigate("/login");
                }
            } else if (error.request) {
                console.error("Aucune réponse reçue du serveur", error.request);
                setIsAuthenticated(true);
            } else {
                console.error("Erreur lors de la configuration de la requête", error.message);
                setIsAuthenticated(true);
            }

            return Promise.reject(error);
        }
    );
};

export default API;
