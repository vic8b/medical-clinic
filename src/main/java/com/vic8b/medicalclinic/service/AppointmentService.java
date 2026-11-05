package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.appointment.CreateAppointmentCommand;
import com.vic8b.medicalclinic.command.appointment.UpdateAppointmentCommand;
import com.vic8b.medicalclinic.dto.appointment.AppointmentDto;
import com.vic8b.medicalclinic.dto.appointment.AppointmentForDoctorDto;
import com.vic8b.medicalclinic.dto.appointment.AppointmentForPatientDto;
import com.vic8b.medicalclinic.dto.appointment.AvailableAppointmentDto;
import com.vic8b.medicalclinic.exception.appointment.AppointmentNotFoundException;
import com.vic8b.medicalclinic.exception.doctor.DoctorNotFoundException;
import com.vic8b.medicalclinic.exception.patient.PatientNotFoundException;
import com.vic8b.medicalclinic.mapper.AppointmentMapper;
import com.vic8b.medicalclinic.model.Appointment;
import com.vic8b.medicalclinic.model.Doctor;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.repository.AppointmentRepository;
import com.vic8b.medicalclinic.repository.DoctorRepository;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.validator.AppointmentValidator;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentValidator validator;

    @Transactional
    public AppointmentDto addAppointment(@NonNull CreateAppointmentCommand command) {
        Doctor doctor = findDoctorByIdOrThrow(command.doctorId());
        validator.validateStartTimeOfTheVisit(command.startDateTime(), command.endDateTime());
        validator.validateDoctorAvailability(doctor, command.startDateTime(), command.endDateTime());
        Appointment appointment = appointmentMapper.toEntity(command);
        appointment.assignDoctor(doctor);
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    @Transactional
    public AppointmentDto updateAppointment(@NonNull Long id, @NonNull UpdateAppointmentCommand command) {
        Appointment appointment = findAppointmentByIdOrThrow(id);
        Doctor assignedDoctor = appointment.getDoctor();
        Doctor doctor = findDoctorByIdOrThrow(command.doctorId());
        validator.validateStartTimeOfTheVisit(command.startDateTime(), command.endDateTime());
        if (!doctor.equals(assignedDoctor)) {
            assignedDoctor.removeAppointment(appointment);
        }
        validator.validateDoctorAvailabilityForUpdate(doctor, command.startDateTime(), command.endDateTime(), id);
        appointment.update(command.startDateTime(), command.endDateTime(), doctor);
        appointment.assignDoctor(doctor);
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    @Transactional
    public void removeAppointmentById(@NonNull Long id) {
        Appointment appointment = findAppointmentByIdOrThrow(id);
        appointment.clearAppointment();
        appointmentRepository.delete(appointment);
    }

    @Transactional
    public AppointmentDto assignPatient(@NonNull Long appointmentId, @NonNull Long patientId) {
        Appointment appointment = findAppointmentByIdOrThrow(appointmentId);
        Patient patient = findPatientOrThrow(patientId);
        validator.validateAppointmentIsNotInPast(appointment);
        appointment.assignPatient(patient);
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    @Transactional
    public void cancelAppointmentById(@NonNull Long id) {
        Appointment appointment = findAppointmentByIdOrThrow(id);
        appointment.cancelAppointment();
    }

    public AppointmentDto getAppointmentById(@NonNull Long id) {
        return appointmentMapper.toDto(findAppointmentByIdOrThrow(id));
    }

    public List<AppointmentForDoctorDto> getAppointmentsForDoctor(@NonNull Long doctorId) {
        Doctor doctor = findDoctorByIdOrThrow(doctorId);
        return doctor.getAppointments().stream()
                .map(appointmentMapper::toForDoctorDto)
                .toList();
    }

    public List<AppointmentForPatientDto> getAppointmentsForPatient(@NonNull Long patientId) {
        Patient patient = findPatientOrThrow(patientId);
        return patient.getAppointments().stream()
                .map(appointmentMapper::toForPatientDto)
                .toList();
    }

    public List<AppointmentDto> getAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    public List<AvailableAppointmentDto> getAvailableAppointments() {
        return appointmentRepository.findAllByPatientIsNullAndStartDateTimeAfter(LocalDateTime.now()).stream()
                .map(appointmentMapper::toAvailableDto)
                .toList();
    }

    private Appointment findAppointmentByIdOrThrow(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    private Doctor findDoctorByIdOrThrow(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    private Patient findPatientOrThrow(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }
}
