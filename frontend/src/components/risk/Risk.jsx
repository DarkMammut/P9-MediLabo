import React, { useState, useEffect } from 'react';
import API from "../../api/api"; // Axios instance

function Risk({ id }) {
    const [risk, setRisk] = useState();
    const [error, setError] = useState(null);

    // Fonction pour déterminer la classe Bootstrap en fonction du niveau de risque
    const getRiskClass = (riskLevel) => {
        switch (riskLevel) {
            case "None":
                return "alert alert-success"; // Vert
            case "Borderline":
                return "alert alert-warning"; // Jaune
            case "In Danger":
                return "alert alert-danger"; // Rouge
            case "Early onset":
                return "alert alert-dark"; // Noir ou sombre
            default:
                return "alert alert-secondary"; // Par défaut (gris)
        }
    };

    useEffect(() => {
        if (!id) return;

        API.get(`/api/risk/${id}`)
            .then(response => {
                setRisk(response.data);
                console.log(response.data);
            })
            .catch(error => {
                console.error("Error fetching risk:", error);
                setError("Unable to fetch risk assessment.");
            });
    }, [id]);

    return (
        <div>
            {error ? (
                <div className="alert alert-danger">{error}</div>
            ) : (
                risk && (
                    <div className={getRiskClass(risk)}>
                        <strong>Risk Level:</strong> {risk}
                    </div>
                )
            )}
        </div>
    );
}

export default Risk;