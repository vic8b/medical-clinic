package com.vic8b.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class PatientAlreadyAssignedException extends MedicalClinicException {
    public PatientAlreadyAssignedException(Long id) {
        super("Patient with id " + id + " already has an assigned user", HttpStatus.CONFLICT);
    }
}
