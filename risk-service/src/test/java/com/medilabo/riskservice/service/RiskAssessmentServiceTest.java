package com.medilabo.riskservice.service;

import com.medilabo.riskservice.model.NoteDTO;
import com.medilabo.riskservice.model.PatientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskAssessmentServiceTest {

    private final RiskAssessmentService service = new RiskAssessmentService();
    private PatientDTO defaultPatient;
    private NoteDTO defaultNote;

    @BeforeEach
    void setUp() {
        // Patient par défaut
        defaultPatient = PatientDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .gender("M")
                .address("123 Street")
                .phoneNumber("1234567890")
                .build();

        // Note par défaut
        defaultNote = NoteDTO.builder()
                .id("note1")
                .patId(1)
                .patient("John Doe")
                .note("")
                .build();
    }

    @Test
    void testAssessRisk_NoKeywords() {
        // Utilise les valeurs par défaut
        PatientDTO patient = defaultPatient;
        NoteDTO note = defaultNote.toBuilder().note("Pas de risque.").build();

        String result = service.assessRisk(patient, List.of(note));

        assertEquals("None", result, "Expected 'None' for notes with no risk keywords.");
    }

    @Test
    void testAssessRisk_SingleKeyword() {
        PatientDTO patient = defaultPatient;
        NoteDTO note = defaultNote.toBuilder().note("patient : hémoglobine A1C, microalbumine.").build();

        String result = service.assessRisk(patient, List.of(note));

        assertEquals("Borderline", result, "Expected 'Borderline' for a single risk keyword.");
    }

    @Test
    void testAssessRisk_MultipleKeywords() {
        PatientDTO patient = defaultPatient.toBuilder().birthdate(LocalDate.of(2000, 5, 20)).gender("F").build();
        NoteDTO note1 = defaultNote.toBuilder().note("patient : hémoglobine A1C, taille, poids.").build();
        NoteDTO note2 = defaultNote.toBuilder().note("cholesterol, rechute, vertiges, anticorps.").build();

        String result = service.assessRisk(patient, List.of(note1, note2));

        assertEquals("Early onset", result, "Expected 'Early onset' for multiple risk keywords.");
    }

    @Test
    void testAssessRisk_Normalization() {
        PatientDTO patient = defaultPatient;
        NoteDTO note = defaultNote.toBuilder().note("Hemoglobine A1C / FUMEUR").build();

        String result = service.assessRisk(patient, List.of(note));

        assertEquals("Borderline", result, "Expected 'Borderline' after normalization.");
    }

    @Test
    void testAssessRisk_AgeAndGenderLogic() {
        PatientDTO patient = defaultPatient.toBuilder().birthdate(LocalDate.of(2010, 3, 25)).gender("F").build();
        NoteDTO note = defaultNote.toBuilder().note("patient : anormal, rechute, poids, taille.").build();

        String result = service.assessRisk(patient, List.of(note));

        assertEquals("In Danger", result, "Expected 'In Danger' for specific age and gender logic.");
    }
}
