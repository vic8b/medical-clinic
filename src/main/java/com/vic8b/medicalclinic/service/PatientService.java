package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.exception.PatientAlreadyExistsException;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.repository.PatientRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    @NonNull
    private final PatientRepository patientRepository;

    public Patient addPatient(@NonNull Patient patient) {
        return patientRepository.add(patient);
    }

    public void removePatientByEmail(@NonNull String email) {
        patientRepository.removeByEmail(email);
    }

    public Patient updatePatient(@NonNull String currentEmail, @NonNull UpdatePatientCommand command) {
        Patient patient = findPatientByEmailOrThrow(currentEmail);
        patientRepository.findByEmail(command.email())
                .filter(existingPatient -> !existingPatient.getId().equals(patient.getId()))
                .ifPresent(existingPatient -> {
                    throw new PatientAlreadyExistsException(command.email());
                });
        patient.update(
                command.email(),
                command.password(),
                command.idCardNo(),
                command.firstName(),
                command.lastName(),
                command.phoneNumber(),
                command.birthday()
        );
        return patient;
    }

    public Patient changePassword(@NonNull String email, @NonNull ChangePasswordCommand command) {
        Patient patient = findPatientByEmailOrThrow(email);
        patient.changePassword(command.password());
        return patient;
    }

    public Patient getPatientByEmail(@NonNull String email) {
        return findPatientByEmailOrThrow(email);
    }

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    private Patient findPatientByEmailOrThrow(@NonNull String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
