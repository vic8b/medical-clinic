package com.vic8b.medicalclinic.model;

import com.vic8b.medicalclinic.exception.InvalidDataException;
import com.vic8b.medicalclinic.exception.appointment.AppointmentAlreadyBookedException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "APPOINTMENTS")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public void assignDoctor(@NonNull Doctor doctor) {
        this.doctor = doctor;
        doctor.addAppointment(this);
    }

    public void assignPatient(@NonNull Patient patient) {
        if (!isAvailable()) {
            throw new AppointmentAlreadyBookedException();
        }
        this.patient = patient;
        patient.addAppointment(this);
    }

    public void update(LocalDateTime startDateTime, LocalDateTime endDateTime, Doctor doctor) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.doctor = doctor;
    }

    public boolean isAvailable() {
        return patient == null;
    }

    public void clearAppointment() {
        doctor.removeAppointment(this);
        if (!isAvailable()) {
            patient.removeAppointment(this);
        }
    }

    public void cancelAppointment() {
        if (this.patient == null) {
            throw new InvalidDataException("Appointment has no patient");
        }
        patient.removeAppointment(this);
        this.patient = null;
    }
}
