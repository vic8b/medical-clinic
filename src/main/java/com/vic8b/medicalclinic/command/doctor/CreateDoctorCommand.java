package com.vic8b.medicalclinic.command.doctor;

import com.vic8b.medicalclinic.command.user.CreateUserCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDoctorCommand(@Valid @NotNull CreateUserCommand userCommand, @NotBlank String specialization) {
}
