import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Logout from "../../pages/auth/logout/Logout";

function Header({ isAuthenticated }) {
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const publicPages = ["/", "/login"];
        // Redirection si non authentifié et la page actuelle n'est pas publique
        if (!isAuthenticated && !publicPages.includes(location.pathname)) {
            navigate("/login");
        }
    }, [isAuthenticated, location.pathname, navigate]);

    // Fonction pour gérer la déconnexion
    const handleLogout = () => {
        console.log("User logged out.");
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <a className="navbar-brand" href="/">
                    MediLabo
                </a>
                <div className="d-flex ms-auto">
                    {isAuthenticated ? (
                        <Logout onLogout={handleLogout} />
                    ) : (
                        <a href="/login" className="btn btn-outline-primary">
                            Connexion
                        </a>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Header;