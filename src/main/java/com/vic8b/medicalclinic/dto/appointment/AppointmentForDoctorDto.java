package com.vic8b.medicalclinic.dto.appointment;

import java.time.LocalDateTime;

public record AppointmentForDoctorDto(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                      String patientFirstName, String patientLastName) {
}
