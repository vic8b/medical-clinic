package com.vic8b.medicalclinic.mapper;

import com.vic8b.medicalclinic.command.doctor.AssignDoctorToUserCommand;
import com.vic8b.medicalclinic.command.doctor.CreateDoctorCommand;
import com.vic8b.medicalclinic.dto.doctor.DoctorDetailsDto;
import com.vic8b.medicalclinic.dto.doctor.DoctorDto;
import com.vic8b.medicalclinic.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    DoctorDetailsDto toDto(Doctor doctor);
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    DoctorDto toDtoWithoutFacilities(Doctor doctor);
    Doctor toEntity(CreateDoctorCommand command);
    Doctor toEntity(AssignDoctorToUserCommand command);
}
