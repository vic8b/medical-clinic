package com.vic8b.medicalclinic.command;

import lombok.NonNull;

public record ChangePasswordCommand(@NonNull String password) {
}
