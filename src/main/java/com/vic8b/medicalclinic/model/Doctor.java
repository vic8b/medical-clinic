package com.vic8b.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "DOCTORS")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(nullable = false)
    private String specialization;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;
    @ManyToMany
    @JoinTable(
            name = "DOCTOR_FACILITIES",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private Set<Facility> facilities;
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    public void update(@NonNull String specialization) {
        this.specialization = specialization;
    }

    public void assignUser(@NonNull User user) {
        this.user = user;
        user.assignDoctor(this);
    }

    public void assignFacility(Facility facility) {
        facilities.add(facility);
        facility.assignDoctor(this);
    }

    public void removeFacility(Facility facility) {
        facilities.remove(facility);
        facility.removeDoctor(this);
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
