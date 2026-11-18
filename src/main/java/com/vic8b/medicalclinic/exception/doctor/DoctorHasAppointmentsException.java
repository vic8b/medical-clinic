package com.vic8b.medicalclinic.exception.doctor;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class DoctorHasAppointmentsException extends MedicalClinicException {
    public DoctorHasAppointmentsException(Long id) {
        super("Doctor with id " + id + " has assigned appointments", HttpStatus.CONFLICT);
    }
}
