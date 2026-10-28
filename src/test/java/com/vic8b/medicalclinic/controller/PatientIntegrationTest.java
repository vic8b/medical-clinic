package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.repository.PatientRepository;
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

    @BeforeEach
    void setup() {
        patientRepository.deleteAll();
    }

    @Test
    void addPatient_WithExistingIdCardNo_AnsweredWithStatusConflict() throws Exception {
        // given
        String existingIdCardNo = "ABC123456";
        Patient patient = Patient.builder().email("test@gmail.com").password("pass1234").idCardNo(existingIdCardNo)
                .firstName("Wiktor").lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build();
        patientRepository.save(patient);
        String addRequest = """
                {
                    "email": "addedPatient@gmail.com",
                    "password": "pass0000",
                    "idCardNo": "ABC123456",
                    "firstName": "Andrew",
                    "lastName": "Kowalski",
                    "phoneNumber": "999999999",
                    "birthday": "1900-01-01"
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
                    "email": null,
                    "password": "pass0000",
                    "idCardNo": "ABC123456",
                    "firstName": "Andrew",
                    "lastName": "Kowalski",
                    "phoneNumber": "999999999",
                    "birthday": "1900-01-01"
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
        Patient patient = Patient.builder().email("test@gmail.com").password("pass1234").idCardNo(originalIdCardNo)
                .firstName("Wiktor").lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build();
        patientRepository.save(patient);
        String updateRequest = """
                {
                    "email": "updatedEmail@gmail.com",
                    "password": "updatedPass",
                    "idCardNo": "shouldNotBeAffected",
                    "firstName": "Andrew",
                    "lastName": "Kowalski",
                    "phoneNumber": "999999999",
                    "birthday": "1900-01-01"
                }
                """;

        // when + then
        mvc.perform(put("/patients/{email}", patient.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk());
        Patient updatedPatient = patientRepository.findByEmailIgnoreCase("updatedEmail@gmail.com").orElseThrow();
        assertAll(
                () -> assertThat(updatedPatient.getIdCardNo()).isEqualTo(originalIdCardNo),
                () -> assertThat(updatedPatient.getFirstName()).isEqualTo("Andrew"),
                () -> assertThat(updatedPatient.getLastName()).isEqualTo("Kowalski"),
                () -> assertThat(updatedPatient.getPhoneNumber()).isEqualTo("999999999")
        );
    }

    @Test
    void updatePatient_WithNullValue_AnsweredWithBadRequestAndPatientNotChanged() throws Exception {
        // given
        Patient patient = Patient.builder().email("test@gmail.com").password("pass1234").idCardNo("ABC123456")
                .firstName("Wiktor").lastName("Test").phoneNumber("500500500").birthday(LocalDate.of(1999, 1, 1)).build();
        patientRepository.save(patient);
        String updateRequest = """
                {
                    "email": null,
                    "password": "updatedPass",
                    "idCardNo": "shouldNotBeAffected",
                    "firstName": "Andrew",
                    "lastName": "Kowalski",
                    "phoneNumber": "999999999",
                    "birthday": "1900-01-01"
                }
                """;

        // when + then
        mvc.perform(put("/patients/{email}", patient.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isBadRequest());
        Patient unchangedPatient = patientRepository.findByEmailIgnoreCase("test@gmail.com").orElseThrow();
        assertAll(
                () -> assertThat(unchangedPatient.getEmail()).isEqualTo("test@gmail.com"),
                () -> assertThat(unchangedPatient.getPassword()).isEqualTo("pass1234"),
                () -> assertThat(unchangedPatient.getFirstName()).isEqualTo("Wiktor"),
                () -> assertThat(unchangedPatient.getLastName()).isEqualTo("Test"),
                () -> assertThat(unchangedPatient.getPhoneNumber()).isEqualTo("500500500"),
                () -> assertThat(unchangedPatient.getBirthday()).isEqualTo(LocalDate.of(1999, 1, 1))
        );
    }
}
