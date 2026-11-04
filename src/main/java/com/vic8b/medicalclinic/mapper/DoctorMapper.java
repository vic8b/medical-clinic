package com.vic8b.medicalclinic.mapper;

import com.vic8b.medicalclinic.command.doctor.CreateDoctorCommand;
import com.vic8b.medicalclinic.command.doctor.CreateDoctorProfileCommand;
import com.vic8b.medicalclinic.dto.doctor.DoctorDto;
import com.vic8b.medicalclinic.dto.doctor.FacilityDoctorDto;
import com.vic8b.medicalclinic.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    DoctorDto toDto(Doctor doctor);
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    FacilityDoctorDto toFacilityDto(Doctor doctor);
    Doctor toEntity(CreateDoctorCommand command);
    Doctor toEntity(CreateDoctorProfileCommand command);
}
