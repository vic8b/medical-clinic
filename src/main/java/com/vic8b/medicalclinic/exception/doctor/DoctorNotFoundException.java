package com.vic8b.medicalclinic.exception.doctor;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalClinicException {
    public DoctorNotFoundException(Long id) {
        super("Doctor with id " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
