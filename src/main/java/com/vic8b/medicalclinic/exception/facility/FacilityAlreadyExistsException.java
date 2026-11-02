package com.vic8b.medicalclinic.exception.facility;

import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class FacilityAlreadyExistsException extends MedicalClinicException {
    public FacilityAlreadyExistsException(String name) {
        super("Facility with name " + name + " already exists", HttpStatus.CONFLICT);
    }
}
