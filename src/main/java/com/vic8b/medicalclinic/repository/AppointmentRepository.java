package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Appointment;
import com.vic8b.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDoctorId(Long doctorId);
    List<Appointment> findAllByPatientId(Long patientId);
    List<Appointment> findAllByDoctorIdAndPatientId(Long doctorId, Long patientId);
    List<Appointment> findAllByPatientIsNullAndStartDateTimeAfter(LocalDateTime startNow);
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
}
