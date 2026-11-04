package com.vic8b.medicalclinic.command.appointment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateAppointmentCommand(@NotNull @FutureOrPresent LocalDateTime startDateTime,
                                       @NotNull @FutureOrPresent LocalDateTime endDateTime,
                                       @NotNull Long doctorId) {
}
