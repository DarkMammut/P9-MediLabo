import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { format, parse } from 'date-fns';
import API from "../../api/api";

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
        setFormData({ ...formData, [name]: value });
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

    const handleEditPatient = () => setIsEditingPatient(true);
    const handleCancelPatient = () => {
        setIsEditingPatient(false);
        if (patient) {
            const formattedBirthDate = patient.birthdate
                ? format(new Date(patient.birthdate), 'dd/MM/yyyy')
                : '';
            setFormData({
                firstName: patient.firstName,
                lastName: patient.lastName,
                gender: patient.gender === 'M' ? 'Male' : 'Female',
                phoneNumber: patient.phoneNumber || '',
                address: patient.address || '',
                birthdate: formattedBirthDate,
            });
        }
        setErrors({});
    };

    const handleSavePatient = () => {
        if (!validateForm()) return;
        if (!window.confirm("Are you sure you want to save these changes?")) return;

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
        if (!window.confirm("Are you sure you want to delete this patient?")) return;

        API.delete(`/api/patients/${id}`)
            .then(() => {
                alert("Patient deleted successfully");
                navigate('/patients');
            })
            .catch(error => console.error("Error deleting patient:", error));
    };

    if (!patient) return <p>Loading...</p>;

    return (
        <div className="container mt-4">
            <h1 className="mb-4">{formData.firstName} {formData.lastName}</h1>
            <span>{id}</span>
            <form>
                <div className="row mb-3">
                    <label htmlFor="gender" className="col-sm-2 col-form-label">Gender:</label>
                    <div className="col-sm-10">
                        {isEditingPatient ? (
                            <select
                                id="gender"
                                name="gender"
                                value={formData.gender}
                                onChange={handleInputChangePatient}
                                className="form-select"
                            >
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        ) : (
                            <p className="form-control-plaintext">{formData.gender}</p>
                        )}
                        {errors.gender && <div className="text-danger">{errors.gender}</div>}
                    </div>
                </div>

                <div className="row mb-3">
                    <label htmlFor="phone" className="col-sm-2 col-form-label">Phone:</label>
                    <div className="col-sm-10">
                        {isEditingPatient ? (
                            <input
                                type="text"
                                id="phone"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleInputChangePatient}
                                className="form-control"
                            />
                        ) : (
                            <p className="form-control-plaintext">{formData.phoneNumber}</p>
                        )}
                        {errors.phoneNumber && <div className="text-danger">{errors.phoneNumber}</div>}
                    </div>
                </div>

                <div className="row mb-3">
                    <label htmlFor="address" className="col-sm-2 col-form-label">Address:</label>
                    <div className="col-sm-10">
                        {isEditingPatient ? (
                            <input
                                type="text"
                                id="address"
                                name="address"
                                value={formData.address}
                                onChange={handleInputChangePatient}
                                className="form-control"
                            />
                        ) : (
                            <p className="form-control-plaintext">{formData.address}</p>
                        )}
                        {errors.address && <div className="text-danger">{errors.address}</div>}
                    </div>
                </div>

                <div className="row mb-3">
                    <label htmlFor="birthdate" className="col-sm-2 col-form-label">Birth Date:</label>
                    <div className="col-sm-10">
                        {isEditingPatient ? (
                            <input
                                type="text"
                                id="birthdate"
                                name="birthdate"
                                value={formData.birthdate}
                                onChange={handleInputChangePatient}
                                className="form-control"
                            />
                        ) : (
                            <p className="form-control-plaintext">{formData.birthdate}</p>
                        )}
                        {errors.birthdate && <div className="text-danger">{errors.birthdate}</div>}
                    </div>
                </div>

                <div className="d-flex">
                    {isEditingPatient ? (
                        <>
                            <button type="button" className="btn btn-primary me-2" onClick={handleSavePatient}>
                                Save
                            </button>
                            <button type="button" className="btn btn-secondary" onClick={handleCancelPatient}>
                                Cancel
                            </button>
                        </>
                    ) : (
                        <button type="button" className="btn btn-warning" onClick={handleEditPatient}>
                            Edit
                        </button>
                    )}
                </div>
            </form>
            <button
                type="button"
                className="btn btn-danger mt-4"
                onClick={handleDeletePatient}
            >
                Delete
            </button>
        </div>
    );
}

export default Patient;