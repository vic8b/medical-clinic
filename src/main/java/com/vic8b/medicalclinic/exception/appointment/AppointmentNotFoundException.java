package com.vic8b.medicalclinic.exception.appointment;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class AppointmentNotFoundException extends MedicalClinicException {
    public AppointmentNotFoundException(Long id) {
        super("Appointment with id " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
