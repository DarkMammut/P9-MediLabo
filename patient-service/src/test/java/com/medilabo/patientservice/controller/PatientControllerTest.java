package com.medilabo.patientservice.controller;

import com.medilabo.patientservice.model.Patient;
import com.medilabo.patientservice.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@Import(PatientController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test") // Utilise le profil de test
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Inject MockMvc automatically provided by @WebMvcTest

    @MockBean
    private PatientService patientService; // Mock the PatientService

    private Patient getPatients(int index) {

        Patient patient1 = Patient.builder()
                .id(1L)
                .gender("M")
                .firstName("John")
                .lastName("Doe")
                .birthdate(LocalDate.parse("2000-01-01"))
                .address("123 Street")
                .phoneNumber("123-456-7890")
                .build();

        Patient patient2 = Patient.builder()
                .id(2L)
                .gender("F")
                .firstName("Jane")
                .lastName("Smith")
                .birthdate(LocalDate.parse("1995-05-05"))
                .address("456 Avenue")
                .phoneNumber("987-654-3210")
                .build();

        if(index == 1) return patient1;
        if(index == 2) return patient2;
        return null;
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(getPatients(1), getPatients(2)));

        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(Objects.requireNonNull(getPatients(1)).getId()))
                .andExpect(jsonPath("$[1].id").value(Objects.requireNonNull(getPatients(2)).getId()));
    }
}
