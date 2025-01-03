package com.medilabo.notesservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.notesservice.model.Note;
import com.medilabo.notesservice.repository.NoteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader {

    private final NoteRepository noteRepository;
    private final ObjectMapper objectMapper;

    public DataLoader(NoteRepository noteRepository, ObjectMapper objectMapper) {
        this.noteRepository = noteRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadData() {
        try {
            // Charge le fichier JSON
            InputStream inputStream = getClass().getResourceAsStream("/data.json");
            List<Note> notes = objectMapper.readValue(inputStream, new TypeReference<List<Note>>() {});

            // Insère les données dans MongoDB si elles n'existent pas déjà
            if (noteRepository.count() == 0) {
                noteRepository.saveAll(notes);
                System.out.println("Données initialisées dans MongoDB.");
            } else {
                System.out.println("Les données existent déjà, pas d'initialisation nécessaire.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement des données initiales : " + e.getMessage());
        }
    }
}