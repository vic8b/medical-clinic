package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.appointment.CreateAppointmentCommand;
import com.vic8b.medicalclinic.command.appointment.UpdateAppointmentCommand;
import com.vic8b.medicalclinic.dto.ErrorMessageDto;
import com.vic8b.medicalclinic.dto.appointment.AppointmentDto;
import com.vic8b.medicalclinic.service.AppointmentService;
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

@Tag(name = "Appointments", description = "Appointments management endpoints")
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Get all appointments, optionally filtered by doctor and patient")
    @ApiResponse(responseCode = "200", description = "Returned all appointments")
    @GetMapping
    public List<AppointmentDto> getAppointments(@RequestParam(required = false) Long doctorId,
                                                @RequestParam(required = false) Long patientId) {
        return appointmentService.getAppointments(doctorId, patientId);
    }

    @Operation(summary = "Get an appointment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class)))
    })
    @GetMapping("/{appointmentId}")
    public AppointmentDto getAppointmentById(@PathVariable Long appointmentId) {
        return appointmentService.getAppointmentById(appointmentId);
    }

    @Operation(summary = "Get all available appointments")
    @ApiResponse(responseCode = "200", description = "Returned all available appointments")
    @GetMapping("/available")
    public List<AppointmentDto> getAvailableAppointments() {
        return appointmentService.getAvailableAppointments();
    }

    @Operation(summary = "Create appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "400", description = """
                    Invalid request:
                    - validation failed,
                    - incorrect appointment start time,
                    - end time is before start time
                    """,
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "Appointment overlaps another doctor's appointment",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto addAppointment(@Valid @RequestBody CreateAppointmentCommand command) {
        return appointmentService.addAppointment(command);
    }

    @Operation(summary = "Assign patient to an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient assigned to an appointment"),
            @ApiResponse(responseCode = "404", description = "Appointment or patient not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = """
                    Conflict:
                    - appointment date has already passed,
                    - appointment is already booked
                    """,
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PatchMapping("/{appointmentId}/patient/{patientId}")
    public AppointmentDto assignPatient(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        return appointmentService.assignPatient(appointmentId, patientId);
    }

    @Operation(summary = "Cancel appointment for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment cancelled for a patient"),
            @ApiResponse(responseCode = "400", description = "Appointment has no patient",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Appointment not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
    })
    @PatchMapping("/{appointmentId}/cancel")
    public void cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointmentById(appointmentId);
    }

    @Operation(summary = "Update appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment updated"),
            @ApiResponse(responseCode = "400", description = """
                    Invalid request:
                    - validation failed,
                    - incorrect appointment start time,
                    - end time is before start time
                    """,
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Appointment or doctor not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "Appointment overlaps another doctor's appointment",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PutMapping("/{appointmentId}")
    public AppointmentDto updateAppointment(@PathVariable Long appointmentId, @Valid @RequestBody UpdateAppointmentCommand command) {
        return appointmentService.updateAppointment(appointmentId, command);
    }

    @Operation(summary = "Remove an appointment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment has been removed"),
            @ApiResponse(responseCode = "404", description = "Appointment not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @DeleteMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.removeAppointmentById(appointmentId);
    }
}
