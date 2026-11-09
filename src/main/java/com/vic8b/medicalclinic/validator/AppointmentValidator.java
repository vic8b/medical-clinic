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

@Component
@RequiredArgsConstructor
public class AppointmentValidator {
    private final AppointmentRepository appointmentRepository;

    public void validateDoctorAvailability(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        if (appointmentRepository.existsOverlappingAppointment(doctor, startTime, endTime)) {
            throw new AppointmentAlreadyBookedException();
        }
    }

    public void validateDoctorAvailabilityForUpdate(Doctor doctor, Long excludedAppointment, LocalDateTime startTime,
                                                    LocalDateTime endtime) {
        if (appointmentRepository.existsOverlappingAppointmentExcluding(doctor, excludedAppointment, startTime, endtime)) {
            throw new AppointmentAlreadyBookedException();
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
}
