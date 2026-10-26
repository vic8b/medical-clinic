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
import org.assertj.core.api.ThrowableAssert;
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

        // when
        PatientDto patientDto = patientService.addPatient(command);

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
        Patient patient = Patient.builder().email(email).password("pass1234").idCardNo("1234").firstName("Wiktor")
                .lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build();
        when(patientRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(patient));

        // when
        patientService.removePatientByEmail(email);

        // then
        verify(patientRepository).delete(any(Patient.class));
    }

    @Test
    void updatePatient_WithCorrectCommand_ReturnedUpdatedPatientDto() {
        // given
        String searchedEmail = "test@gmail.com";
        Patient patient = Patient.builder().email(searchedEmail).password("pass1234").idCardNo("1234").firstName("Wiktor")
                .lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build();
        when(patientRepository.findByEmailIgnoreCase(searchedEmail)).thenReturn(Optional.of(patient));
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("test123@gmail.com", "pass1234", "Andrzej",
                "Kowalski", "500500500", LocalDate.of(1999, 1, 1));

        // when
        PatientDto result = patientService.updatePatient(searchedEmail, updatePatientCommand);

        // then
        assertAll(
                () -> assertThat(result.email()).isEqualTo("test123@gmail.com"),
                () -> assertThat(result.firstName()).isEqualTo("Andrzej"),
                () -> assertThat(result.lastName()).isEqualTo("Kowalski"),
                () -> assertThat(patient.getEmail()).isEqualTo("test123@gmail.com")
        );
    }

    @Test
    void changePassword_WithCorrectPasswordCommand_PasswordChanged() {
        // given
        String email = "test@gmail.com";
        Patient patient = Patient.builder().email(email).password("pass1234").idCardNo("1234").firstName("Wiktor")
                .lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build();
        when(patientRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(patient));
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("4321ssap");

        // when
        patientService.changePassword(email, changePasswordCommand);

        // then
        assertThat(patient.getPassword()).isEqualTo(changePasswordCommand.password());
    }

    @Test
    void getPatientByEmail_WithExistingEmail_ReturnedPatientDto() {
        // given
        String email = "test@gmail.com";
        Patient patient = Patient.builder().email(email).password("pass1234").idCardNo("1234").firstName("Wiktor")
                .lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build();
        when(patientRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(patient));

        // when
        PatientDto patientByEmail = patientService.getPatientByEmail(email);

        // then
        assertAll(
                () -> assertThat(patientByEmail.email()).isEqualTo(email),
                () -> assertThat(patientByEmail.firstName()).isEqualTo("Wiktor"),
                () -> assertThat(patientByEmail.lastName()).isEqualTo("Test"),
                () -> assertThat(patientByEmail.birthday()).isEqualTo(LocalDate.of(1999, 1, 1))
        );
        verify(patientRepository).findByEmailIgnoreCase(email);
    }

    @Test
    void getPatients_WithExistingPatients_ReturnedPatientDtos() {
        // given
        List<Patient> patients = List.of(
                Patient.builder().email("test@gmail.com").password("pass1234").idCardNo("1234").firstName("Wiktor")
                        .lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build(),
                Patient.builder().email("test2@gmail.com").password("pass12345").idCardNo("5555").firstName("Andrzej")
                        .lastName("Kowalski").phoneNumber("600600600").birthday(LocalDate.of(1988, 12, 12)).build()
        );
        when(patientRepository.findAll()).thenReturn(patients);

        // when
        List<PatientDto> result = patientService.getPatients();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).email()).isEqualTo("test@gmail.com"),
                () -> assertThat(result.get(0).firstName()).isEqualTo("Wiktor"),
                () -> assertThat(result.get(1).email()).isEqualTo("test2@gmail.com"),
                () -> assertThat(result.get(1).firstName()).isEqualTo("Andrzej")
        );
        verify(patientRepository).findAll();
    }

    @Test
    void addPatient_WithExistingIdCardNo_ThrownPatientAlreadyExistsException() {
        // given
        String existingIdCardNo = "ABC123456";
        CreatePatientCommand command = new CreatePatientCommand("test@gmail.com", "pass1234", existingIdCardNo, "Wiktor",
                "Test", "500500500", LocalDate.of(1999, 1, 1));
        when(patientRepository.existsByIdCardNo(existingIdCardNo)).thenReturn(true);

        // when + then
        assertThatThrownBy(() -> patientService.addPatient(command))
                .isInstanceOf(PatientAlreadyExistsException.class)
                .hasMessage("Patient with id card number ABC123456 already exists");
    }

    @Test
    void updatePatient_WithNotExistingCurrentEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("test123@gmail.com", "pass1234", "Andrzej",
                "Kowalski", "500500500", LocalDate.of(1999, 1, 1));
        when(patientRepository.findByEmailIgnoreCase(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.updatePatient(notExistingEmail, updatePatientCommand))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }

    @Test
    void updatePatient_WithEmailUsedByOtherPatient_ThrownPatientAlreadyExistsException() {
        // given
        Patient firstPatient = Patient.builder().email("test@gmail.com").password("pass1234")
                .idCardNo("1234").firstName("Wiktor")
                .lastName("Test").phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        Patient secondPatient = Patient.builder().email("existing@gmail.com").password("pass12345").idCardNo("1234").firstName("Andrzej").lastName("Test").phoneNumber("600600600").birthday(LocalDate.of(1999, 2, 2)).build();
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand(secondPatient.getEmail(), firstPatient.getPassword(),
                firstPatient.getFirstName(), firstPatient.getLastName(), firstPatient.getPhoneNumber(),
                firstPatient.getBirthday());
        when(patientRepository.findByEmailIgnoreCase(firstPatient.getEmail())).thenReturn(Optional.of(firstPatient));
        when(patientRepository.existsByEmailIgnoreCase(secondPatient.getEmail())).thenReturn(true);

        // when + then
        assertThatThrownBy(() -> patientService.updatePatient(firstPatient.getEmail(), updatePatientCommand))
                .isInstanceOf(PatientAlreadyExistsException.class)
                .hasMessage("Patient with email existing@gmail.com already exists");
    }

    @Test
    void updatePatient_withNullFields_ThrownNPE() {
        // given
        ThrowableAssert.ThrowingCallable emailIsNull = () -> new UpdatePatientCommand(null, "1234",
                "Wiktor", "Test", "500500500", LocalDate.of(1999, 1, 1));
        ThrowableAssert.ThrowingCallable passwordIsNull = () -> new UpdatePatientCommand("test@gmail.com", null,
                "Wiktor", "Test", "500500500", LocalDate.of(1999, 1, 1));
        ThrowableAssert.ThrowingCallable firstNameIsNull = () -> new UpdatePatientCommand("test@gmail.com", "1234",
                null, "Test", "500500500", LocalDate.of(1999, 1, 1));
        ThrowableAssert.ThrowingCallable lastNameIsNull = () -> new UpdatePatientCommand("test@gmail.com", "1234",
                "Wiktor", null, "500500500", LocalDate.of(1999, 1, 1));
        ThrowableAssert.ThrowingCallable phoneNumberIsNull = () -> new UpdatePatientCommand("test@gmail.com", "12345",
                "Wiktor", "Test", null, LocalDate.of(1999, 1, 1));
        ThrowableAssert.ThrowingCallable birthdayIsNull = () -> new UpdatePatientCommand("test@gmail.com", "12345",
                "Wiktor", "Test", "500500500", null);
        // when + then
        assertAll(
                () -> assertThatThrownBy(emailIsNull)
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("email is marked non-null but is null"),
                () -> assertThatThrownBy(passwordIsNull)
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("password is marked non-null but is null"),
                () -> assertThatThrownBy(firstNameIsNull)
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("firstName is marked non-null but is null"),
                () -> assertThatThrownBy(lastNameIsNull)
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("lastName is marked non-null but is null"),
                () -> assertThatThrownBy(phoneNumberIsNull)
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("phoneNumber is marked non-null but is null"),
                () -> assertThatThrownBy(birthdayIsNull)
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("birthday is marked non-null but is null")
        );
    }

    @Test
    void changePassword_WithNotExistingEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("test");
        when(patientRepository.findByEmailIgnoreCase(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.changePassword(notExistingEmail, changePasswordCommand))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }

    @Test
    void getPatientByEmail_WithNotExistingEmail_ThrownPatientNotFoundException() {
        // given
        String notExistingEmail = "test@gmail.com";
        when(patientRepository.findByEmailIgnoreCase(notExistingEmail)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> patientService.getPatientByEmail(notExistingEmail))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient with email test@gmail.com not found");
    }
}
