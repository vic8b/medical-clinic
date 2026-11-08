package com.vic8b.medicalclinic.mapper;

import com.vic8b.medicalclinic.command.appointment.CreateAppointmentCommand;
import com.vic8b.medicalclinic.dto.appointment.AppointmentDto;
import com.vic8b.medicalclinic.model.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class})
public interface AppointmentMapper {
    AppointmentDto toDto(Appointment appointment);
    Appointment toEntity(CreateAppointmentCommand command);
}
