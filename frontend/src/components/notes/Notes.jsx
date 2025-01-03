import React, { useState, useEffect } from 'react';
import API from "../../api/api";
function Notes({ patientId }) {
    const [notes, setNotes] = useState([]); // Liste des notes
    const [isEditing, setIsEditing] = useState(false); // Si on est en mode édition
    const [formData, setFormData] = useState({ note: '' }); // Contenu de la note
    const [errors, setErrors] = useState({});

    // Récupération des notes du patient lors du premier rendu
    useEffect(() => {
        if (!patientId) return;
        API.get(`/api/notes/patient/${patientId}`)
            .then(response => setNotes(response.data))
            .catch(error => console.error("Error fetching notes:", error));
    }, [patientId]);

    // Gestion du changement d'entrée dans le formulaire de note
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Validation du formulaire avant enregistrement
    const validateForm = () => {
        const newErrors = {};
        if (!formData.note.trim()) newErrors.note = 'Note is required';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    // Enregistrer ou mettre à jour la note
    const handleSaveNote = () => {
        if (!validateForm()) return;
        const payload = { ...formData, patId: patientId };

        if (isEditing) {
            API.put(`/api/notes/${formData.id}`, payload)
                .then(response => {
                    setNotes(notes.map(note => note.id === formData.id ? response.data : note));
                    setIsEditing(false);
                    setFormData({ note: '' });
                })
                .catch(error => console.error("Error updating note:", error));
        } else {
            API.post('/api/notes', payload)
                .then(response => {
                    setNotes([...notes, response.data]);
                    setFormData({ note: '' });
                })
                .catch(error => console.error("Error adding note:", error));
        }
    };

    // Supprimer une note
    const handleDeleteNote = (noteId) => {
        if (!window.confirm("Are you sure you want to delete this note?")) return;
        API.delete(`/api/notes/${noteId}`)
            .then(() => setNotes(notes.filter(note => note.id !== noteId)))
            .catch(error => console.error("Error deleting note:", error));
    };

    // Modifier une note
    const handleEditNote = (note) => {
        setIsEditing(true);
        setFormData({ id: note.id, note: note.note });
    };

    if (!notes) return <p>Loading notes...</p>;

    return (
        <div className="container mt-5">
            <h2 className="text-primary">Notes</h2>
            <form className="mb-4">
                <div className="mb-3">
                    <label htmlFor="note" className="form-label">Note:</label>
                    <textarea
                        id="note"
                        name="note"
                        className={`form-control ${errors.note ? 'is-invalid' : ''}`}
                        value={formData.note}
                        onChange={handleInputChange}
                    />
                    {errors.note && <div className="invalid-feedback">{errors.note}</div>}
                </div>
                <button type="button" className="btn btn-primary" onClick={handleSaveNote}>
                    {isEditing ? 'Save Changes' : 'Add Note'}
                </button>
            </form>

            <h3 className="text-secondary">Existing Notes</h3>
            {notes.length > 0 ? (
                <ul className="list-group">
                    {notes.map(note => (
                        <li key={note.id} className="list-group-item d-flex justify-content-between align-items-center">
                            <span>{note.note}</span>
                            <div>
                                <button
                                    type="button"
                                    className="btn btn-outline-secondary btn-sm me-2"
                                    onClick={() => handleEditNote(note)}
                                >
                                    Edit
                                </button>
                                <button
                                    type="button"
                                    className="btn btn-outline-danger btn-sm"
                                    onClick={() => handleDeleteNote(note.id)}
                                >
                                    Delete
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className="text-muted">No notes available for this patient.</p>
            )}
        </div>
    );
}

export default Notes;