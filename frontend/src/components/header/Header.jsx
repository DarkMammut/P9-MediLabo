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
    console.log("non authentifié");
    }
  }, [isAuthenticated, location.pathname, navigate]);

  // Fonction pour gérer la déconnexion dans le parent Header
  const handleLogout = () => {
  };

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
