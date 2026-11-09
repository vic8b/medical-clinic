package com.vic8b.medicalclinic.validator;

import com.vic8b.medicalclinic.exception.PatientAlreadyExistsException;
import com.vic8b.medicalclinic.exception.UserAlreadyHasPatientException;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientValidator {
    private final PatientRepository patientRepository;

    public void validateUserDoesNotHavePatient(User user) {
        if (user.getPatient() != null) {
            throw new UserAlreadyHasPatientException(user.getId());
        }
    }

    public void validateIdCardNoAvailability(String idCardNo) {
        if (patientRepository.existsByIdCardNo(idCardNo)) {
            throw PatientAlreadyExistsException.byCardIdNo(idCardNo);
        }
    }
}
