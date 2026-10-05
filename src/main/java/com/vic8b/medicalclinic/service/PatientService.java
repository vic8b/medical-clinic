package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.UpdatePatientCommand;
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

    public Patient updatePatient(@NonNull UpdatePatientCommand command) {
        Patient patient = patientRepository.findByEmail(command.currentEmail())
                .orElseThrow(() -> new PatientNotFoundException(command.currentEmail()));

        patient.update(
                command.newEmail(),
                command.password(),
                command.idCardNo(),
                command.firstName(),
                command.lastName(),
                command.phoneNumber(),
                command.birthday()
        );

        return patient;
    }

    public Patient getPatientByEmail(@NonNull String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }
}
