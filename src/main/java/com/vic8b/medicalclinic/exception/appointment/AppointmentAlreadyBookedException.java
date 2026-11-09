package com.vic8b.medicalclinic.exception.appointment;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class AppointmentAlreadyBookedException extends MedicalClinicException {
    public AppointmentAlreadyBookedException() {
        super("Appointment is already booked", HttpStatus.CONFLICT);
    }
}
