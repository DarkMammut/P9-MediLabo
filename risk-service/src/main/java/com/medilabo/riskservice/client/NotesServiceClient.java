package com.medilabo.riskservice.client;

import com.medilabo.riskservice.model.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "notes-service", url = "${notes-service.url}")
public interface NotesServiceClient {
    @GetMapping("/api/notes/patient/{patientId}")
    List<NoteDTO> getNotesByPatId(@PathVariable("patientId") Long patientId);
}

