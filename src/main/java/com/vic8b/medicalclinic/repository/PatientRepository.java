package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Patient;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByEmailIgnoreCase(@NonNull String email);
    boolean existsByEmailIgnoreCase(@NonNull String email);
}
