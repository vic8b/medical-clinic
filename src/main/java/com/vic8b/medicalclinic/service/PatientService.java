package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.command.CreatePatientProfileCommand;
import com.vic8b.medicalclinic.command.CreateUserCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.PatientDto;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.exception.UserNotFoundException;
import com.vic8b.medicalclinic.mapper.PatientMapper;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
import com.vic8b.medicalclinic.validator.PatientValidator;
import com.vic8b.medicalclinic.validator.UserValidator;
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
    private final PatientValidator patientValidator;
    private final UserValidator userValidator;

    @Transactional
    public PatientDto addPatient(@NonNull CreatePatientCommand createPatientCommand) {
        patientValidator.validateIdCardNoAvailability(createPatientCommand.idCardNo());
        User user = createUser(createPatientCommand.userCommand());
        Patient patient = patientMapper.toEntity(createPatientCommand);
        patient.assignUser(user);
        patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    @Transactional
    public PatientDto createPatientProfile(@NonNull Long userId, @NonNull CreatePatientProfileCommand command) {
        patientValidator.validateIdCardNoAvailability(command.idCardNo());
        User user = findUserByIdOrThrow(userId);
        patientValidator.validateUserDoesNotHavePatient(user);
        Patient patient = patientMapper.toEntity(command);
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
        userValidator.validateUpdatedUserAvailability(user, command.email(), command.username());
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

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private User createUser(CreateUserCommand createUserCommand) {
        userValidator.validateUsernameAvailability(createUserCommand.username());
        userValidator.validateEmailAvailability(createUserCommand.email());
        return userRepository.save(userMapper.toEntity(createUserCommand));
    }

    private Patient findPatientByEmailOrThrow(String email) {
        return patientRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
