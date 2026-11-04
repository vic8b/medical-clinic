package com.vic8b.medicalclinic.dto.appointment;

import java.time.LocalDateTime;

public record AvailableAppointmentDto(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime, Long doctorId,
                                      String doctorFirstName, String doctorLastName, String doctorSpecialization) {
}
