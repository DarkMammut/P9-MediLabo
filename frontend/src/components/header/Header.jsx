import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import API from "../../api/api";
import Logout from "../../pages/auth/logout/Logout";

function Header() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const verifyAuth = async () => {
      try {
        await API.get("/auth/check");
        setIsAuthenticated(true);
      } catch {
        setIsAuthenticated(false);
      } finally {
        setIsLoading(false);
      }
    };

    if (isLoading) {
      verifyAuth();
    }
  }, [isLoading]);

  useEffect(() => {
    const publicPages = ["/", "/login"];
  
    // Redirection si non authentifié et la page actuelle n'est pas publique
    if (!isAuthenticated && !publicPages.includes(location.pathname)) {
      console.log("non authentifié")
      navigate("/login");
    }
  }, [isAuthenticated, location.pathname, navigate]);

  // Fonction pour gérer la déconnexion dans le parent Header
  const handleLogout = () => {
    setIsAuthenticated(false); // Mettre à jour le statut de l'authentification
  };

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="header">
      <div className="auth-links">
        {isAuthenticated ? (
          // Utiliser le composant Logout ici
          <Logout onLogout={handleLogout} />
        ) : (
          <a href="/login">Connexion</a>
        )}
      </div>
    </div>
  );
}

export default Header;
