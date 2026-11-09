package com.vic8b.medicalclinic.mapper;

import com.vic8b.medicalclinic.command.facility.CreateFacilityCommand;
import com.vic8b.medicalclinic.dto.facility.FacilityDto;
import com.vic8b.medicalclinic.model.Facility;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacilityMapper {
    FacilityDto toDto(Facility facility);
    Facility toEntity(CreateFacilityCommand command);
}
