import React from "react";
import { useAuth } from "../../context/AuthContext";

function Header() {
    const { token, logout } = useAuth();

    return (
        <div className="header">
            <div className="auth-links">
                {token ? (
                    <button onClick={logout}>Déconnexion</button>
                ) : (
                    <a href="/login">Connexion</a>
                )}
            </div>
        </div>
    );
}

export default Header;
