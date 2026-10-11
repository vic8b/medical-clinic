package com.vic8b.medicalclinic.command;

import lombok.NonNull;

import java.time.LocalDate;

public record CreatePatientCommand(@NonNull String email, @NonNull String password, @NonNull String idCardNo, @NonNull String firstName,
                            @NonNull String lastName, @NonNull String phoneNumber, @NonNull LocalDate birthday) {
}
