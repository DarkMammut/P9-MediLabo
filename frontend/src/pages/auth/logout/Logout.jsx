import React from "react";
import API from "../../../api/api";
import { useNavigate } from "react-router-dom";

const Logout = ({ onLogout }) => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      // Envoyer une requête pour supprimer le token côté serveur
      await API.post("/auth/logout");
      onLogout();
      // Rediriger l'utilisateur après la déconnexion
      navigate("/login");

    } catch (err) {
      console.error("Error during logout:", err);
    }
  };

  return (
    <div>
      <button onClick={handleLogout}>Déconnexion</button>
    </div>
  );
};

export default Logout;
