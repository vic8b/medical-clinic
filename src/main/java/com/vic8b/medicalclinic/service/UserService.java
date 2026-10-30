package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.CreateUserCommand;
import com.vic8b.medicalclinic.dto.UserDto;
import com.vic8b.medicalclinic.exception.*;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto addUser(@NonNull CreateUserCommand command) {
        validateUsernameAvailability(command.username());
        validateEmailAvailability(command.email());
        User user = userMapper.toEntity(command);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public void removeUser(@NonNull Long id) {
        User user = findUserOrThrow(id);
        userRepository.delete(user);
    }

    @Transactional
    public UserDto changePassword(@NonNull Long id, @NonNull ChangePasswordCommand command) {
        User user = findUserOrThrow(id);
        user.changePassword(command.password());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto assignPatient(@NonNull Long userId, @NonNull Long patientId) {
        User user = findUserOrThrow(userId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        validateAssignmentAvailability(user, patient);
        user.assignPatient(patient);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto getUserById(@NonNull Long id) {
        return userMapper.toDto(findUserOrThrow(id));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    private void validateUsernameAvailability(String username) {
        if (userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException.byUsername(username);
        }
    }

    private void validateEmailAvailability(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw UserAlreadyExistsException.byEmail(email);
        }
    }

    private void validateAssignmentAvailability(User user, Patient patient) {
        if (user.getPatient() != null) {
            throw new UserAlreadyHasPatientException(user.getId());
        }
        if (patient.getUser() != null) {
            throw new PatientAlreadyAssignedException(patient.getId());
        }
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
