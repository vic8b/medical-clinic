package com.vic8b.medicalclinic.exception.user;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class UserAlreadyHasDoctorException extends MedicalClinicException {
    public UserAlreadyHasDoctorException(Long id) {
        super("User with id " + id + " already has an assigned doctor", HttpStatus.CONFLICT);
    }
}
