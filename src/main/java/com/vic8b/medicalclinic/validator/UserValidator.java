package com.vic8b.medicalclinic.validator;

import com.vic8b.medicalclinic.exception.user.UserAlreadyExistsException;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateUsernameAvailability(String username) {
        if (userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException.byUsername(username);
        }
    }

    public void validateEmailAvailability(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw UserAlreadyExistsException.byEmail(email);
        }
    }

    public void validateUpdatedUserAvailability(User user, String email, String username) {
        if (!user.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmailIgnoreCase(email)) {
            throw UserAlreadyExistsException.byEmail(email);
        }
        if (!user.getUsername().equalsIgnoreCase(username) && userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException.byUsername(username);
        }
    }
}
