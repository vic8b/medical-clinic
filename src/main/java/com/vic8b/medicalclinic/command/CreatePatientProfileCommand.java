package com.vic8b.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreatePatientProfileCommand(@NotBlank String idCardNo, @NotBlank String phoneNumber, @NotNull @PastOrPresent LocalDate birthday) {
}
