package com.vic8b.medicalclinic.service;

import com.vic8b.medicalclinic.command.doctor.CreateDoctorCommand;
import com.vic8b.medicalclinic.command.doctor.UpdateDoctorCommand;
import com.vic8b.medicalclinic.dto.PageDto;
import com.vic8b.medicalclinic.dto.doctor.DoctorDto;
import com.vic8b.medicalclinic.exception.doctor.DoctorHasAppointmentsException;
import com.vic8b.medicalclinic.exception.doctor.DoctorNotFoundException;
import com.vic8b.medicalclinic.exception.facility.FacilityNotFoundException;
import com.vic8b.medicalclinic.exception.user.UserAlreadyHasDoctorException;
import com.vic8b.medicalclinic.exception.user.UserNotFoundException;
import com.vic8b.medicalclinic.mapper.DoctorMapper;
import com.vic8b.medicalclinic.model.Doctor;
import com.vic8b.medicalclinic.model.Facility;
import com.vic8b.medicalclinic.model.User;
import com.vic8b.medicalclinic.repository.AppointmentRepository;
import com.vic8b.medicalclinic.repository.DoctorRepository;
import com.vic8b.medicalclinic.repository.FacilityRepository;
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
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;
    private final UserValidator userValidator;
    private final FacilityRepository facilityRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public DoctorDto addDoctor(@NonNull CreateDoctorCommand createDoctorCommand) {
        log.info("Creating doctor for userId={}", createDoctorCommand.userId());
        User user = findUserByIdOrThrow(createDoctorCommand.userId());
        validateUserDoesNotHaveDoctor(user);
        Doctor doctor = doctorMapper.toEntity(createDoctorCommand);
        doctor.assignUser(user);
        doctorRepository.save(doctor);
        log.info("Doctor created: userId={}, doctorId={}", createDoctorCommand.userId(), doctor.getId());
        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public DoctorDto assignFacilityToDoctor(@NonNull Long doctorId, @NonNull Long facilityId) {
        log.info("Assigning facility: doctorId={}, facilityId={}", doctorId, facilityId);
        Doctor doctor = findDoctorByIdOrThrow(doctorId);
        Facility facility = findFacilityByIdOrThrow(facilityId);
        doctor.assignFacility(facility);
        doctorRepository.save(doctor);
        log.info("Facility assigned: doctorId={}, facilityId={}", doctorId, facilityId);
        return doctorMapper.toDto(doctor);
    }

    @Transactional
    public void removeDoctorById(@NonNull Long id) {
        log.info("Removing doctor for doctorId={}", id);
        Doctor doctor = findDoctorByIdOrThrow(id);
        if (appointmentRepository.existsByDoctorId(id)) {
            throw new DoctorHasAppointmentsException(id);
        }
        doctor.getUser().clearDoctor();
        doctorRepository.delete(doctor);
        log.info("Doctor removed: doctorId={}", id);
    }

    @Transactional
    public DoctorDto updateDoctor(@NonNull Long id, @NonNull UpdateDoctorCommand command) {
        log.info("Updating doctor for doctorId={}", id);
        Doctor doctor = findDoctorByIdOrThrow(id);
        User user = doctor.getUser();
        userValidator.validateUpdatedUserAvailability(user, command.email(), command.username());
        doctor.update(command.specialization());
        user.update(command.username(), command.email(), command.password(), command.firstName(), command.lastName());
        doctorRepository.save(doctor);
        log.info("Doctor updated for doctorId={}", id);
        return doctorMapper.toDto(doctor);
    }

    public DoctorDto getDoctorById(@NonNull Long id) {
        return doctorMapper.toDto(findDoctorByIdOrThrow(id));
    }

    public PageDto<DoctorDto> getDoctors(Long facilityId, Pageable pageable) {
        if (facilityId != null) {
            return PageDto.from(doctorRepository.findByFacilitiesId(facilityId, pageable)
                    .map(doctorMapper::toDto));
        }
        return PageDto.from(doctorRepository.findAll(pageable)
                .map(doctorMapper::toDto));
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
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
