import React, { createContext, useState, useEffect, useContext } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(null);

    // Récupérer le token des cookies
    const getTokenFromCookie = () => {
        const cookies = document.cookie.split("; ");
        for (let cookie of cookies) {
            const [key, value] = cookie.split("=");
            if (key === "token") return value;
        }
        return null;
    };

    // Vérifier si le token est expiré
    const isTokenExpired = (token) => {
        try {
            const payload = JSON.parse(atob(token.split(".")[1]));
            return payload.exp * 1000 < Date.now();
        } catch (e) {
            return true;
        }
    };

    useEffect(() => {
        console.log("Token dans AuthContext :", token);
    }, [token]);    

    useEffect(() => {
        const tokenFromCookie = getTokenFromCookie();
        if (tokenFromCookie && !isTokenExpired(tokenFromCookie)) {
            setToken(tokenFromCookie);
        } else {
            setToken(null); // Déconnecter si expiré
        }
    }, []);

    const logout = () => {
        document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
        setToken(null);
    };

    return (
        <AuthContext.Provider value={{ token, setToken, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
