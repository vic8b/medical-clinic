package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.doctor.CreateDoctorCommand;
import com.vic8b.medicalclinic.command.doctor.CreateDoctorProfileCommand;
import com.vic8b.medicalclinic.command.doctor.UpdateDoctorCommand;
import com.vic8b.medicalclinic.dto.DoctorDto;
import com.vic8b.medicalclinic.dto.FacilityDto;
import com.vic8b.medicalclinic.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDto> getDoctors() {
        return doctorService.getDoctors();
    }

    @GetMapping("/{doctorId}")
    public DoctorDto getDoctorById(@PathVariable Long doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @GetMapping("/{doctorId}/facilities")
    public Set<FacilityDto> getFacilitiesForDoctor(@PathVariable Long doctorId) {
        return doctorService.getFacilities(doctorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@Valid @RequestBody CreateDoctorCommand command) {
        return doctorService.addDoctor(command);
    }

    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto createDoctorProfile(@PathVariable Long userId, @Valid @RequestBody CreateDoctorProfileCommand command) {
        return doctorService.createDoctorProfile(userId, command);
    }

    @PutMapping("/{doctorId}")
    public DoctorDto updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody UpdateDoctorCommand command) {
        return doctorService.updateDoctor(doctorId, command);
    }

    @PutMapping("/{doctorId}/facilities/{facilityId}")
    public DoctorDto assignFacilityToDoctor(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        return doctorService.assignFacilityToDoctor(doctorId, facilityId);
    }

    @DeleteMapping("/{doctorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable Long doctorId) {
        doctorService.removeDoctorById(doctorId);
    }
}
