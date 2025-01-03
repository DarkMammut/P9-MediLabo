package com.medilabo.notesservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.notesservice.model.Note;
import com.medilabo.notesservice.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        note1 = new Note("1", 123, "Test Note 1", "Details 1");
        note2 = new Note("2", 123, "Test Note 2", "Details 2");
    }

    @Test
    void getAllNotes_ShouldReturnListOfNotes() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Arrays.asList(note1, note2));

        mockMvc.perform(get("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(note1.getId()))
                .andExpect(jsonPath("$[1].id").value(note2.getId()));
    }

    @Test
    void getNoteById_ShouldReturnNote_WhenFound() throws Exception {
        when(noteService.getNoteById("1")).thenReturn(Optional.of(note1));

        mockMvc.perform(get("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(note1.getId()));
    }

    @Test
    void getNoteById_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(noteService.getNoteById("3")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notes/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotesByPatId_ShouldReturnNotes_WhenFound() throws Exception {
        when(noteService.getNotesByPatId(123)).thenReturn(Arrays.asList(note1, note2));

        mockMvc.perform(get("/api/notes/patient/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patId").value(123));
    }

    @Test
    void getNotesByPatId_ShouldReturnNotFound_WhenEmpty() throws Exception {
        when(noteService.getNotesByPatId(999)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/notes/patient/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNote_ShouldReturnCreatedNote() throws Exception {
        when(noteService.createNote(any(Note.class))).thenReturn(note1);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(note1.getId()));
    }

    @Test
    void updateNote_ShouldReturnUpdatedNote_WhenFound() throws Exception {
        Note updatedNote = new Note("1", 123, "Updated Note", "Updated Details");
        when(noteService.updateNote(eq("1"), any(Note.class))).thenReturn(updatedNote);

        mockMvc.perform(put("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.note").value("Updated Details"));
    }

    @Test
    void updateNote_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(noteService.updateNote(eq("3"), any(Note.class))).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(put("/api/notes/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNoteById_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(noteService).deleteNoteById("1");
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NoteService noteService() {
            return Mockito.mock(NoteService.class);
        }
    }
}