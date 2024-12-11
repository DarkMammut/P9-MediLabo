//package com.medilabo.patientservice.service;
//
//import com.medilabo.patientservice.model.Patient;
//import com.medilabo.patientservice.repository.PatientRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class PatientServiceIntegrationTest {
//
//    @Autowired
//    private PatientRepository patientRepository;
//
//    @Test
//    void testSaveAndRetrievePatient() {
//        Patient patient = Patient.builder()
//                .firstName("Integration")
//                .lastName("Test")
//                .birthdate(LocalDate.of(1985, 5, 15))
//                .gender("M")
//                .address("5 Integration St")
//                .phoneNumber("5556667777")
//                .build();
//
//        Patient savedPatient = patientRepository.save(patient);
//
//        assertNotNull(savedPatient.getId());
//
//        List<Patient> patients = patientRepository.findAll();
//
//        assertEquals(1, patients.size());
//        assertEquals("Integration", patients.getFirst().getFirstName());
//    }
//}
