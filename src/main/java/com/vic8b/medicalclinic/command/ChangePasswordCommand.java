package com.vic8b.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordCommand(@NotBlank String password) {
}
