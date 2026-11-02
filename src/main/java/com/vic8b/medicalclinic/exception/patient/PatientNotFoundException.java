package com.vic8b.medicalclinic.exception.patient;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends MedicalClinicException {
    public PatientNotFoundException(Long id) {
        super("Patient with id " + id + " not found", HttpStatus.NOT_FOUND);
    }

    public PatientNotFoundException(String email) {
        super("Patient with email " + email + " not found", HttpStatus.NOT_FOUND);
    }
}
