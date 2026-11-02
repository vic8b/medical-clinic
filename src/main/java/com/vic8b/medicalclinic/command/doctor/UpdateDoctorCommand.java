package com.vic8b.medicalclinic.command.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateDoctorCommand(@NotBlank String username, @Email @NotBlank String email, @NotBlank String password,
                                  @NotBlank String firstName, @NotBlank String lastName, @NotBlank String specialization) {
}
