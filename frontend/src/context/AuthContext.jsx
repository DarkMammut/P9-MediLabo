import React, { createContext, useState, useEffect, useContext } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(null);

    useEffect(() => {
        const tokenFromCookie = document.cookie
            .split("; ")
            .find(row => row.startsWith("token="))
            ?.split("=")[1];
        setToken(tokenFromCookie); // Initialiser avec le token des cookies
    }, []);

    return (
        <AuthContext.Provider value={{ token, setToken }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
