package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class PatientAlreadyExistsException extends MedicalClinicException {
    public PatientAlreadyExistsException(UUID id) {
        super("Patient with id " + id + " already exists", HttpStatus.CONFLICT);
    }

    public PatientAlreadyExistsException(String message, HttpStatus status) {
        super(message, status);
    }

    public static PatientAlreadyExistsException byEmail(String email) {
        return new PatientAlreadyExistsException(
                "Patient with email " + email + " already exists", HttpStatus.CONFLICT
        );
    }

    public static PatientAlreadyExistsException byCardIdNo(String cardIdNo) {
        return new PatientAlreadyExistsException(
                "Patient with id card number " + cardIdNo + " already exists", HttpStatus.CONFLICT
        );
    }
}
