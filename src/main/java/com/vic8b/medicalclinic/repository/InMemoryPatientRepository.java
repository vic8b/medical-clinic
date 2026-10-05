package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.exception.PatientAlreadyExistsException;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.model.Patient;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPatientRepository implements PatientRepository {
    private final Map<UUID, Patient> patients = new ConcurrentHashMap<>();

    @Override
    public Patient add(@NonNull Patient patient) {
        if (patients.containsKey(patient.getId())) {
            throw new PatientAlreadyExistsException(patient.getId());
        }
        if (findByEmail(patient.getEmail()).isPresent()) {
            throw new PatientAlreadyExistsException(patient.getEmail());
        }
        patients.put(patient.getId(), patient);
        return patient;
    }

    @Override
    public void removeByEmail(@NonNull String email) {
        Patient patient = findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
        patients.remove(patient.getId());
    }

    @Override
    public Optional<Patient> findByEmail(@NonNull String email) {
        return patients.values().stream()
                .filter(patient -> email.equalsIgnoreCase(patient.getEmail()))
                .findFirst();
    }

    @Override
    public List<Patient> findAll() {
        return List.copyOf(patients.values());
    }
}
