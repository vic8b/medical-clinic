package com.vic8b.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USERS")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @Builder
    public User(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    public void assignPatient(@NonNull Patient patient) {
        this.patient = patient;
        patient.assignUser(this);
    }
}
