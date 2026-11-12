package com.vic8b.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PATIENTS")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(unique = true, nullable = false)
    private String idCardNo;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private LocalDate birthday;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @Builder
    public Patient(@NonNull String idCardNo, @NonNull String phoneNumber, @NonNull LocalDate birthday) {
        this.idCardNo = idCardNo;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public void update(@NonNull String phoneNumber, @NonNull LocalDate birthday) {
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public void assignUser(@NonNull User user) {
        this.user = user;
        user.assignPatient(this);
    }

    public void addAppointment(Appointment appointment) {
        if (!appointments.contains(appointment)) {
            appointments.add(appointment);
        }
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }
}