package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends MedicalClinicException {
    public InvalidDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
