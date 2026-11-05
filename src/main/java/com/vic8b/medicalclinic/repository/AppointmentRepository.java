package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Appointment;
import com.vic8b.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByPatientIsNullAndStartDateTimeAfter(LocalDateTime startNow);
    List<Appointment> findAllByDoctor(Doctor doctor);
}
