package com.vic8b.medicalclinic.command.doctor;

import jakarta.validation.constraints.NotBlank;

public record AssignDoctorToUserCommand(@NotBlank String specialization) {
}
