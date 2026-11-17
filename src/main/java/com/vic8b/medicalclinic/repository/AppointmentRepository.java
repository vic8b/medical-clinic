package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Appointment;
import com.vic8b.medicalclinic.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findAllByDoctorId(Long doctorId, Pageable pageable);
    Page<Appointment> findAllByPatientId(Long patientId, Pageable pageable);
    Page<Appointment> findAllByDoctorIdAndPatientId(Long doctorId, Long patientId, Pageable pageable);
    Page<Appointment> findAllByPatientIsNullAndStartDateTimeAfter(LocalDateTime startNow, Pageable pageable);
    @Query("""
            SELECT COUNT(a) > 0
            FROM Appointment a
            WHERE a.doctor = :doctor
            AND a.startDateTime < :endDateTime
            AND a.endDateTime > :startDateTime
            """)
    boolean existsOverlappingAppointment(@Param("doctor") Doctor doctor, @Param("startDateTime") LocalDateTime startDateTime,
                                         @Param("endDateTime") LocalDateTime endDateTime);
    @Query("""
            SELECT COUNT(a) > 0
            FROM Appointment a
            WHERE a.doctor = :doctor
            AND a.id <> :appointmentId
            AND a.startDateTime < :endDateTime
            AND a.endDateTime > :startDateTime
            """)
    boolean existsOverlappingAppointmentExcluding(@Param("doctor") Doctor doctor, @Param("appointmentId") Long appointmentId,
                                                  @Param("startDateTime") LocalDateTime startDateTime,
                                                  @Param("endDateTime") LocalDateTime endDateTime);
    boolean existsByDoctorId(Long doctorId);
}
