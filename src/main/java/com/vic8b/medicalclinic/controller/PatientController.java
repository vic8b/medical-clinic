package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.UpdatePatientCommand;
import com.vic8b.medicalclinic.model.Patient;
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
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/search")
    public Patient getPatientByEmail(@RequestParam String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @PutMapping
    public Patient updatePatient(@RequestBody UpdatePatientCommand command) {
        return patientService.updatePatient(command);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatientByEmail(email);
    }
}
