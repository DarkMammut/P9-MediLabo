import React, { useState, useEffect } from "react";
import API from "../../../api/api";
import { Link, useNavigate } from "react-router-dom";

const PatientsList = () => {
  const [patients, setPatients] = useState([]); // Liste des patients
  const [error, setError] = useState(null); // État pour gérer les erreurs
  const navigate = useNavigate();

  useEffect(() => {
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
        setError("Une erreur est survenue lors de la récupération des patients.");
      }
    };

    fetchPatients();
  }, []);

  const handleRowClick = (id) => {
    navigate(`/patients/${id}`);
  };

  return (
    <div className="container my-5 mx-auto">
      <h1 className="text-center mb-4">Liste des Patients</h1>

      {error && <div className="alert alert-danger">{error}</div>}

      <div className="d-flex justify-content-end mb-3">
        <Link to="/patients/add" className="btn btn-primary">
          Ajouter un Patient
        </Link>
      </div>

      {patients.length > 0 ? (
        <table className="table table-striped table-hover">
          <thead className="table-dark">
            <tr>
              <th scope="col">Prénom</th>
              <th scope="col">Nom</th>
            </tr>
          </thead>
          <tbody>
            {patients.map((patient) => (
              <tr
                key={patient.id}
                style={{ cursor: "pointer" }}
                onClick={() => handleRowClick(patient.id)}
              >
                <td>{patient.firstName}</td>
                <td>{patient.lastName}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p className="text-center">Aucun patient trouvé.</p>
      )}
    </div>
  );
};

export default PatientsList;
