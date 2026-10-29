package com.vic8b.medicalclinic.model;

import com.vic8b.medicalclinic.exception.InvalidDataException;
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
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @Builder
    public User(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String firstName, @NonNull String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void changePassword(@NonNull String newPassword) {
        if (newPassword.isBlank()) {
            throw new InvalidDataException("Password cannot be blank");
        }
        this.password = newPassword;
    }

    public void assignPatient(@NonNull Patient patient) {
        this.patient = patient;
        patient.assignUser(this);
    }

    public void removeAssignedPatient() {
        if (patient != null) {
            patient.removeUser();
            patient = null;
        }
    }
}
