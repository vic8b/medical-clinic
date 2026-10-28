package com.vic8b.medicalclinic.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record UpdatePatientCommand(@Email @NotBlank String email, @NotBlank String password, @NotBlank String firstName,
                                   @NotBlank String lastName, @NotBlank String phoneNumber, @NotNull @PastOrPresent LocalDate birthday) {
}
