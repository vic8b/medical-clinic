package com.vic8b.medicalclinic.command;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record UpdatePatientCommand(
        @NonNull String email,
        @NonNull String password,
        @NonNull String idCardNo,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String phoneNumber,
        @NonNull LocalDate birthday
) {
}
