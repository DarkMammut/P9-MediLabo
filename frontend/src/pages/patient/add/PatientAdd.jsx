import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { format, parse } from 'date-fns'; // Importez format et parse
import API from "../../../api/api"; // Axios instance

const PatientAdd = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        gender: '',
        phoneNumber: '',
        address: '',
        birthdate: '',
    });

    const [errors, setErrors] = useState({});

    const handleInputChange = (e) => {
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
        if (!/^\d{2}\/\d{2}\/\d{4}$/.test(formData.birthdate)) 
            newErrors.birthdate = 'Birth date must be in the format DD/MM/YYYY';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSave = () => {
        if (!validateForm()) return;

        const confirmation = window.confirm("Are you sure you want to add this patient?");
        if (!confirmation) return;

        // Convertissez la date au format attendu par l'API
        const payload = {
            ...formData,
            gender: formData.gender === 'Male' ? 'M' : 'F',
            birthdate: formData.birthdate 
                ? format(parse(formData.birthdate, 'dd/MM/yyyy', new Date()), 'yyyy-MM-dd') 
                : '',
        };

        API.post('/patients/add', payload)
            .then(response => {
                alert('Patient successfully added!');
                navigate(`/patients/${response.data.id}`); // Redirection vers la page du patient ajouté
            })
            .catch(error => console.error("Error adding patient:", error));
    };

    const handleCancel = () => {
        navigate('/patients'); // Retourne à la liste des patients
    };

    return (
        <div>
            <h1>Add New Patient</h1>
            <form>
                <div>
                    <label htmlFor="firstName">First Name:</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleInputChange}
                    />
                    {errors.firstName && <p className="error">{errors.firstName}</p>}
                </div>

                <div>
                    <label htmlFor="lastName">Last Name:</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleInputChange}
                    />
                    {errors.lastName && <p className="error">{errors.lastName}</p>}
                </div>

                <div>
                    <label htmlFor="gender">Gender:</label>
                    <select
                        id="gender"
                        name="gender"
                        value={formData.gender}
                        onChange={handleInputChange}
                    >
                        <option value="">Select</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                    </select>
                    {errors.gender && <p className="error">{errors.gender}</p>}
                </div>

                <div>
                    <label htmlFor="phoneNumber">Phone Number:</label>
                    <input
                        type="text"
                        id="phoneNumber"
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handleInputChange}
                    />
                    {errors.phoneNumber && <p className="error">{errors.phoneNumber}</p>}
                </div>

                <div>
                    <label htmlFor="address">Address:</label>
                    <input
                        type="text"
                        id="address"
                        name="address"
                        value={formData.address}
                        onChange={handleInputChange}
                    />
                    {errors.address && <p className="error">{errors.address}</p>}
                </div>

                <div>
                    <label htmlFor="birthdate">Birth Date (DD/MM/YYYY):</label>
                    <input
                        type="text"
                        id="birthdate"
                        name="birthdate"
                        value={formData.birthdate}
                        onChange={handleInputChange}
                    />
                    {errors.birthdate && <p className="error">{errors.birthdate}</p>}
                </div>

                <button type="button" onClick={handleSave}>Add Patient</button>
                <button type="button" onClick={handleCancel}>Cancel</button>
            </form>
        </div>
    );
};

export default PatientAdd;