package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Patient;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserEmailIgnoreCase(@NonNull String email);
    boolean existsByIdCardNo(@NonNull String idCardNo);
}
