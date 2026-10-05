package com.vic8b.medicalclinic.exception;

import java.util.UUID;

public class PatientAlreadyExistsException extends RuntimeException {
    public PatientAlreadyExistsException(UUID id) {
        super("Patient with id " + id + " already exists");
    }

    public PatientAlreadyExistsException(String email) {
        super("Patient with email " + email + " already exists");
    }
}
