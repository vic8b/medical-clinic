package com.vic8b.medicalclinic.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Patient {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    @EqualsAndHashCode.Include
    private final UUID id;
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    @Builder
    public Patient(@NonNull String email, @NonNull String password, @NonNull String idCardNo, @NonNull String firstName,
                   @NonNull String lastName, @NonNull String phoneNumber, @NonNull LocalDate birthday) {
        validatePatientData(email, password, idCardNo, firstName, lastName, phoneNumber, birthday);
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public void update(@NonNull String email, @NonNull String password, @NonNull String idCardNo, @NonNull String firstName,
                       @NonNull String lastName, @NonNull String phoneNumber, @NonNull LocalDate birthday) {
        validatePatientData(email, password, idCardNo, firstName, lastName, phoneNumber, birthday);
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public void changePassword(@NonNull String newPassword) {
        if (newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
        this.password = newPassword;
    }

    private static void validatePatientData(String email, String password, String idCardNo, String firstName,
                                            String lastName, String phoneNumber, LocalDate birthday) {
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
        if (idCardNo.isBlank()) {
            throw new IllegalArgumentException("ID card number cannot be blank");
        }
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }
        if (phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be blank");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birthday cannot be in the future");
        }
    }

    private static boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}