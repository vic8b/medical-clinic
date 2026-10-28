package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends MedicalClinicException {
    public UserAlreadyExistsException(String username) {
        super("User with username " + username + " already exists", HttpStatus.CONFLICT);
    }
}
