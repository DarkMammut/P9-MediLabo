package com.medilabo.notesservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notes")
public class Note {

    @Id
    private String id; // Identifiant unique généré par MongoDB

    @NotNull(message = "Patient Id is mandatory")
    private int patId; // ID du patient

    @NotBlank(message = "Patient name is mandatory")
    private String patient; // Nom du patient

    @NotBlank(message = "Note is mandatory")
    private String note; // Contenu de la note
}