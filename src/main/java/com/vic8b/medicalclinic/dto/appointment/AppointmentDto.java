package com.vic8b.medicalclinic.dto.appointment;

import java.time.LocalDateTime;

public record AppointmentDto(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
}
