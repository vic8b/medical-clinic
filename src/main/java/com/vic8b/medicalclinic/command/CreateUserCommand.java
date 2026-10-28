package com.vic8b.medicalclinic.command;

import jakarta.validation.constraints.NotBlank;

public record CreateUserCommand(@NotBlank String username, @NotBlank String password) {
}
