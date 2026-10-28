package com.vic8b.medicalclinic.model;

import com.vic8b.medicalclinic.exception.PatientInvalidDataException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "PATIENTS")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String idCardNo;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private LocalDate birthday;

    @Builder
    public Patient(@NonNull String email, @NonNull String password, @NonNull String idCardNo, @NonNull String firstName,
                   @NonNull String lastName, @NonNull String phoneNumber, @NonNull LocalDate birthday) {
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public void update(@NonNull String email, @NonNull String password, @NonNull String firstName,
                       @NonNull String lastName, @NonNull String phoneNumber, @NonNull LocalDate birthday) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public void changePassword(@NonNull String newPassword) {
        if (newPassword.isBlank()) {
            throw new PatientInvalidDataException("Password cannot be blank");
        }
        this.password = newPassword;
    }
}