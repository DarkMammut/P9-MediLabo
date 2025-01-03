import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import Header from "./components/header/Header";
import Home from "./pages/home/home";
import Login from "./pages/auth/login/Login";
import PatientsList from "./pages/patient/list/PatientList";
import PatientDetail from "./pages/patient/detail/PatientDetail";
import PatientAdd from "./pages/patient/add/PatientAdd";
import { setupAxiosInterceptors } from "./api/api";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        setupAxiosInterceptors(setIsAuthenticated, navigate);
    }, [navigate]);

    return (
        <>
            <Header isAuthenticated={isAuthenticated}/>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/patients" element={ <PatientsList /> } />
                <Route path="/patients/:id" element={ <PatientDetail /> } />
                <Route path="/patients/add" element={ <PatientAdd /> } />
            </Routes>
        </>
    );
}

export default function RootApp() {
    return (
        <Router>
            <App />
        </Router>
    );
}
