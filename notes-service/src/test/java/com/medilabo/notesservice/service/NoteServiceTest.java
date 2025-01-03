package com.medilabo.notesservice.service;

import com.medilabo.notesservice.model.Note;
import com.medilabo.notesservice.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class NoteServiceTest {

    private NoteRepository noteRepository;
    private NoteService noteService;
    private List<Note> mockNotes;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        noteService = new NoteService(noteRepository);

        // Initialize mock notes with builder
        mockNotes = new ArrayList<>();
        mockNotes.add(Note.builder()
                .id("1")
                .patId(101)
                .patient("Patient A")
                .note("Note 1")
                .build());
        mockNotes.add(Note.builder()
                .id("2")
                .patId(102)
                .patient("Patient B")
                .note("Note 2")
                .build());
    }

    @Test
    void getAllNotes_ShouldReturnListOfNotes() {
        when(noteRepository.findAll()).thenReturn(mockNotes);

        List<Note> notes = noteService.getAllNotes();

        assertEquals(2, notes.size());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void getNoteById_ShouldReturnNote_WhenNoteExists() {
        when(noteRepository.findById("1")).thenReturn(Optional.of(mockNotes.get(0)));

        Optional<Note> note = noteService.getNoteById("1");

        assertTrue(note.isPresent());
        assertEquals("Patient A", note.get().getPatient());
        verify(noteRepository, times(1)).findById("1");
    }

    @Test
    void getNoteById_ShouldReturnEmpty_WhenNoteDoesNotExist() {
        when(noteRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Note> note = noteService.getNoteById("99");

        assertFalse(note.isPresent());
        verify(noteRepository, times(1)).findById("99");
    }

    @Test
    void getNotesByPatId_ShouldReturnNotesForGivenPatientId() {
        when(noteRepository.findByPatId(101)).thenReturn(List.of(mockNotes.get(0)));

        List<Note> notes = noteService.getNotesByPatId(101);

        assertEquals(1, notes.size());
        verify(noteRepository, times(1)).findByPatId(101);
    }

    @Test
    void createNote_ShouldSaveAndReturnNote() {
        Note newNote = Note.builder()
                .id("3")
                .patId(103)
                .patient("Patient C")
                .note("Note 3")
                .build();

        when(noteRepository.save(newNote)).thenReturn(newNote);

        Note savedNote = noteService.createNote(newNote);

        assertEquals("3", savedNote.getId());
        verify(noteRepository, times(1)).save(newNote);
    }

    @Test
    void updateNote_ShouldUpdateAndReturnUpdatedNote_WhenNoteExists() {
        Note existingNote = mockNotes.get(0);
        Note updatedNote = Note.builder()
                .id("1")
                .patId(102)
                .patient("Patient B")
                .note("Updated Note")
                .build();

        when(noteRepository.findById("1")).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(existingNote)).thenReturn(updatedNote);

        Note result = noteService.updateNote("1", updatedNote);

        assertEquals("Updated Note", result.getNote());
        assertEquals(102, result.getPatId());
        verify(noteRepository, times(1)).findById("1");
        verify(noteRepository, times(1)).save(existingNote);
    }

    @Test
    void updateNote_ShouldThrowException_WhenNoteDoesNotExist() {
        Note updatedNote = Note.builder()
                .id("1")
                .patId(102)
                .patient("Patient B")
                .note("Updated Note")
                .build();

        when(noteRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            noteService.updateNote("1", updatedNote);
        });

        assertEquals("Note not found with id 1", exception.getMessage());
        verify(noteRepository, times(1)).findById("1");
    }

    @Test
    void deleteNoteById_ShouldDeleteNote_WhenNoteExists() {
        doNothing().when(noteRepository).deleteById("1");

        noteService.deleteNoteById("1");

        verify(noteRepository, times(1)).deleteById("1");
    }
}
