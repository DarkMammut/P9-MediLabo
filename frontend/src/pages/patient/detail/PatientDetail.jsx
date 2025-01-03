import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';

import Patient from '../../../components/patient/Patient';
import Risk from '../../../components/risk/Risk';
import Notes from '../../../components/notes/Notes';

const PatientDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const handleBack = () => {
        navigate(-1); // Retourne à la page précédente
    };

    return (
        <main className="Patient">
            <div className="container mt-4">
                <button 
                    className="btn btn-secondary mb-4" 
                    onClick={handleBack}
                >
                    Retour
                </button>
                <Risk id={id} />
                <Patient id={id} />
                <Notes patientId={id} />
            </div>
        </main>
    );
};

export default PatientDetail;