import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { format, parse } from 'date-fns'; // Importez format et parse
import API from "./api"; // Axios instance

function Patient({ id }) {
    const navigate = useNavigate();
    const [patient, setPatient] = useState(null);
    const [isEditingPatient, setIsEditingPatient] = useState(false);
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        gender: '',
        phoneNumber: '',
        address: '',
        birthdate: '',
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (!id) return;
        API.get(`/api/patients/${id}`)
            .then(response => {
                const patientData = response.data;
                setPatient(patientData);

                // Formatez la date reçue pour l'affichage
                const formattedBirthDate = patientData.birthdate 
                    ? format(new Date(patientData.birthdate), 'dd/MM/yyyy') 
                    : '';

                setFormData({
                    firstName: patientData.firstName,
                    lastName: patientData.lastName,
                    gender: patientData.gender === 'M' ? 'Male' : 'Female',
                    phoneNumber: patientData.phoneNumber || '',
                    address: patientData.address || '',
                    birthdate: formattedBirthDate,
                });
            })
            .catch(error => console.error("Error fetching patient:", error));
    }, [id]);

    const handleInputChangePatient = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const validateForm = () => {
        const newErrors = {};
        if (!formData.firstName.trim()) newErrors.firstName = 'First name is required';
        if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required';
        if (!['Male', 'Female'].includes(formData.gender)) newErrors.gender = 'Gender must be Male or Female';
        if (!/^[0-9]{10}$/.test(formData.phoneNumber)) newErrors.phoneNumber = 'Phone number must be 10 digits';
        if (!formData.address.trim()) newErrors.address = 'Address is required';
        if (!formData.birthdate.match(/^\d{2}\/\d{2}\/\d{4}$/))
            newErrors.birthdate = 'Birth date must be in the format DD/MM/YYYY';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleEditPatient = () => {
        setIsEditingPatient(true);
    };

    const handleCancelPatient = () => {
        setIsEditingPatient(false);
        if (patient) {
            setFormData({
                firstName: patient.firstName,
                lastName: patient.lastName,
                gender: patient.gender === 'M' ? 'Male' : 'Female',
                phoneNumber: patient.phoneNumber || '',
                address: patient.address || '',
                birthdate: patient.birthdate 
                    ? format(new Date(patient.birthdate), 'dd/MM/yyyy') 
                    : '',
            });
        }
        setErrors({});
    };

    const handleSavePatient = () => {
        if (!validateForm()) return;

        const confirmation = window.confirm("Are you sure you want to save these changes?");
        if (!confirmation) return;

        // Convertissez la date au format attendu par l'API
        const payload = {
            ...formData,
            gender: formData.gender === 'Male' ? 'M' : 'F',
            birthdate: formData.birthdate 
                ? format(parse(formData.birthdate, 'dd/MM/yyyy', new Date()), 'yyyy-MM-dd') 
                : '',
        };

        API.put(`/api/patients/${id}`, payload)
            .then(response => {
                setPatient(response.data);
                setIsEditingPatient(false);
            })
            .catch(error => console.error("Error updating patient:", error));
    };

    const handleDeletePatient = () => {
        const confirmation = window.confirm("Are you sure you want to delete this patient?");
        if (!confirmation) return;

        API.delete(`/api/patients/${id}`)
            .then(() => {
                alert("Patient deleted successfully");
                navigate('/patients'); // Redirigez vers la liste des patients après la suppression
            })
            .catch(error => console.error("Error deleting patient:", error));
    };

    if (!patient) return <p>Loading...</p>;

    return (
        <div>
            <h1>{formData.firstName} {formData.lastName}</h1>
            <form>
                <div>
                    <label htmlFor="gender">Gender:</label>
                    {isEditingPatient ? (
                        <select
                            id="gender"
                            name="gender"
                            value={formData.gender}
                            onChange={handleInputChangePatient}
                        >
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                        </select>
                    ) : (
                        <span>{patient.gender === 'M' ? 'Male' : 'Female'}</span>
                    )}
                    {errors.gender && <p className="error">{errors.gender}</p>}
                </div>

                <div>
                    <label htmlFor="phone">Phone:</label>
                    {isEditingPatient ? (
                        <input
                            type="text"
                            id="phone"
                            name="phone"
                            value={formData.phoneNumber}
                            onChange={handleInputChangePatient}
                        />
                    ) : (
                        <span>{patient.phoneNumber}</span>
                    )}
                    {errors.phoneNumber && <p className="error">{errors.phoneNumber}</p>}
                </div>

                <div>
                    <label htmlFor="address">Address:</label>
                    {isEditingPatient ? (
                        <input
                            type="text"
                            id="address"
                            name="address"
                            value={formData.address}
                            onChange={handleInputChangePatient}
                        />
                    ) : (
                        <span>{patient.address}</span>
                    )}
                    {errors.address && <p className="error">{errors.address}</p>}
                </div>

                <div>
                    <label htmlFor="birthdate">Birth Date:</label>
                    {isEditingPatient ? (
                        <input
                            type="text"
                            id="birthdate"
                            name="birthdate"
                            value={formData.birthdate}
                            onChange={handleInputChangePatient}
                        />
                    ) : (
                        <span>{formData.birthdate}</span>
                    )}
                    {errors.birthdate && <p className="error">{errors.birthdate}</p>}
                </div>

                {isEditingPatient ? (
                    <>
                        <button type="button" onClick={handleSavePatient}>Valider</button>
                        <button type="button" onClick={handleCancelPatient}>Annuler</button>
                    </>
                ) : (
                    <button type="button" onClick={handleEditPatient}>Modifier</button>
                )}
            </form>
            <button type="button" onClick={handleDeletePatient} style={{ marginTop: '20px', color: 'red' }}>Supprimer</button>
        </div>
    );
};

export default Patient;