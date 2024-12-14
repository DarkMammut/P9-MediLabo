import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext"; // Import du contexte

const ProtectedRoute = ({ children }) => {
    const { token } = useAuth(); // Récupère le token depuis le contexte

    if (token === null) {
        return <div>Loading...</div>; // Affiche un message de chargement si le token n'est pas encore disponible
    }

    if (!token) {
        return <Navigate to="/login" />;
    }

    return children;
};

export default ProtectedRoute;
