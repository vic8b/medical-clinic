package com.vic8b.medicalclinic.command.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record AssignPatientToUserCommand(@NotBlank String idCardNo, @NotBlank String phoneNumber,
                                         @NotNull @PastOrPresent LocalDate birthday) {
}
