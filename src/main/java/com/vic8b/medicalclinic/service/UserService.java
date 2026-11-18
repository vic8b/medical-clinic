package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.user.CreateUserCommand;
import com.vic8b.medicalclinic.dto.PageDto;
import com.vic8b.medicalclinic.dto.user.UserDto;
import com.vic8b.medicalclinic.exception.user.UserHasAssignedDoctorException;
import com.vic8b.medicalclinic.exception.user.UserHasAssignedPatientException;
import com.vic8b.medicalclinic.exception.user.UserNotFoundException;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.UserRepository;
import com.vic8b.medicalclinic.validator.UserValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Transactional
    public UserDto addUser(@NonNull CreateUserCommand command) {
        log.info("Creating user");
        userValidator.validateUsernameAvailability(command.username());
        userValidator.validateEmailAvailability(command.email());
        User user = userMapper.toEntity(command);
        userRepository.save(user);
        log.info("User created: userId={}", user.getId());
        return userMapper.toDto(user);
    }

    @Transactional
    public void removeUser(@NonNull Long id) {
        log.info("Removing user for userId={}", id);
        User user = findUserOrThrow(id);
        if (user.getPatient() != null) {
            throw new UserHasAssignedPatientException(id);
        }
        if (user.getDoctor() != null) {
            throw new UserHasAssignedDoctorException(id);
        }
        userRepository.delete(user);
        log.info("User removed for userId={}", id);
    }

    @Transactional
    public UserDto changePassword(@NonNull Long id, @NonNull ChangePasswordCommand command) {
        log.info("Changing password for userId={}", id);
        User user = findUserOrThrow(id);
        user.changePassword(command.password());
        userRepository.save(user);
        log.info("Password changed for userId={}", user.getId());
        return userMapper.toDto(user);
    }

    public UserDto getUserById(@NonNull Long id) {
        return userMapper.toDto(findUserOrThrow(id));
    }

    public PageDto<UserDto> getUsers(Pageable pageable) {
        return PageDto.from(userRepository.findAll(pageable)
                .map(userMapper::toDto));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
