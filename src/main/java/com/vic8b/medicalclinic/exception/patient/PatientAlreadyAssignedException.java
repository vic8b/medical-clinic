package com.vic8b.medicalclinic.exception.patient;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class PatientAlreadyAssignedException extends MedicalClinicException {
    public PatientAlreadyAssignedException(Long id) {
        super("Patient with id " + id + " already has an assigned user", HttpStatus.CONFLICT);
    }
}
