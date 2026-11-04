package com.vic8b.medicalclinic.validator;

import com.vic8b.medicalclinic.exception.appointment.AppointmentAlreadyBookedException;
import com.vic8b.medicalclinic.exception.appointment.InvalidStartTimeOfAppointmentException;
import com.vic8b.medicalclinic.exception.appointment.PastAppointmentException;
import com.vic8b.medicalclinic.model.Appointment;
import com.vic8b.medicalclinic.model.Doctor;
import com.vic8b.medicalclinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {
    private final AppointmentRepository appointmentRepository;

    public void validateDoctorAvailability(Doctor doctor, LocalDateTime startTime, LocalDateTime endtime) {
        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctor(doctor);
        for (Appointment appointment : doctorAppointments) {
            if (isOverlapping(appointment, startTime, endtime)) {
                throw new AppointmentAlreadyBookedException(appointment.getId());
            }
        }
    }

    public void validateDoctorAvailabilityForUpdate(Doctor doctor, LocalDateTime startTime, LocalDateTime endtime,
                                                    Long excludedAppointment) {
        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctor(doctor).stream()
                .filter(appointment -> !appointment.getId().equals(excludedAppointment))
                .toList();
        for (Appointment appointment : doctorAppointments) {
            if (isOverlapping(appointment, startTime, endtime)) {
                throw new AppointmentAlreadyBookedException(appointment.getId());
            }
        }
    }

    public void validateStartTimeOfTheVisit(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new InvalidStartTimeOfAppointmentException();
        }
        if (start.getMinute() % 15 != 0 || start.getSecond() != 0 || start.getNano() != 0) {
            throw new InvalidStartTimeOfAppointmentException();
        }
    }

    public void validateAppointmentIsNotInPast(Appointment appointment) {
        if (!appointment.getStartDateTime().isAfter(LocalDateTime.now())) {
            throw new PastAppointmentException(appointment.getId());
        }
    }

    public boolean isOverlapping(Appointment appointment, LocalDateTime startTime, LocalDateTime endtime) {
        return startTime.isBefore(appointment.getEndDateTime()) && endtime.isAfter(appointment.getStartDateTime());
    }
}
