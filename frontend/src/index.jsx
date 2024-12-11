import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import ProtectedRoute from "./components/route/ProtectedRoute";
import Header from "./components/header/Header";
import Sidebar from "./components/sidebar/Sidebar";
import Home from "./pages/home/home"
import Login from "./pages/auth/login/Login"
import PatientsList from "./pages/patient/list/PatientList";
import PatientDetail from "./pages/patient/detail/PatientDetail";
import PatientAdd from "./pages/patient/add/PatientAdd";
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
            <Router>
            <Header />
            <Sidebar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/patients" element={
                  <ProtectedRoute>
                    <PatientsList />
                  </ProtectedRoute>
                  } />
                <Route path="/patients/:id" element={
                  <ProtectedRoute>
                    <PatientDetail />
                  </ProtectedRoute>
                  } />
                <Route path="/patients/add" element={
                  <ProtectedRoute>
                    <PatientAdd />
                  </ProtectedRoute>
                  } />
            </Routes>
        </Router>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
