package com.vic8b.medicalclinic.exception.user;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class UserHasAssignedPatientException extends MedicalClinicException {
    public UserHasAssignedPatientException(Long id) {
        super("User with id " + id + " has an assigned patient", HttpStatus.CONFLICT);
    }
}
