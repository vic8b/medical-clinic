package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.user.CreateUserCommand;
import com.vic8b.medicalclinic.dto.user.UserDto;
import com.vic8b.medicalclinic.exception.user.UserHasAssignedDoctorException;
import com.vic8b.medicalclinic.exception.user.UserHasAssignedPatientException;
import com.vic8b.medicalclinic.exception.user.UserNotFoundException;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.UserRepository;
import com.vic8b.medicalclinic.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Transactional
    public UserDto addUser(@NonNull CreateUserCommand command) {
        userValidator.validateUsernameAvailability(command.username());
        userValidator.validateEmailAvailability(command.email());
        User user = userMapper.toEntity(command);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public void removeUser(@NonNull Long id) {
        User user = findUserOrThrow(id);
        if (user.getPatient() != null) {
            throw new UserHasAssignedPatientException(id);
        }
        if (user.getDoctor() != null) {
            throw new UserHasAssignedDoctorException(id);
        }
        userRepository.delete(user);
    }

    @Transactional
    public UserDto changePassword(@NonNull Long id, @NonNull ChangePasswordCommand command) {
        User user = findUserOrThrow(id);
        user.changePassword(command.password());
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

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
