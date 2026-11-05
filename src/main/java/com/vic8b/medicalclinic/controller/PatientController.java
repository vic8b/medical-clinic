package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.patient.CreatePatientCommand;
import com.vic8b.medicalclinic.command.patient.CreatePatientProfileCommand;
import com.vic8b.medicalclinic.command.patient.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.ErrorMessageDto;
import com.vic8b.medicalclinic.dto.patient.PatientDto;
import com.vic8b.medicalclinic.service.PatientService;
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

@Tag(name = "Patients", description = "Patients management endpoints")
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Get all patients")
    @ApiResponse(responseCode = "200", description = "Returned all patients")
    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getPatients();
    }

    @Operation(summary = "Get a patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @Operation(summary = "Create patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = """
                    Conflict:
                    - ID Card No is taken,
                    - username is taken,
                    - email is taken
                    """,
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@Valid @RequestBody CreatePatientCommand command) {
        return patientService.addPatient(command);
    }

    @Operation(summary = "Create patient profile for existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient profile for user created and assigned"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = """
                    Conflict:
                    - ID Card No is taken,
                    - User already has an existing patient profile
                    """,
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto createPatientProfile(@PathVariable Long userId, @Valid @RequestBody CreatePatientProfileCommand command) {
        return patientService.createPatientProfile(userId, command);
    }

    @Operation(summary = "Update patient profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "User with given update data already exists",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @Valid @RequestBody UpdatePatientCommand command) {
        return patientService.updatePatient(email, command);
    }

    @Operation(summary = "Remove a patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient has been removed"),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatientByEmail(email);
    }
}
