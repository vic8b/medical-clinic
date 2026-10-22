package com.vic8b.medicalclinic.model;

import com.vic8b.medicalclinic.exception.PatientInvalidDataException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;
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
            throw new PatientInvalidDataException("Password cannot be blank");
        }
        this.password = newPassword;
    }

    private static void validatePatientData(String email, String password, String idCardNo, String firstName,
                                            String lastName, String phoneNumber, LocalDate birthday) {
        if (email.isBlank()) {
            throw new PatientInvalidDataException("Email cannot be blank");
        }
        if (!isEmailValid(email)) {
            throw new PatientInvalidDataException("Invalid email");
        }
        if (password.isBlank()) {
            throw new PatientInvalidDataException("Password cannot be blank");
        }
        if (idCardNo.isBlank()) {
            throw new PatientInvalidDataException("ID card number cannot be blank");
        }
        if (firstName.isBlank()) {
            throw new PatientInvalidDataException("First name cannot be blank");
        }
        if (lastName.isBlank()) {
            throw new PatientInvalidDataException("Last name cannot be blank");
        }
        if (phoneNumber.isBlank()) {
            throw new PatientInvalidDataException("Phone number cannot be blank");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new PatientInvalidDataException("Birthday cannot be in the future");
        }
    }

    private static boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}