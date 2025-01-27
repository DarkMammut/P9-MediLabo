package com.medilabo.patientservice.controller;

import com.medilabo.patientservice.model.Patient;
import com.medilabo.patientservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test") // Utilise le profil de test
class PatientControllerTest {

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

        if (index == 1) return patient1;
        if (index == 2) return patient2;
        return null;
    }

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
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

    @Test
    void getPatientById_ShouldReturnPatient_WhenExists() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.ofNullable(getPatients(1)));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getPatientById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePatientById_ShouldDeletePatient_WhenExists() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.ofNullable(getPatients(1)));
        doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void deletePatientById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePatient_ShouldUpdatePatient_WhenExists() throws Exception {
        Patient updatedPatient = Patient.builder()
                .id(1L)
                .gender("M")
                .firstName("John")
                .lastName("Smith")
                .birthdate(LocalDate.parse("2000-01-01"))
                .address("123 Street")
                .phoneNumber("123-456-7890")
                .build();
        when(patientService.getPatientById(1L)).thenReturn(Optional.ofNullable(getPatients(1)));
        when(patientService.updatePatient(1L, updatedPatient)).thenReturn(updatedPatient);

        mockMvc.perform(put("/api/patients/1")
                        .contentType("application/json")
                        .content("{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"gender\":\"M\",\"birthdate\":\"2000-01-01\",\"address\":\"123 Street\",\"phoneNumber\":\"123-456-7890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void addPatient_ShouldCreatePatient() throws Exception {
        Patient createdPatient = Patient.builder()
                .id(1L)
                .gender("M")
                .firstName("John")
                .lastName("Doe")
                .birthdate(LocalDate.parse("2000-01-01"))
                .address("123 Street")
                .phoneNumber("123-456-7890")
                .build();
        when(patientService.addPatient(getPatients(1))).thenReturn(createdPatient);

        mockMvc.perform(post("/api/patients/add")
                        .contentType("application/json")
                        .content("{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"gender\":\"M\",\"birthdate\":\"2000-01-01\",\"address\":\"123 Street\",\"phoneNumber\":\"123-456-7890\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
