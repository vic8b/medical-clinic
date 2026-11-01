package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.command.CreateUserCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.PatientDto;
import com.vic8b.medicalclinic.exception.*;
import com.vic8b.medicalclinic.mapper.PatientMapper;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;
    private final UserMapper userMapper;

    @Transactional
    public PatientDto addPatient(@NonNull CreatePatientCommand createPatientCommand) {
        validateIdCardNoAvailability(createPatientCommand);
        User user = findOrCreateUser(createPatientCommand.userCommand());
        validateUserDoesNotHavePatient(user);
        Patient patient = patientMapper.toEntity(createPatientCommand);
        patient.assignUser(user);
        patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    @Transactional
    public void removePatientByEmail(@NonNull String email) {
        Patient patient = findPatientByEmailOrThrow(email);
        patient.getUser().clearPatient();
        patientRepository.delete(patient);
    }

    @Transactional
    public PatientDto updatePatient(@NonNull String currentEmail, @NonNull UpdatePatientCommand command) {
        Patient patient = findPatientByEmailOrThrow(currentEmail);
        User user = patient.getUser();
        validateUpdatedUserAvailability(command, user);
        patient.update(command.phoneNumber(), command.birthday());
        user.update(command.username(), command.email(), command.password(), command.firstName(), command.lastName());
        patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    public PatientDto getPatientByEmail(@NonNull String email) {
        return patientMapper.toDto(findPatientByEmailOrThrow(email));
    }

    public List<PatientDto> getPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDto)
                .toList();
    }

    private void validateUserDoesNotHavePatient(User user) {
        if (user.getPatient() != null) {
            throw new UserAlreadyHasPatientException(user.getId());
        }
    }

    private void validateIdCardNoAvailability(CreatePatientCommand command) {
        if (patientRepository.existsByIdCardNo(command.idCardNo())) {
            throw PatientAlreadyExistsException.byCardIdNo(command.idCardNo());
        }
    }

    private User findOrCreateUser(CreateUserCommand createUserCommand) {
        return userRepository.findByEmailIgnoreCase(createUserCommand.email())
                .orElseGet(() -> createUser(createUserCommand));
    }

    private User createUser(CreateUserCommand createUserCommand) {
        if (userRepository.existsByUsername(createUserCommand.username())) {
            throw UserAlreadyExistsException.byUsername(createUserCommand.username());
        }
        if (userRepository.existsByEmailIgnoreCase(createUserCommand.email())) {
            throw UserAlreadyExistsException.byEmail(createUserCommand.email());
        }
        return userRepository.save(userMapper.toEntity(createUserCommand));
    }

    private Patient findPatientByEmailOrThrow(String email) {
        return patientRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    private void validateUpdatedUserAvailability(UpdatePatientCommand command, User user) {
        if (!user.getEmail().equalsIgnoreCase(command.email()) && userRepository.existsByEmailIgnoreCase(command.email())) {
            throw UserAlreadyExistsException.byEmail(command.email());
        }
        if (!user.getUsername().equalsIgnoreCase(command.username()) && userRepository.existsByUsername(command.username())) {
            throw UserAlreadyExistsException.byUsername(command.username());
        }
    }
}
