import React, { useState, useEffect } from 'react';
import API from "./api"; // Axios instance

function Notes({ patientId }) {
    const [notes, setNotes] = useState([]); // Liste des notes
    const [isEditing, setIsEditing] = useState(false); // Si on est en mode édition
    const [formData, setFormData] = useState({
        note: '', // Contenu de la note
    });
    const [errors, setErrors] = useState({});

    // Récupération des notes du patient lors du premier rendu
    useEffect(() => {
        if (!patientId) return;
        API.get(`/api/notes/patient/${patientId}`)
            .then(response => {
                setNotes(response.data);
                console.log(response.data)
            })
            .catch(error => console.error("Error fetching notes:", error));
    }, [patientId]);

    // Gestion du changement d'entrée dans le formulaire de note
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
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

        const payload = {
            ...formData,
            patId: patientId, // Associe la note au patient
        };

        if (isEditing) {
            // Si on est en mode édition, on met à jour la note
            API.put(`/api/notes/${formData.id}`, payload)
                .then(response => {
                    setNotes(notes.map(note => note.id === formData.id ? response.data : note));
                    setIsEditing(false);
                    setFormData({ note: '' });
                })
                .catch(error => console.error("Error updating note:", error));
        } else {
            // Si on n'est pas en mode édition, on ajoute une nouvelle note
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
        const confirmation = window.confirm("Are you sure you want to delete this note?");
        if (!confirmation) return;

        API.delete(`/api/notes/${noteId}`)
            .then(() => {
                setNotes(notes.filter(note => note.id !== noteId));
            })
            .catch(error => console.error("Error deleting note:", error));
    };

    // Modifier une note
    const handleEditNote = (note) => {
        setIsEditing(true);
        setFormData({
            id: note.id, // L'ID de la note pour l'édition
            note: note.note,
        });
    };

    if (!notes) return <p>Loading notes...</p>;

    return (
        <div>
            <h2>Notes</h2>

            <form>
                <div>
                    <label htmlFor="note">Note:</label>
                    <textarea
                        id="note"
                        name="note"
                        value={formData.note}
                        onChange={handleInputChange}
                    />
                    {errors.note && <p className="error">{errors.note}</p>}
                </div>

                <button type="button" onClick={handleSaveNote}>
                    {isEditing ? 'Save Changes' : 'Add Note'}
                </button>
            </form>

            <h2>Existing Notes</h2>
            <ul>
                {notes.map(note => (
                    <li key={note.id}>
                        <p>{note.note}</p>
                        <button type="button" onClick={() => handleEditNote(note)}>
                            Edit
                        </button>
                        <button type="button" onClick={() => handleDeleteNote(note.id)}>
                            Delete
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Notes;
