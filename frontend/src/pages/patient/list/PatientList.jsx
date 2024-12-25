import React, { useState, useEffect } from "react";
import API, { setupAxiosInterceptors } from "../../../api/api"; // Axios instance
import { Link } from "react-router-dom";
import Header from "../../../components/header/Header";
import Sidebar from "../../../components/sidebar/Sidebar";

const PatientsList = () => {
  const [patients, setPatients] = useState([]); // Data from API
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // Configure Axios interceptors
    setupAxiosInterceptors();

    const fetchPatients = async () => {
      try {
        const response = await API.get("/api/patients");
        console.log(response.data);  // Log pour vérifier la structure de la réponse
        if (Array.isArray(response.data)) {
          setPatients(response.data);
          setIsAuthenticated(true);
        } else {
          console.error("Response data is not an array:", response.data);
          setPatients([]);
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error("Error fetching patients:", error);
      }
    };

    fetchPatients();
  }, []);

  return (
    <div>
      <Header isAuthenticated={isAuthenticated} />
      <Sidebar />

    <main>
      <h1>Patients</h1>
      <Link to="/patients/add">
        <button>Ajouter Patient</button>
      </Link>

      <table border="1" style={{ width: "100%", marginTop: "20px" }}>
        <thead>
          <tr>
            <th>Prénom</th>
            <th>Nom</th>
          </tr>
        </thead>
        <tbody>
          {patients.map((patient) => (
            <tr key={patient.id}>
              <td>
                <Link to={`/patients/${patient.id}`}>
                  {patient.firstName} {patient.lastName}
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </main>
    </div>
  );
};

export default PatientsList;
