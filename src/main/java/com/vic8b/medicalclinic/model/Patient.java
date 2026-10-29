package com.vic8b.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @OneToOne(mappedBy = "patient")
    private User user;

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
    }

    public void removeUser() {
        this.user = null;
    }
}