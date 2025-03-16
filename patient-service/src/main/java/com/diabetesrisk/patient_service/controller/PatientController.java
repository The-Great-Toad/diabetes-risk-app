package com.diabetesrisk.patient_service.controller;

import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/patients")
//@CrossOrigin
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Integer id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @PostMapping
    public ResponseEntity<Void> getPatient(@RequestBody Patient patient) {

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(patientService.savePatient(patient).getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(patient));
    }
}
