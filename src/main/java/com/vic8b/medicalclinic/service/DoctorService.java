package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.doctor.AssignDoctorToUserCommand;
import com.vic8b.medicalclinic.command.doctor.CreateDoctorCommand;
import com.vic8b.medicalclinic.command.doctor.UpdateDoctorCommand;
import com.vic8b.medicalclinic.command.user.CreateUserCommand;
import com.vic8b.medicalclinic.dto.doctor.DoctorDetailsDto;
import com.vic8b.medicalclinic.exception.doctor.DoctorNotFoundException;
import com.vic8b.medicalclinic.exception.facility.FacilityNotFoundException;
import com.vic8b.medicalclinic.exception.user.UserAlreadyHasDoctorException;
import com.vic8b.medicalclinic.exception.user.UserNotFoundException;
import com.vic8b.medicalclinic.mapper.DoctorMapper;
import com.vic8b.medicalclinic.mapper.UserMapper;
import com.vic8b.medicalclinic.model.Doctor;
import com.vic8b.medicalclinic.model.Facility;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.DoctorRepository;
import com.vic8b.medicalclinic.repository.FacilityRepository;
import com.vic8b.medicalclinic.repository.UserRepository;
import com.vic8b.medicalclinic.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final FacilityRepository facilityRepository;

    @Transactional
    public DoctorDetailsDto addDoctor(@NonNull CreateDoctorCommand createDoctorCommand) {
        User user = createUser(createDoctorCommand.userCommand());
        Doctor doctor = doctorMapper.toEntity(createDoctorCommand);
        doctor.assignUser(user);
        doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public DoctorDetailsDto assignDoctorToUser(@NonNull Long userId, @NonNull AssignDoctorToUserCommand command) {
        User user = findUserByIdOrThrow(userId);
        Doctor doctor = doctorMapper.toEntity(command);
        validateUserDoesNotHaveDoctor(user);
        doctor.assignUser(user);
        doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public DoctorDetailsDto assignFacilityToDoctor(@NonNull Long doctorId, @NonNull Long facilityId) {
        Doctor doctor = findDoctorByIdOrThrow(doctorId);
        Facility facility = findFacilityByIdOrThrow(facilityId);
        doctor.assignFacility(facility);
        doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public void removeDoctorById(@NonNull Long id) {
        Doctor doctor = findDoctorByIdOrThrow(id);
        doctor.getUser().clearDoctor();
        doctorRepository.delete(doctor);
    }

    @Transactional
    public DoctorDetailsDto updateDoctor(@NonNull Long id, @NonNull UpdateDoctorCommand command) {
        Doctor doctor = findDoctorByIdOrThrow(id);
        User user = doctor.getUser();
        userValidator.validateUpdatedUserAvailability(user, command.email(), command.username());
        doctor.update(command.specialization());
        user.update(command.username(), command.email(), command.password(), command.firstName(), command.lastName());
        doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    public DoctorDetailsDto getDoctorById(@NonNull Long id) {
        return doctorMapper.toDto(findDoctorByIdOrThrow(id));
    }

    public List<DoctorDetailsDto> getDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDto)
                .toList();
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private User createUser(CreateUserCommand createUserCommand) {
        userValidator.validateUsernameAvailability(createUserCommand.username());
        userValidator.validateEmailAvailability(createUserCommand.email());
        return userRepository.save(userMapper.toEntity(createUserCommand));
    }

    private void validateUserDoesNotHaveDoctor(User user) {
        if (user.getDoctor() != null) {
            throw new UserAlreadyHasDoctorException(user.getId());
        }
    }

    private Doctor findDoctorByIdOrThrow(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    private Facility findFacilityByIdOrThrow(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException(id));
    }
}
