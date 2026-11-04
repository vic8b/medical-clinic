package com.vic8b.medicalclinic.exception.appointment;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class PastAppointmentException extends MedicalClinicException {
    public PastAppointmentException(Long appointmentId) {
        super("Appointment with id " + appointmentId + " start date has already passed", HttpStatus.CONFLICT);
    }
}
