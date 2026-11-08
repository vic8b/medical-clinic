package com.vic8b.medicalclinic.dto.appointment;

import com.vic8b.medicalclinic.dto.doctor.DoctorDto;
import com.vic8b.medicalclinic.dto.patient.PatientDto;

import java.time.LocalDateTime;

public record AppointmentDto(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime, DoctorDto doctor,
                             PatientDto patient) {
}
