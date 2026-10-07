package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.ChangePasswordCommand;
import com.vic8b.medicalclinic.command.CreatePatientCommand;
import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.dto.PatientDto;
import com.vic8b.medicalclinic.service.PatientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    @NonNull
    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody CreatePatientCommand command) {
        return patientService.addPatient(command);
    }

    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @RequestBody UpdatePatientCommand command) {
        return patientService.updatePatient(email, command);
    }

    @PatchMapping("/{email}/password")
    public PatientDto changePassword(@PathVariable String email, @RequestBody ChangePasswordCommand command) {
        return patientService.changePassword(email, command);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatientByEmail(email);
    }
}
