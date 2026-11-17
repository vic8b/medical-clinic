package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.facility.CreateFacilityCommand;
import com.vic8b.medicalclinic.dto.PageDto;
import com.vic8b.medicalclinic.dto.facility.FacilityDto;
import com.vic8b.medicalclinic.exception.facility.FacilityAlreadyExistsException;
import com.vic8b.medicalclinic.exception.facility.FacilityNotFoundException;
import com.vic8b.medicalclinic.mapper.FacilityMapper;
import com.vic8b.medicalclinic.model.Facility;
import com.vic8b.medicalclinic.repository.FacilityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    @Transactional
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

    public PageDto<FacilityDto> getFacilities(Long doctorId, Pageable pageable) {
        if (doctorId != null) {
            return PageDto.from(facilityRepository.findByDoctorsId(doctorId, pageable)
                    .map(facilityMapper::toDto));
        }
        return PageDto.from(facilityRepository.findAll(pageable)
                .map(facilityMapper::toDto));
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
