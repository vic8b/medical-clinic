package com.vic8b.medicalclinic.mapper;

import com.vic8b.medicalclinic.command.appointment.CreateAppointmentCommand;
import com.vic8b.medicalclinic.dto.appointment.AppointmentDto;
import com.vic8b.medicalclinic.dto.appointment.AppointmentForDoctorDto;
import com.vic8b.medicalclinic.dto.appointment.AppointmentForPatientDto;
import com.vic8b.medicalclinic.dto.appointment.AvailableAppointmentDto;
import com.vic8b.medicalclinic.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDto toDto(Appointment appointment);
    @Mapping(target = "doctorFirstName", source = "doctor.user.firstName")
    @Mapping(target = "doctorLastName", source = "doctor.user.lastName")
    AppointmentForPatientDto toForPatientDto(Appointment appointment);
    @Mapping(target = "patientFirstName", source = "patient.user.firstName")
    @Mapping(target = "patientLastName", source = "patient.user.lastName")
    AppointmentForDoctorDto toForDoctorDto(Appointment appointment);
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorFirstName", source = "doctor.user.firstName")
    @Mapping(target = "doctorLastName", source = "doctor.user.lastName")
    @Mapping(target = "doctorSpecialization", source = "doctor.specialization")
    AvailableAppointmentDto toAvailableDto(Appointment appointment);
    Appointment toEntity(CreateAppointmentCommand command);
}
