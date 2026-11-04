package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.appointment.CreateAppointmentCommand;
import com.vic8b.medicalclinic.command.appointment.UpdateAppointmentCommand;
import com.vic8b.medicalclinic.dto.appointment.AppointmentDto;
import com.vic8b.medicalclinic.dto.appointment.AppointmentForDoctorDto;
import com.vic8b.medicalclinic.dto.appointment.AppointmentForPatientDto;
import com.vic8b.medicalclinic.dto.appointment.AvailableAppointmentDto;
import com.vic8b.medicalclinic.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public List<AppointmentDto> getAppointments() {
        return appointmentService.getAppointments();
    }

    @GetMapping("/{appointmentId}")
    public AppointmentDto getAppointmentsById(@PathVariable Long appointmentId) {
        return appointmentService.getAppointmentById(appointmentId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<AppointmentForDoctorDto> getAppointmentsForDoctor(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsForDoctor(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<AppointmentForPatientDto> getAppointmentsForPatient(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsForPatient(patientId);
    }

    @GetMapping("/available")
    public List<AvailableAppointmentDto> getAvailableAppointments() {
        return appointmentService.getAvailableAppointments();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto addAppointment(@Valid @RequestBody CreateAppointmentCommand command) {
        return appointmentService.addAppointment(command);
    }

    @PatchMapping("/{appointmentId}/patient/{patientId}")
    public AppointmentDto assignPatient(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        return appointmentService.assignPatient(appointmentId, patientId);
    }

    @PatchMapping("/cancel/{appointmentId}")
    public void cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointmentById(appointmentId);
    }

    @PutMapping("/{appointmentId}")
    public AppointmentDto updateAppointment(@PathVariable Long appointmentId, @Valid @RequestBody UpdateAppointmentCommand command) {
        return appointmentService.updateAppointment(appointmentId, command);
    }

    @DeleteMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.removeAppointmentById(appointmentId);
    }
}
