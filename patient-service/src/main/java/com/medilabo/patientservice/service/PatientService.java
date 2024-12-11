package com.medilabo.patientservice.service;

import com.medilabo.patientservice.model.Patient;
import com.medilabo.patientservice.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    //Récupérer tous les patients

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    // Récupérer les informations personnelles d'un patient

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // Mettre à jour les informations personnelles d'un patient

    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setFirstName(updatedPatient.getFirstName());
            patient.setLastName(updatedPatient.getLastName());
            patient.setBirthdate(updatedPatient.getBirthdate());
            patient.setGender(updatedPatient.getGender());
            patient.setAddress(updatedPatient.getAddress());
            patient.setPhoneNumber(updatedPatient.getPhoneNumber());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new EntityNotFoundException("Patient not found with id " + id));
    }

    // Ajouter un nouveau patient

    public Patient addPatient(Patient newPatient) {
        return patientRepository.save(newPatient);
    }

    // Supprimer un patient

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}

