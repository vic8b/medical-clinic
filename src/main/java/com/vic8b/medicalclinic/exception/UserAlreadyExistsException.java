package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends MedicalClinicException {
    public UserAlreadyExistsException(Long id) {
        super("User with id " + id + " already exists", HttpStatus.CONFLICT);
    }

    public UserAlreadyExistsException(String message, HttpStatus status) {
        super(message, status);
    }

    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException(
                "User with email " + email + " already exists", HttpStatus.CONFLICT
        );
    }

    public static UserAlreadyExistsException byUsername(String username) {
        return new UserAlreadyExistsException(
                "User with username " + username + " already exists", HttpStatus.CONFLICT
        );
    }
}
