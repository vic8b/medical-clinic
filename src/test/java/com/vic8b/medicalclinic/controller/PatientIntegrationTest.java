package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PatientIntegrationTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAllInBatch();
        patientRepository.deleteAllInBatch();
    }

    @Test
    void addPatient_WithExistingIdCardNo_AnsweredWithStatusConflict() throws Exception {
        // given
        String existingIdCardNo = "ABC123456";
        Patient patient = Patient.builder().idCardNo(existingIdCardNo).phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        patientRepository.save(patient);
        String addRequest = """
                {
                    "userCommand": {
                        "username": "username",
                        "email": "test@gmail.com",
                        "password": "pass1234",
                        "firstName": "Wiktor",
                        "lastName": "Test"
                      },
                    "idCardNo": "ABC123456",
                    "phoneNumber": "500500500",
                    "birthday": "1999-01-01"
                }
                """;

        // when + then
        mvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRequest))
                .andExpect(status().isConflict());
        assertThat(patientRepository.findAll()).hasSize(1);
    }

    @Test
    void addPatient_WithNullValue_AnsweredWithBadRequest() throws Exception {
        // given
        String addRequestWithNullField = """
                {
                    "userCommand": {
                        "username": "username",
                        "email": "test@gmail.com",
                        "password": "pass1234",
                        "firstName": "Wiktor",
                        "lastName": "Test"
                      },
                    "idCardNo": null,
                    "phoneNumber": "500500500",
                    "birthday": "1999-01-01"
                }
                """;

        // when + then
        mvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRequestWithNullField))
                .andExpect(status().isBadRequest());
        assertThat(patientRepository.findAll()).isEmpty();
    }

    @Test
    void updatePatient_WithChangedIdCardNo_AnsweredOkAndIdCardNotChanged() throws Exception {
        // given
        String originalIdCardNo = "ABC123456";
        Patient patient = Patient.builder().idCardNo(originalIdCardNo).phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        patientRepository.save(patient);
        User user = User.builder().username("username").email("test@gmail.com").password("pass1234").firstName("Wiktor")
                .lastName("Test").build();
        user.assignPatient(patient);
        userRepository.save(user);
        String updateRequest = """
                {
                    "phoneNumber": "999999999",
                    "birthday": "1990-01-01"
                }
                """;

        // when + then
        mvc.perform(put("/patients/{email}", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk());
        Patient updatedPatient = patientRepository.findByUserEmailIgnoreCase("test@gmail.com").orElseThrow();
        assertAll(
                () -> assertThat(updatedPatient.getIdCardNo()).isEqualTo(originalIdCardNo),
                () -> assertThat(updatedPatient.getPhoneNumber()).isEqualTo("999999999"),
                () -> assertThat(updatedPatient.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1))
        );
    }

    @Test
    void updatePatient_WithNullValue_AnsweredWithBadRequestAndPatientNotChanged() throws Exception {
        // given
        Patient patient = Patient.builder().idCardNo("ABC123456").phoneNumber("500500500")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        patientRepository.save(patient);
        User user = User.builder().username("username").email("test@gmail.com").password("pass1234").firstName("Wiktor")
                .lastName("Test").build();
        user.assignPatient(patient);
        userRepository.save(user);
        String updateRequest = """
                {
                    "phoneNumber": null,
                    "birthday": "1990-01-01"
                }
                """;

        // when + then
        mvc.perform(put("/patients/{email}", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isBadRequest());
        Patient unchangedPatient = patientRepository.findByUserEmailIgnoreCase("test@gmail.com").orElseThrow();
        assertAll(
                () -> assertThat(unchangedPatient.getPhoneNumber()).isEqualTo("500500500"),
                () -> assertThat(unchangedPatient.getBirthday()).isEqualTo(LocalDate.of(1999, 1, 1))
        );
    }
}
