package com.vic8b.medicalclinic.mapper;

import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.dto.PatientDto;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public PatientDto toDto(@NonNull Patient patient) {
        return PatientDto.builder()
                .email(patient.getEmail())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phoneNumber(patient.getPhoneNumber())
                .birthday(patient.getBirthday())
                .build();
    }

    public Patient toPatient(@NonNull CreatePatientCommand command) {
        return Patient.builder()
                .email(command.email())
                .password(command.password())
                .idCardNo(command.idCardNo())
                .firstName(command.firstName())
                .lastName(command.lastName())
                .phoneNumber(command.phoneNumber())
                .birthday(command.birthday())
                .build();
    }
}
