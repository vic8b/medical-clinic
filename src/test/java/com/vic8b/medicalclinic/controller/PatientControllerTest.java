package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.PatientDto;
import com.vic8b.medicalclinic.exception.PatientAlreadyExistsException;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PatientControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    PatientService patientService;

    @Test
    void getPatients_WithExistingPatients_AnsweredWithExistingPatients() throws Exception {
        // given
        PatientDto patientDto = new PatientDto("test@gmail.com", "Wiktor", "Test",
                "500500500", LocalDate.of(1999, 1, 1));
        PatientDto secondPatientDto = new PatientDto("second@gmail.com", "Andrew", "Kowalski",
                "600600600", LocalDate.of(1988, 12, 12));
        when(patientService.getPatients()).thenReturn(List.of(patientDto, secondPatientDto));

        // when + then
        mvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$[0].firstName").value("Wiktor"))
                .andExpect(jsonPath("$[0].lastName").value("Test"))
                .andExpect(jsonPath("$[0].phoneNumber").value("500500500"))
                .andExpect(jsonPath("$[0].birthday").value("1999-01-01"))
                .andExpect(jsonPath("$[1].email").value("second@gmail.com"))
                .andExpect(jsonPath("$[1].firstName").value("Andrew"))
                .andExpect(jsonPath("$[1].lastName").value("Kowalski"))
                .andExpect(jsonPath("$[1].phoneNumber").value("600600600"))
                .andExpect(jsonPath("$[1].birthday").value("1988-12-12"));
    }

    @Test
    void getPatientByEmail_WithExistingPatientEmail_AnsweredWithExistingPatient() throws Exception {
        // given
        PatientDto patientDto = new PatientDto("test@gmail.com", "Wiktor", "Test",
                "500500500", LocalDate.of(1999, 1, 1));
        when(patientService.getPatientByEmail(patientDto.email()))
                .thenReturn(patientDto);

        // when + then
        mvc.perform(get("/patients/{email}", patientDto.email()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Wiktor"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andExpect(jsonPath("$.phoneNumber").value("500500500"))
                .andExpect(jsonPath("$.birthday").value("1999-01-01"));
    }

    @Test
    void addPatient_WithCorrectPatientCommand_AnsweredWithAddedPatient() throws Exception {
        // given
        CreatePatientCommand command = new CreatePatientCommand("test@gmail.com", "1234",
                "1234", "Wiktor", "Test", "500500500",
                LocalDate.of(1999, 1, 1));
        PatientDto patientDto = new PatientDto(command.email(), command.firstName(), command.lastName(), command.phoneNumber(), command.birthday());
        when(patientService.addPatient(command)).thenReturn(patientDto);

        // when + then
        mvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Wiktor"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andExpect(jsonPath("$.phoneNumber").value("500500500"))
                .andExpect(jsonPath("$.birthday").value("1999-01-01"));
    }

    @Test
    void updatePatient_WithCorrectEmailAndCorrectUpdatePatientCommand_AnsweredWithUpdatedPatient() throws Exception {
        // given
        String email = "test@gmail.com";
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("updatedTest@gmail.com",
                "newPassword", "Andrew", "Kowalski", "600600600",
                LocalDate.of(1900, 12, 12));
        PatientDto updatedPatientDto = new PatientDto(updatePatientCommand.email(), updatePatientCommand.firstName(),
                updatePatientCommand.lastName(), updatePatientCommand.phoneNumber(), updatePatientCommand.birthday());
        when(patientService.updatePatient(email, updatePatientCommand)).thenReturn(updatedPatientDto);

        // when + then
        mvc.perform(put("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePatientCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updatedTest@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Andrew"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("600600600"))
                .andExpect(jsonPath("$.birthday").value("1900-12-12"));
    }

    @Test
    void changePassword_WithCorrectEmailAndCorrectChangePasswordCommand_AnsweredWithUpdatedPatient() throws Exception {
        // given
        String email = "test@gmail.com";
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("newPassword");
        PatientDto patientDto = new PatientDto("test@gmail.com", "Wiktor", "Test",
                "500500500", LocalDate.of(1999, 1, 1));
        when(patientService.changePassword(email, changePasswordCommand)).thenReturn(patientDto);

        // when + then
        mvc.perform(patch("/patients/{email}/password", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Wiktor"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andExpect(jsonPath("$.phoneNumber").value("500500500"))
                .andExpect(jsonPath("$.birthday").value("1999-01-01"));
        ArgumentCaptor<ChangePasswordCommand> captor =
                ArgumentCaptor.forClass(ChangePasswordCommand.class);
        verify(patientService).changePassword(eq(email), captor.capture());
        assertThat(captor.getValue().password()).isEqualTo(changePasswordCommand.password());
    }

    @Test
    void removePatient_WithCorrectEmail_AnsweredWithNoContentStatus() throws Exception {
        // given
        String email = "test@gmail.com";
        // when + then
        mvc.perform(delete("/patients/{email}", email))
                .andExpect(status().isNoContent());
        verify(patientService).removePatientByEmail(email);
    }

    @Test
    void getPatientByEmail_WithNotExistingPatientEmail_AnsweredWithNotFoundStatus() throws Exception {
        // given
        String email = "notExistingEmail@gmail.com";
        when(patientService.getPatientByEmail(email)).thenThrow(new PatientNotFoundException(email));

        // when + then
        mvc.perform(get("/patients/{email}", email))
                .andExpect(status().isNotFound());
    }

    @Test
    void addPatient_WithTakenEmail_AnsweredWithConflictStatus() throws Exception {
        // given
        String takenEmail = "takenEmail@gmail.com";
        CreatePatientCommand command = new CreatePatientCommand(takenEmail, "1234",
                "1234", "Wiktor", "Test", "500500500",
                LocalDate.of(1999, 1, 1));
        when(patientService.addPatient(command)).thenThrow(PatientAlreadyExistsException.byEmail(takenEmail));

        // when + then
        mvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(command)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Patient with email takenEmail@gmail.com already exists"))
                .andExpect(jsonPath("$.httpCode").value(409))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updatePatient_WithNotExistingPatientEmail_AnsweredWithNotFoundStatus() throws Exception {
        // given
        String email = "notExistingEmail@gmail.com";
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("updatedTest@gmail.com",
                "newPassword", "Andrew", "Kowalski", "600600600",
                LocalDate.of(1900, 12, 12));
        when(patientService.updatePatient(email, updatePatientCommand)).thenThrow(new PatientNotFoundException(email));

        // when + then
        mvc.perform(put("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePatientCommand)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient with email notExistingEmail@gmail.com not found"))
                .andExpect(jsonPath("$.httpCode").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updatePatient_WithTakenNewEmail_AnsweredWithConflictStatus() throws Exception {
        // given
        String email = "test@gmail.com";
        String takenEmail = "takenEmail@gmail.com";
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand(takenEmail,
                "newPassword", "Andrew", "Kowalski", "600600600",
                LocalDate.of(1900, 12, 12));
        when(patientService.updatePatient(email, updatePatientCommand)).thenThrow(PatientAlreadyExistsException.byEmail(takenEmail));

        // when + then
        mvc.perform(put("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePatientCommand)))
                .andExpect(status().isConflict());
    }

    @Test
    void changePassword_WithNotExistingEmailAndCorrectChangePasswordCommand_AnsweredWithNotFoundStatus() throws Exception {
        // given
        String email = "notExistingEmail@gmail.com";
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("newPassword");
        when(patientService.changePassword(email, changePasswordCommand)).thenThrow(new PatientNotFoundException(email));

        // when + then
        mvc.perform(patch("/patients/{email}/password", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordCommand)))
                .andExpect(status().isNotFound());
    }

    @Test
    void removePatient_WithNotExistingEmail_AnsweredWithNotFoundStatus() throws Exception {
        // given
        String notExistingEmail = "notExisting@gmail.com";
        doThrow(new PatientNotFoundException(notExistingEmail)).when(patientService).removePatientByEmail(notExistingEmail);

        // when
        mvc.perform(delete("/patients/{email}", notExistingEmail))
                .andExpect(status().isNotFound());
    }
}
