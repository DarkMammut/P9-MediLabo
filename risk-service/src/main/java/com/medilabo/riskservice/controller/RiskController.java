package com.medilabo.riskservice.controller;

import com.medilabo.riskservice.client.NotesServiceClient;
import com.medilabo.riskservice.client.PatientServiceClient;
import com.medilabo.riskservice.model.NoteDTO;
import com.medilabo.riskservice.model.PatientDTO;
import com.medilabo.riskservice.service.RiskAssessmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final PatientServiceClient patientServiceClient;
    private final NotesServiceClient notesServiceClient;
    private final RiskAssessmentService riskAssessmentService;

    public RiskController(PatientServiceClient patientServiceClient, NotesServiceClient notesServiceClient, RiskAssessmentService riskAssessmentService) {
        this.patientServiceClient = patientServiceClient;
        this.notesServiceClient = notesServiceClient;
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<String> getRiskAssessment(@PathVariable Long patientId) {
        PatientDTO patientDTO= patientServiceClient.getPatientById(patientId);
        List<NoteDTO> notesDTO = notesServiceClient.getNotesByPatId(patientId);

        if (patientDTO == null || notesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient or notes not found.");
        }

        String assessment = riskAssessmentService.assessRisk(patientDTO, notesDTO);
        return ResponseEntity.ok(assessment);
    }
}