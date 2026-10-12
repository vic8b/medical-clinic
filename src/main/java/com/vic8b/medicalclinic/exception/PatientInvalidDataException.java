package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class PatientInvalidDataException extends MedicalClinicException {
    public PatientInvalidDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
