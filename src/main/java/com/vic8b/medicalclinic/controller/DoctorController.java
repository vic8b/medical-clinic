package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.doctor.AssignDoctorToUserCommand;
import com.vic8b.medicalclinic.command.doctor.CreateDoctorCommand;
import com.vic8b.medicalclinic.command.doctor.UpdateDoctorCommand;
import com.vic8b.medicalclinic.dto.ErrorMessageDto;
import com.vic8b.medicalclinic.dto.doctor.DoctorDetailsDto;
import com.vic8b.medicalclinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Doctors", description = "Doctors management endpoints")
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Get all doctors")
    @ApiResponse(responseCode = "200", description = "Returned all doctors")
    @GetMapping
    public List<DoctorDetailsDto> getDoctors() {
        return doctorService.getDoctors();
    }

    @Operation(summary = "Get a doctor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class)))
    })
    @GetMapping("/{doctorId}")
    public DoctorDetailsDto getDoctorById(@PathVariable Long doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @Operation(summary = "Create doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = """
                    Conflict - user already exists:
                    - username is taken,
                    - email is taken
                    """,
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDetailsDto addDoctor(@Valid @RequestBody CreateDoctorCommand command) {
        return doctorService.addDoctor(command);
    }

    @Operation(summary = "Assign doctor profile to existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor profile for user created and assigned"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = """
                    Conflict:
                    - User already has an existing doctor profile
                    """,
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PostMapping("/assign/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDetailsDto assignDoctorToUser(@PathVariable Long userId, @Valid @RequestBody AssignDoctorToUserCommand command) {
        return doctorService.assignDoctorToUser(userId, command);
    }

    @Operation(summary = "Update doctor profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "User with given update data already exists",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PutMapping("/{doctorId}")
    public DoctorDetailsDto updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody UpdateDoctorCommand command) {
        return doctorService.updateDoctor(doctorId, command);
    }

    @Operation(summary = "Assign facility to a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facility successfully assigned to a doctor"),
            @ApiResponse(responseCode = "404", description = "Doctor or facility not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PutMapping("/{doctorId}/facilities/{facilityId}")
    public DoctorDetailsDto assignFacilityToDoctor(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        return doctorService.assignFacilityToDoctor(doctorId, facilityId);
    }

    @Operation(summary = "Remove a doctor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor has been removed"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @DeleteMapping("/{doctorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable Long doctorId) {
        doctorService.removeDoctorById(doctorId);
    }
}
