package com.medilabo.riskservice.controller;

import com.medilabo.riskservice.client.NotesServiceClient;
import com.medilabo.riskservice.client.PatientServiceClient;
import com.medilabo.riskservice.model.NoteDTO;
import com.medilabo.riskservice.model.PatientDTO;
import com.medilabo.riskservice.service.RiskAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RiskControllerTest {

    @Mock
    private PatientServiceClient patientServiceClient;

    @Mock
    private NotesServiceClient notesServiceClient;

    @Mock
    private RiskAssessmentService riskAssessmentService;

    @InjectMocks
    private RiskController riskController;

    private PatientDTO defaultPatient;
    private NoteDTO defaultNote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        defaultPatient = PatientDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .gender("M")
                .address("123 Street")
                .phoneNumber("1234567890")
                .build();

        defaultNote = NoteDTO.builder()
                .id("note1")
                .patId(1)
                .patient("John Doe")
                .note("The patient has hémoglobine A1C.")
                .build();
    }

    @Test
    void testGetRiskAssessment_Success() {
        // Arrange
        when(patientServiceClient.getPatientById(1L)).thenReturn(defaultPatient);
        when(notesServiceClient.getNotesByPatId(1L)).thenReturn(List.of(defaultNote));
        when(riskAssessmentService.assessRisk(defaultPatient, List.of(defaultNote))).thenReturn("In Danger");

        // Act
        ResponseEntity<String> response = riskController.getRiskAssessment(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("In Danger", response.getBody());

        verify(patientServiceClient, times(1)).getPatientById(1L);
        verify(notesServiceClient, times(1)).getNotesByPatId(1L);
        verify(riskAssessmentService, times(1)).assessRisk(defaultPatient, List.of(defaultNote));
    }

    @Test
    void testGetRiskAssessment_PatientNotFound() {
        // Arrange
        when(patientServiceClient.getPatientById(1L)).thenReturn(null);

        // Act
        ResponseEntity<String> response = riskController.getRiskAssessment(1L);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Patient or notes not found.", response.getBody());

        verify(patientServiceClient, times(1)).getPatientById(1L);
//        verifyNoInteractions(notesServiceClient, riskAssessmentService);
    }

    @Test
    void testGetRiskAssessment_NotesNotFound() {
        // Arrange
        when(patientServiceClient.getPatientById(1L)).thenReturn(defaultPatient);
        when(notesServiceClient.getNotesByPatId(1L)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<String> response = riskController.getRiskAssessment(1L);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Patient or notes not found.", response.getBody());

        verify(patientServiceClient, times(1)).getPatientById(1L);
        verify(notesServiceClient, times(1)).getNotesByPatId(1L);
        verifyNoInteractions(riskAssessmentService);
    }
}
