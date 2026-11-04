package com.vic8b.medicalclinic.dto.appointment;

import java.time.LocalDateTime;

public record AppointmentForPatientDto(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                       String doctorFirstName, String doctorLastName) {
}
