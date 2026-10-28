package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.CreateUserCommand;
import com.vic8b.medicalclinic.dto.UserDto;
import com.vic8b.medicalclinic.exception.PatientNotFoundException;
import com.vic8b.medicalclinic.exception.UserAlreadyExistsException;
import com.vic8b.medicalclinic.exception.UserNotFoundException;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.Patient;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.PatientRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
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

    public UserDto addUser(@NonNull CreateUserCommand command) {
        validateUsernameAvailability(command.username());
        User user = userMapper.toEntity(command);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void removeUser(@NonNull Long id) {
        User user = findUserOrThrow(id);
        userRepository.delete(user);
    }

    public UserDto getUserById(@NonNull Long id) {
        return userMapper.toDto(findUserOrThrow(id));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto assignPatient(@NonNull Long id, @NonNull String patientEmail) {
        User user = findUserOrThrow(id);
        Patient patient = patientRepository.findByEmailIgnoreCase(patientEmail)
                .orElseThrow(() -> new PatientNotFoundException(patientEmail));
        user.assignPatient(patient);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private void validateUsernameAvailability(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);
        }
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
