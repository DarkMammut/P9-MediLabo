import React, { useState } from "react";
import API from "../../../api/api"; // Axios instance
import { useNavigate } from "react-router-dom";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await API.post("/login", {
                username,
                password,
            }, {
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                withCredentials: true, // Pour gérer les cookies de session
            });
            console.log(response.data);
            if (response.data.accessToken) {
                localStorage.setItem("accessToken", response.data.accessToken);
            } else {
                console.error("Token manquant dans la réponse");
            }
            navigate("/"); // Rediriger après connexion
        } catch (err) {
            setError("Invalid username or password");
        }
    };

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">Login</button>
            </form>
            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );
};

export default Login;
