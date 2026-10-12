package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class PatientAlreadyExistsException extends MedicalClinicException {
    public PatientAlreadyExistsException(UUID id) {
        super("Patient with id " + id + " already exists", HttpStatus.CONFLICT);
    }

    public PatientAlreadyExistsException(String email) {
        super("Patient with email " + email + " already exists", HttpStatus.CONFLICT);
    }
}
