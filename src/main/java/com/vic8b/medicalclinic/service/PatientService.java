package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.patient.CreatePatientCommand;
import com.vic8b.medicalclinic.command.patient.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.PageDto;
import com.vic8b.medicalclinic.dto.patient.PatientDto;
import com.vic8b.medicalclinic.exception.patient.PatientNotFoundException;
import com.vic8b.medicalclinic.exception.user.UserNotFoundException;
import com.vic8b.medicalclinic.mapper.PatientMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
import com.vic8b.medicalclinic.validator.PatientValidator;
import com.vic8b.medicalclinic.validator.UserValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;
    private final PatientValidator patientValidator;
    private final UserValidator userValidator;

    @Transactional
    public PatientDto addPatient(@NonNull CreatePatientCommand createPatientCommand) {
        User user = findUserByIdOrThrow(createPatientCommand.userId());
        patientValidator.validateUserDoesNotHavePatient(user);
        patientValidator.validateIdCardNoAvailability(createPatientCommand.idCardNo());
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
        userValidator.validateUpdatedUserAvailability(user, command.email(), command.username());
        patient.update(command.phoneNumber(), command.birthday());
        user.update(command.username(), command.email(), command.password(), command.firstName(), command.lastName());
        patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    public PatientDto getPatientByEmail(@NonNull String email) {
        return patientMapper.toDto(findPatientByEmailOrThrow(email));
    }

    public PageDto<PatientDto> getPatients(Pageable pageable) {
        return PageDto.from(patientRepository.findAll(pageable)
                .map(patientMapper::toDto));
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private Patient findPatientByEmailOrThrow(String email) {
        return patientRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
