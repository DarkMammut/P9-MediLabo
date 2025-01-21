package com.medilabo.riskservice.client;

import com.medilabo.riskservice.model.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "patient-service", url = "${patient-service.url}")
public interface PatientServiceClient {
    @GetMapping("/api/patients/{patientId}")
    PatientDTO getPatientById(@PathVariable("patientId") Long patientId);
}

