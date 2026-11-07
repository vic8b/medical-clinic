package com.vic8b.medicalclinic.dto.doctor;

import com.vic8b.medicalclinic.dto.facility.FacilityDto;

import java.util.Set;

public record DoctorDetailsDto(Long id, String email, String firstName, String lastName, String specialization,
                               Set<FacilityDto> facilities) {
}
