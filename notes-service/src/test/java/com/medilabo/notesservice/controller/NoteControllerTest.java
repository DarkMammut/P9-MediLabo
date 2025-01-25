package com.medilabo.notesservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.notesservice.model.Note;
import com.medilabo.notesservice.service.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
@Import(NoteController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test") // Utilise le profil de test
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;

    private Note getNotes(int index) {
        Note note1 = new Note("1", 123, "Test Note 1", "Details 1");
        Note note2 = new Note("2", 123, "Test Note 2", "Details 2");

        if(index == 1) return note1;
        if(index == 2) return note2;
        return null;
    }

    @Test
    void getAllNotes_ShouldReturnListOfNotes() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Arrays.asList(getNotes(1), getNotes(2)));

        mockMvc.perform(get("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(getNotes(1).getId()))
                .andExpect(jsonPath("$[1].id").value(getNotes(2).getId()));
    }

    @Test
    void getNoteById_ShouldReturnNote_WhenFound() throws Exception {
        when(noteService.getNoteById("1")).thenReturn(Optional.ofNullable(getNotes(1)));

        mockMvc.perform(get("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Objects.requireNonNull(getNotes(1)).getId()));
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
        when(noteService.getNotesByPatId(123)).thenReturn(Arrays.asList(getNotes(1), getNotes(2)));

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
        when(noteService.createNote(any(Note.class))).thenReturn(getNotes(1));

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getNotes(1))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Objects.requireNonNull(getNotes(1)).getId()));
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
                        .content(objectMapper.writeValueAsString(getNotes(1))))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNoteById_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(noteService).deleteNoteById("1");
    }

    @Configuration
    static class TestConfig {
        @Bean
        public NoteService noteService() {
            return Mockito.mock(NoteService.class);
        }
    }
}
