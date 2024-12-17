import React, { useState, useEffect } from "react";
import API from "../../../api/api"; // Axios instance
import { Link } from "react-router-dom";

const PatientsList = () => {
  const [patients, setPatients] = useState([]); // Data from API
  const [filteredPatients, setFilteredPatients] = useState([]); // Filtered data
  const [filters, setFilters] = useState({
    lastName: "",
  });

  useEffect(() => {
    API.get("/patients")
      .then((response) => {
        setPatients(response.data);
        setFilteredPatients(response.data); // Initialize filtered data
      })
      .catch((error) => console.error("Error fetching patients:", error));
  }, []);

  // Handle filter changes
  const handleFilterChange = (key, value) => {
    setFilters({ ...filters, [key]: value });

    const newFilteredPatients = patients.filter((patient) => {
      return Object.keys(filters).every((filterKey) => {
        const filterValue = filterKey === key ? value : filters[filterKey];
        return (
          patient[filterKey]
            ?.toString()
            .toLowerCase()
            .includes(filterValue.toLowerCase())
        );
      });
    });

    setFilteredPatients(newFilteredPatients);
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
              Patients
              <input
                type="text"
                placeholder="Filtrer..."
                value={filters.firstName}
                onChange={(e) => handleFilterChange("firstName", e.target.value)}
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