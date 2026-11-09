package com.vic8b.medicalclinic.command.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDoctorCommand(@NotNull Long userId, @NotBlank String specialization) {
}
