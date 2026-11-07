package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.facility.CreateFacilityCommand;
import com.vic8b.medicalclinic.dto.doctor.DoctorDto;
import com.vic8b.medicalclinic.dto.facility.FacilityDto;
import com.vic8b.medicalclinic.exception.facility.FacilityAlreadyExistsException;
import com.vic8b.medicalclinic.exception.facility.FacilityNotFoundException;
import com.vic8b.medicalclinic.mapper.DoctorMapper;
import com.vic8b.medicalclinic.mapper.FacilityMapper;
import com.vic8b.medicalclinic.model.Facility;
import com.vic8b.medicalclinic.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;
    private final DoctorMapper doctorMapper;

    public FacilityDto addFacility(@NonNull CreateFacilityCommand command) {
        validateNameAvailability(command.name());
        Facility facility = facilityMapper.toEntity(command);
        facilityRepository.save(facility);
        return facilityMapper.toDto(facility);
    }

    @Transactional
    public void removeFacilityById(@NonNull Long id) {
        Facility facility = findFacilityByIdOrThrow(id);
        facility.clearFacility();
        facilityRepository.delete(facility);
    }

    public FacilityDto getFacilityById(@NonNull Long id) {
        Facility facility = findFacilityByIdOrThrow(id);
        return facilityMapper.toDto(facility);
    }

    public Set<FacilityDto> getFacilities(Long doctorId) {
        if (doctorId != null) {
            return facilityRepository.findByDoctorsId(doctorId).stream()
                    .map(facilityMapper::toDto)
                    .collect(Collectors.toSet());
        }
        return facilityRepository.findAll().stream()
                .map(facilityMapper::toDto)
                .collect(Collectors.toSet());
    }

    public Set<DoctorDto> getDoctors(@NonNull Long facilityId) {
        Facility facility = findFacilityByIdOrThrow(facilityId);
        return facility.getDoctors().stream()
                .map(doctorMapper::toDtoWithoutFacilities)
                .collect(Collectors.toSet());
    }

    private void validateNameAvailability(String name) {
        if (facilityRepository.existsByName(name)) {
            throw new FacilityAlreadyExistsException(name);
        }
    }

    private Facility findFacilityByIdOrThrow(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException(id));
    }
}
