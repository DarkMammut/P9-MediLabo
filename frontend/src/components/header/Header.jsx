import React from "react";
import { useNavigate } from "react-router-dom"; // Pour rediriger après la déconnexion
import "./Header.css";

function Header() {
    const navigate = useNavigate();

    const handleLogout = () => {
        // Supprimer le token JWT de localStorage
        localStorage.removeItem("accessToken");

        // Rediriger vers la page de login après la déconnexion
        navigate("/login");
    };

    // Vérifier si l'utilisateur est connecté
    const isLoggedIn = localStorage.getItem("accessToken");

    return (
        <div className="header">
            <div className="auth-links">
                {/* Si l'utilisateur est connecté, afficher le lien de déconnexion */}
                {isLoggedIn ? (
                    <a href="/logout" onClick={handleLogout}>Déconnexion</a>
                ) : (
                    <>
                        {/* Sinon, afficher le lien de connexion */}
                        <a href="/login">Connexion</a>
                    </>
                )}
            </div>
        </div>
    );
}

export default Header;
