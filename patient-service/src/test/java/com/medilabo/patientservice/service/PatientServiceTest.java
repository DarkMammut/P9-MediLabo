package com.medilabo.patientservice.service;

import com.medilabo.patientservice.model.Patient;
import com.medilabo.patientservice.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patient = Patient.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Patient")
                .birthdate(LocalDate.of(1966, 12, 31))
                .gender("F")
                .address("1 Brookside St")
                .phoneNumber("1002223333")
                .build();
    }

    @Test
    void testGetAllPatients() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));

        List<Patient> patients = patientService.getAllPatients();

        assertNotNull(patients);
        assertEquals(1, patients.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientService.getPatientById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getFirstName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void testAddPatient() {
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient savedPatient = patientService.addPatient(patient);

        assertNotNull(savedPatient);
        assertEquals("Test", savedPatient.getFirstName());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void testUpdatePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient updatedPatient = patientService.updatePatient(1L, patient);

        assertNotNull(updatedPatient);
        assertEquals("Test", updatedPatient.getFirstName());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testDeletePatient() {
        doNothing().when(patientRepository).deleteById(1L);

        assertDoesNotThrow(() -> patientService.deletePatient(1L));

        verify(patientRepository, times(1)).deleteById(1L);
    }
}
