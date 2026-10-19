package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.PatientDto;
import com.vic8b.medicalclinic.exception.PatientAlreadyExistsException;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.mapper.PatientMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    PatientRepository patientRepository;
    PatientMapper mapper;

    PatientService patientService;

    @BeforeEach
    void setup() {
        patientRepository = Mockito.mock(PatientRepository.class);
        mapper = Mappers.getMapper(PatientMapper.class);
        patientService = new PatientService(patientRepository, mapper);
    }

    @Test
    void addPatient_WithCorrectCommand_ReturnedPatientDto() {
        // given
        CreatePatientCommand command = new CreatePatientCommand("test@gmail.com", "pass1234", "1234", "Wiktor",
                "Test", "500500500", LocalDate.of(1999, 1, 1));
        Patient patient = mapper.toEntity(command);

        // when
        PatientDto patientDto = patientService.addPatient(command);

        // then
        assertThat(patientDto).isEqualTo(mapper.toDto(patient));
    }

    @Test
    void removePatientByEmail_WithCorrectEmail_RepositoryRemoveInvoked() {
        // given
        String email = "test@gmail.com";

        // when
        patientService.removePatientByEmail(email);

        // then
        verify(patientRepository).removeByEmail(email);
    }

    @Test
    void updatePatient_WithCorrectCommand_ReturnedUpdatedPatientDto() {
        // given
        CreatePatientCommand createPatientCommand = new CreatePatientCommand("test@gmail.com", "pass1234", "1234", "Wiktor",
                "Test", "500500500", LocalDate.of(1999, 1, 1));
        Patient patient = mapper.toEntity(createPatientCommand);
        when(patientRepository.findByEmail(createPatientCommand.email())).thenReturn(Optional.of(patient));
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("test123@gmail.com", "pass1234", "1234", "Andrzej",
                "Kowalski", "500500500", LocalDate.of(1999, 1, 1));
        when(patientRepository.findByEmail(updatePatientCommand.email())).thenReturn(Optional.empty());

        // when
        PatientDto result = patientService.updatePatient(createPatientCommand.email(), updatePatientCommand);

        // then
        assertThat(result.email()).isEqualTo(updatePatientCommand.email());
        assertThat(result.firstName()).isEqualTo(updatePatientCommand.firstName());
        assertThat(result.lastName()).isEqualTo(updatePatientCommand.lastName());
        assertThat(patient.getEmail()).isEqualTo(updatePatientCommand.email());
    }

    @Test
    void changePassword_WithCorrectPasswordCommand_PasswordChanged() {
        // given
        CreatePatientCommand createPatientCommand = new CreatePatientCommand("test@gmail.com", "pass1234", "1234", "Wiktor",
                "Test", "500500500", LocalDate.of(1999, 1, 1));
        Patient patient = mapper.toEntity(createPatientCommand);
        when(patientRepository.findByEmail(createPatientCommand.email())).thenReturn(Optional.of(patient));
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("4321ssap");

        // when
        patientService.changePassword(createPatientCommand.email(), changePasswordCommand);

        // then
        assertThat(patient.getPassword()).isEqualTo(changePasswordCommand.password());
    }

    @Test
    void getPatientByEmail_WithExistingEmail_ReturnedPatientDto() {
        // given
        CreatePatientCommand createPatientCommand = new CreatePatientCommand("test@gmail.com", "pass1234", "1234", "Wiktor",
                "Test", "500500500", LocalDate.of(1999, 1, 1));
        Patient patient = mapper.toEntity(createPatientCommand);
        when(patientRepository.findByEmail(createPatientCommand.email())).thenReturn(Optional.of(patient));

        // when
        PatientDto patientByEmail = patientService.getPatientByEmail(createPatientCommand.email());

        // then
        assertThat(patientByEmail).isEqualTo(mapper.toDto(patient));
    }

    @Test
    void getPatients_WithExistingPatients_ReturnedPatientsDtos() {
        // given
        CreatePatientCommand createPatientCommand = new CreatePatientCommand("test@gmail.com", "pass1234", "1234", "Wiktor",
                "Test", "500500500", LocalDate.of(1999, 1, 1));
        Patient firstPatient = mapper.toEntity(createPatientCommand);
        CreatePatientCommand secondCreatePatientCommand = new CreatePatientCommand("test2@gmail.com", "pass12345", "1234", "Andrzej",
                "Test", "500500500", LocalDate.of(1999, 2, 2));
        Patient secondPatient = mapper.toEntity(secondCreatePatientCommand);
        when(patientRepository.findAll()).thenReturn(List.of(firstPatient, secondPatient));

        // when
        List<PatientDto> result = patientService.getPatients();

        // then
        assertThat(result).containsExactly(mapper.toDto(firstPatient), mapper.toDto(secondPatient));
    }

    @Test
    void updatePatient_WithNotExistingCurrentEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("test123@gmail.com", "pass1234", "1234", "Andrzej",
                "Kowalski", "500500500", LocalDate.of(1999, 1, 1));
        when(patientRepository.findByEmail(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.updatePatient(notExistingEmail, updatePatientCommand))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }

    @Test
    void updatePatient_WithEmailUsedByOtherPatient_ThrownPatientAlreadyExistsException() {
        // given
        CreatePatientCommand createPatientCommand = new CreatePatientCommand("test@gmail.com", "pass1234", "1234", "Wiktor",
                "Test", "500500500", LocalDate.of(1999, 1, 1));
        Patient firstPatient = mapper.toEntity(createPatientCommand);
        CreatePatientCommand secondCreatePatientCommand = new CreatePatientCommand("existing@gmail.com", "pass12345", "1234", "Andrzej",
                "Test", "500500500", LocalDate.of(1999, 2, 2));
        Patient secondPatient = mapper.toEntity(secondCreatePatientCommand);
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand(secondPatient.getEmail(), firstPatient.getPassword(), firstPatient.getIdCardNo(),
                firstPatient.getFirstName(), firstPatient.getLastName(), firstPatient.getPhoneNumber(),
                firstPatient.getBirthday());
        when(patientRepository.findByEmail(firstPatient.getEmail())).thenReturn(Optional.of(firstPatient));
        when(patientRepository.findByEmail(secondPatient.getEmail())).thenReturn(Optional.of(secondPatient));

        // when + then
        assertThatThrownBy(() -> patientService.updatePatient(firstPatient.getEmail(), updatePatientCommand))
                .isInstanceOf(PatientAlreadyExistsException.class)
                .hasMessage("Patient with email existing@gmail.com already exists");
    }

    @Test
    void changePassword_WithNotExistingEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("test");
        when(patientRepository.findByEmail(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.changePassword(notExistingEmail, changePasswordCommand))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }

    @Test
    void getPatientByEmail_WithNotExistingEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        when(patientRepository.findByEmail(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.getPatientByEmail(notExistingEmail))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }
}
