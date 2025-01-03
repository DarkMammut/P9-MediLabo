package com.medilabo.patientservice.service;

import com.medilabo.patientservice.model.Patient;
import com.medilabo.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    private PatientRepository patientRepository;
    private PatientService patientService;
    private List<Patient> mockPatients;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        patientService = new PatientService(patientRepository);

        // Initialize mock patients
        mockPatients = new ArrayList<>();
        mockPatients.add(Patient.builder()
                .id(1L)
                .gender("M")
                .firstName("John")
                .lastName("Doe")
                .birthdate(LocalDate.of(2000, 1, 1))
                .address("Test Address")
                .phoneNumber("111-111-111")
                .build());
        mockPatients.add(Patient.builder()
                .id(2L)
                .gender("F")
                .firstName("Jane")
                .lastName("Doe")
                .birthdate(LocalDate.of(1998, 12, 31))
                .address("Test Address")
                .phoneNumber("222-222-222")
                .build());
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatients() {
        when(patientRepository.findAll()).thenReturn(mockPatients);

        List<Patient> patients = patientService.getAllPatients();

        assertEquals(2, patients.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_ShouldReturnPatient_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatients.get(0)));

        Optional<Patient> patient = patientService.getPatientById(1L);

        assertTrue(patient.isPresent());
        assertEquals("John", patient.get().getFirstName());
        assertEquals("Doe", patient.get().getLastName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientById_ShouldReturnEmpty_WhenPatientDoesNotExist() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Patient> patient = patientService.getPatientById(99L);

        assertFalse(patient.isPresent());
        verify(patientRepository, times(1)).findById(99L);
    }

    @Test
    void createPatient_ShouldSaveAndReturnPatient() {
        Patient newPatient = Patient.builder()
                .id(3L)
                .gender("M")
                .firstName("Mark")
                .lastName("Smith")
                .birthdate(LocalDate.of(1995, 5, 15))
                .address("New Address")
                .phoneNumber("333-333-333")
                .build();

        when(patientRepository.save(newPatient)).thenReturn(newPatient);

        Patient savedPatient = patientService.addPatient(newPatient);

        assertEquals(3L, savedPatient.getId());
        assertEquals("Mark", savedPatient.getFirstName());
        verify(patientRepository, times(1)).save(newPatient);
    }

    @Test
    void updatePatient_ShouldUpdateAndReturnUpdatedPatient_WhenPatientExists() {
        Patient existingPatient = mockPatients.get(0);
        Patient updatedPatient = Patient.builder()
                .id(1L)
                .gender("M")
                .firstName("Updated")
                .lastName("Patient")
                .birthdate(existingPatient.getBirthdate())
                .address("Updated Address")
                .phoneNumber("444-444-444")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(updatedPatient);

        Patient result = patientService.updatePatient(1L, updatedPatient);

        assertEquals("Updated", result.getFirstName());
        assertEquals("Updated Address", result.getAddress());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(existingPatient);
    }

    @Test
    void updatePatient_ShouldThrowException_WhenPatientDoesNotExist() {
        Patient updatedPatient = Patient.builder()
                .id(1L)
                .gender("M")
                .firstName("Updated")
                .lastName("Patient")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            patientService.updatePatient(1L, updatedPatient);
        });

        assertEquals("Patient not found with id 1", exception.getMessage());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void deletePatientById_ShouldDeletePatient_WhenPatientExists() {
        doNothing().when(patientRepository).deleteById(1L);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).deleteById(1L);
    }
}