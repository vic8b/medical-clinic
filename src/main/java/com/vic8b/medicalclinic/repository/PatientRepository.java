package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Patient;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    Patient add(@NonNull Patient patient);

    void removeByEmail(@NonNull String email);

    Optional<Patient> findByEmail(@NonNull String email);

    List<Patient> findAll();
}
