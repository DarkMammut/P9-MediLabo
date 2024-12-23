import React, { useState, useEffect } from "react";
import API, { setupAxiosInterceptors } from "../../../api/api"; // Axios instance
import { Link } from "react-router-dom";

const PatientsList = () => {
  const [patients, setPatients] = useState([]); // Data from API

  useEffect(() => {
    // Configure Axios interceptors
    setupAxiosInterceptors();

    const fetchPatients = async () => {
      try {
        const response = await API.get("/api/patients");
        if (Array.isArray(response.data)) {
          setPatients(response.data);
        } else {
          console.error("Response data is not an array:", response.data);
          setPatients([]);
        }
      } catch (error) {
        console.error("Error fetching patients:", error);
      }
    };

    fetchPatients();
  }, []);

  return (
    <div>
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
    </div>
  );
};

export default PatientsList;
