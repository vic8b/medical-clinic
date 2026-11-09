package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UserHasAssignedPatientException extends MedicalClinicException {
    public UserHasAssignedPatientException(Long id) {
        super("User with id " + id + " has an assigned patient", HttpStatus.CONFLICT);
    }
}
