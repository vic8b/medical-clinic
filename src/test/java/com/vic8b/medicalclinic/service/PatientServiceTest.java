package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.command.CreateUserCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.PatientDto;
import com.vic8b.medicalclinic.exception.PatientAlreadyExistsException;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.mapper.PatientMapper;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    PatientRepository patientRepository;
    UserRepository userRepository;
    PatientMapper patientMapper;
    UserMapper userMapper;
    PatientService patientService;

    @BeforeEach
    void setup() {
        patientRepository = Mockito.mock(PatientRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        patientMapper = Mappers.getMapper(PatientMapper.class);
        userMapper = Mappers.getMapper(UserMapper.class);
        patientService = new PatientService(patientRepository, userRepository, patientMapper, userMapper);
    }

    @Test
    void addPatient_WithCorrectCommand_ReturnedPatientDto() {
        // given
        CreateUserCommand createUserCommand = new CreateUserCommand("username", "test@gmail.com", "pass1234", "Wiktor", "Test");
        CreatePatientCommand createPatientCommand = new CreatePatientCommand(createUserCommand, "1234", "500500500", LocalDate.of(1999, 1, 1));
        User user = User.builder().username("username").email("test@gmail.com").password("pass1234").firstName("Wiktor")
                .lastName("Test").build();
        when(userRepository.findByEmailIgnoreCase("test@gmail.com")).thenReturn(Optional.of(user));

        // when
        PatientDto patientDto = patientService.addPatient(createPatientCommand);

        // then
        assertAll(
                () -> assertThat(patientDto.email()).isEqualTo("test@gmail.com"),
                () -> assertThat(patientDto.firstName()).isEqualTo("Wiktor"),
                () -> assertThat(patientDto.lastName()).isEqualTo("Test"),
                () -> assertThat(patientDto.phoneNumber()).isEqualTo("500500500"),
                () -> assertThat(patientDto.birthday()).isEqualTo(LocalDate.of(1999, 1, 1))
        );
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void removePatientByEmail_WithCorrectEmail_RepositoryRemoveInvoked() {
        // given
        String email = "test@gmail.com";
        Patient patient = Patient.builder().idCardNo("1234").phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        User user = User.builder().username("username").email("test@gmail.com").password("pass1234").firstName("Wiktor")
                .lastName("Test").build();
        user.assignPatient(patient);
        when(patientRepository.findByUserEmailIgnoreCase(email)).thenReturn(Optional.of(patient));

        // when
        patientService.removePatientByEmail(email);

        // then
        verify(patientRepository).delete(any(Patient.class));
        verify(userRepository).save(user);
        verify(patientRepository).delete(patient);
    }

    @Test
    void updatePatient_WithCorrectCommand_ReturnedUpdatedPatientDto() {
        // given
        String searchedEmail = "test@gmail.com";
        Patient patient = Patient.builder().idCardNo("1234").phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        when(patientRepository.findByUserEmailIgnoreCase(searchedEmail)).thenReturn(Optional.of(patient));
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("500500500",
                LocalDate.of(1999, 1, 1));

        // when
        PatientDto result = patientService.updatePatient(searchedEmail, updatePatientCommand);

        // then
        assertAll(
                () -> assertThat(result.phoneNumber()).isEqualTo("500500500"),
                () -> assertThat(result.birthday()).isEqualTo(LocalDate.of(1999, 1, 1))
        );
    }

    @Test
    void getPatientByEmail_WithExistingEmail_ReturnedPatientDto() {
        // given
        String email = "test@gmail.com";
        Patient patient = Patient.builder().idCardNo("1234").phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        when(patientRepository.findByUserEmailIgnoreCase(email)).thenReturn(Optional.of(patient));

        // when
        PatientDto patientByEmail = patientService.getPatientByEmail(email);

        // then
        assertAll(
                () -> assertThat(patientByEmail.phoneNumber()).isEqualTo("500500500"),
                () -> assertThat(patientByEmail.birthday()).isEqualTo(LocalDate.of(1999, 1, 1))
        );
        verify(patientRepository).findByUserEmailIgnoreCase(email);
    }

    @Test
    void getPatients_WithExistingPatients_ReturnedPatientDtos() {
        // given
        List<Patient> patients = List.of(
                Patient.builder().idCardNo("1234").phoneNumber("500500500")
                        .birthday(LocalDate.of(1999, 1, 1)).build(),
                Patient.builder().idCardNo("5555").phoneNumber("600600600")
                        .birthday(LocalDate.of(1990, 1, 1)).build()
        );
        when(patientRepository.findAll()).thenReturn(patients);

        // when
        List<PatientDto> result = patientService.getPatients();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).phoneNumber()).isEqualTo("500500500"),
                () -> assertThat(result.get(0).birthday()).isEqualTo(LocalDate.of(1999, 1, 1)),
                () -> assertThat(result.get(1).phoneNumber()).isEqualTo("600600600"),
                () -> assertThat(result.get(1).birthday()).isEqualTo(LocalDate.of(1990, 1, 1))
        );
        verify(patientRepository).findAll();
    }

    @Test
    void addPatient_WithExistingIdCardNo_ThrownPatientAlreadyExistsException() {
        // given
        String existingIdCardNo = "ABC123456";
        CreateUserCommand createUserCommand = new CreateUserCommand("username", "test@gmail.com", "pass1234", "Wiktor", "Test");
        CreatePatientCommand createPatientCommand = new CreatePatientCommand(createUserCommand, existingIdCardNo, "500500500", LocalDate.of(1999, 1, 1));
        when(patientRepository.existsByIdCardNo(existingIdCardNo)).thenReturn(true);

        // when + then
        assertThatThrownBy(() -> patientService.addPatient(createPatientCommand))
                .isInstanceOf(PatientAlreadyExistsException.class)
                .hasMessage("Patient with id card number ABC123456 already exists");
    }

    @Test
    void updatePatient_WithNotExistingCurrentEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        Patient patient = Patient.builder().idCardNo("1234").phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        when(patientRepository.findByUserEmailIgnoreCase(notExistingEmail)).thenReturn(Optional.of(patient));
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("500500500",
                LocalDate.of(1999, 1, 1));
        when(patientRepository.findByUserEmailIgnoreCase(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.updatePatient(notExistingEmail, updatePatientCommand))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }

    @Test
    void getPatientByEmail_WithNotExistingEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        when(patientRepository.findByUserEmailIgnoreCase(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.getPatientByEmail(notExistingEmail))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }
}
