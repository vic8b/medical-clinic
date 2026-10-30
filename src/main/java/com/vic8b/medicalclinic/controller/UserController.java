package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.CreateUserCommand;
import com.vic8b.medicalclinic.dto.UserDto;
import com.vic8b.medicalclinic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody CreateUserCommand command) {
        return userService.addUser(command);
    }

    @PutMapping("/{userId}/patient/{patientId}")
    public UserDto assignPatient(@PathVariable Long userId, @PathVariable Long patientId) {
        return userService.assignPatient(userId, patientId);
    }

    @PatchMapping("/{id}/password")
    public UserDto changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordCommand command) {
        return userService.changePassword(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.removeUser(id);
    }
}
