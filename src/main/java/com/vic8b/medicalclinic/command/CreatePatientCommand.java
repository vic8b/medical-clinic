package com.vic8b.medicalclinic.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreatePatientCommand(@Valid @NotNull CreateUserCommand userCommand, @NotBlank String idCardNo,
                                   @NotBlank String phoneNumber, @NotNull @PastOrPresent LocalDate birthday) {
}
