import React from 'react'; 
import { useParams } from 'react-router-dom';
import Patient from '../../../api/patient';
import Notes from '../../../api/note'

const PatientDetail = () => {
    const { id } = useParams();

    return (
        <main className="Patient">
            <span>{id}</span>
            <Patient id={id} />
            <Notes patientId={id} />
        </main>
    );
};

export default PatientDetail;
