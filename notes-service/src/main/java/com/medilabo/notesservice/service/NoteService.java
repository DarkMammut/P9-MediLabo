package com.medilabo.notesservice.service;

import com.medilabo.notesservice.model.Note;
import com.medilabo.notesservice.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(String id) {
        return noteRepository.findById(id);
    }

    public List<Note> getNotesByPatId(int patId) {
        return noteRepository.findByPatId(patId);
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(String id, Note updatedNote) {
        return noteRepository.findById(id).map(note -> {
            note.setPatId(updatedNote.getPatId());
            note.setPatient(updatedNote.getPatient());
            note.setNote(updatedNote.getNote());
            return noteRepository.save(note);
        }).orElseThrow(() -> new RuntimeException("Note not found with id " + id));
    }

    public void deleteNoteById(String id) {
        noteRepository.deleteById(id);
    }
}
