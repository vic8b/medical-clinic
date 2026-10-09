package com.vic8b.medicalclinic.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PatientDto(String email, String firstName, String lastName, String phoneNumber, LocalDate birthday) {
}
