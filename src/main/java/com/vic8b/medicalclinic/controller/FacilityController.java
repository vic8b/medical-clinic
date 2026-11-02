package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.facility.CreateFacilityCommand;
import com.vic8b.medicalclinic.dto.FacilityDoctorDto;
import com.vic8b.medicalclinic.dto.FacilityDto;
import com.vic8b.medicalclinic.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @GetMapping
    public List<FacilityDto> getFacilities() {
        return facilityService.getFacilities();
    }

    @GetMapping("/{facilityId}")
    public FacilityDto getFacilityById(@PathVariable Long facilityId) {
        return facilityService.getFacilityById(facilityId);
    }

    @GetMapping("/{facilityId}/doctors")
    public Set<FacilityDoctorDto> getDoctorsForFacility(@PathVariable Long facilityId) {
        return facilityService.getDoctors(facilityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDto addFacility(@Valid @RequestBody CreateFacilityCommand command) {
        return facilityService.addFacility(command);
    }

    @DeleteMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFacilityById(@PathVariable Long facilityId) {
        facilityService.removeFacilityById(facilityId);
    }
}
