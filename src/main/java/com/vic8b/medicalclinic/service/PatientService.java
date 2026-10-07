package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.exception.PatientAlreadyExistsException;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.mapper.PatientMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.dto.PatientDto;
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
    @NonNull
    private final PatientMapper patientMapper;

    public PatientDto addPatient(@NonNull CreatePatientCommand command) {
        Patient patient = patientMapper.toPatient(command);
        patientRepository.add(patient);
        return patientMapper.toDto(patient);
    }

    public void removePatientByEmail(@NonNull String email) {
        patientRepository.removeByEmail(email);
    }

    public PatientDto updatePatient(@NonNull String currentEmail, @NonNull UpdatePatientCommand command) {
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
        return patientMapper.toDto(patient);
    }

    public PatientDto changePassword(@NonNull String email, @NonNull ChangePasswordCommand command) {
        Patient patient = findPatientByEmailOrThrow(email);
        patient.changePassword(command.password());
        return patientMapper.toDto(patient);
    }

    public PatientDto getPatientByEmail(@NonNull String email) {
        return patientMapper.toDto(findPatientByEmailOrThrow(email));
    }

    public List<PatientDto> getPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .toList();
    }

    private Patient findPatientByEmailOrThrow(@NonNull String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
