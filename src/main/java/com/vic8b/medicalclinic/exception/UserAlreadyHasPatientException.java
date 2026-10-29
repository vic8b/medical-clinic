package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyHasPatientException extends MedicalClinicException {
    public UserAlreadyHasPatientException(Long id) {
        super("User with id " + id + " already has an assigned patient", HttpStatus.CONFLICT);
    }
}
