package com.vic8b.medicalclinic.exception.appointment;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class InvalidStartTimeOfAppointmentException extends MedicalClinicException {
    public InvalidStartTimeOfAppointmentException() {
        super("Invalid start time of the appointment", HttpStatus.BAD_REQUEST);
    }
}
