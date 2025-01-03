import React, { useState, useEffect, useCallback } from "react";
import API, { setupAxiosInterceptors } from "../../../api/api"; // Axios instance
import { Link } from "react-router-dom";

const PatientsList = () => {
  const [patients, setPatients] = useState([]); // Data from API
  const [filteredPatients, setFilteredPatients] = useState([]); // Filtered data
  const [filters, setFilters] = useState({
    firstName: "",
    lastName: "", // Ajout du filtre pour le nom
  });

  useEffect(() => {
    // Configure Axios interceptors
    setupAxiosInterceptors();

    const fetchPatients = async () => {
      try {
        const response = await API.get("/api/patients", {
          params: { firstName: filters.firstName, lastName: filters.lastName },
        });
        setPatients(response.data);
        setFilteredPatients(response.data);
      } catch (error) {
        console.error("Error fetching patients:", error);
      }
    };

    fetchPatients();
  }, [filters]); // Ajout des filtres à la dépendance

  // Fonction de filtrage des patients, mémorisée avec useCallback
  const filterPatients = useCallback(
    (patients) => {
      return patients.filter((patient) => {
        return (
          patient.firstName.toLowerCase().includes(filters.firstName.toLowerCase()) &&
          patient.lastName.toLowerCase().includes(filters.lastName.toLowerCase())
        );
      });
    },
    [filters] // Dépendance à filters pour recalculer la fonction si les filtres changent
  );

  // Mettre à jour les patients filtrés lorsque les filtres changent
  useEffect(() => {
    setFilteredPatients(filterPatients(patients));
  }, [filters, patients, filterPatients]); // Inclure filterPatients dans la liste des dépendances

  // Handle filter change
  const handleFilterChange = (key, value) => {
    setFilters({ ...filters, [key]: value });
  };

  return (
    <div>
      <h1>Patients</h1>
      <Link to="/patients/add">
        <button>Ajouter Patient</button>
      </Link>

      <table border="1" style={{ width: "100%", marginTop: "20px" }}>
        <thead>
          <tr>
            <th>
              Prénom
              <input
                type="text"
                placeholder="Filtrer par prénom"
                value={filters.firstName}
                onChange={(e) => handleFilterChange("firstName", e.target.value)}
              />
            </th>
            <th>
              Nom
              <input
                type="text"
                placeholder="Filtrer par nom"
                value={filters.lastName}
                onChange={(e) => handleFilterChange("lastName", e.target.value)}
              />
            </th>
          </tr>
        </thead>
        <tbody>
          {filteredPatients.map((patient) => (
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
