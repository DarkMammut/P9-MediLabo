//package com.medilabo.patientservice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.medilabo.patientservice.model.Patient;
//import com.medilabo.patientservice.service.PatientService;
//import jakarta.ws.rs.core.MediaType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(PatientController.class)
//class PatientControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;  // Inject MockMvc automatically provided by @WebMvcTest
//
//    @Mock
//    private PatientService patientService; // Mock the PatientService
//
//    @InjectMocks
//    private PatientController patientController;  // Inject the mock into the controller
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    private Patient patient1;
//    private Patient patient2;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        patient1 = Patient.builder()
//                .id(1L)
//                .gender("M")
//                .firstName("John")
//                .lastName("Doe")
//                .birthdate(LocalDate.parse("2000-01-01"))
//                .address("123 Street")
//                .phoneNumber("123-456-7890")
//                .build();
//
//        patient2 = Patient.builder()
//                .id(2L)
//                .gender("F")
//                .firstName("Jane")
//                .lastName("Smith")
//                .birthdate(LocalDate.parse("1995-05-05"))
//                .address("456 Avenue")
//                .phoneNumber("987-654-3210")
//                .build();
//    }
//
//    @Test
//    void getAllPatients_ShouldReturnListOfPatients() throws Exception {
//        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient1, patient2));
//
//        mockMvc.perform(get("/api/patients")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(patient1.getId()))
//                .andExpect(jsonPath("$[1].id").value(patient2.getId()));
//    }
//
//
//}
