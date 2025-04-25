package com.diabetesrisk.patient_service.controller;

import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.model.PatientDto;
import com.diabetesrisk.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/patients")
@Validated
@Tag(name = "Patient", description = "Patient API")
@SecurityRequirement(name = "basicAuth")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(
            summary = "Get all patients",
            description = "Get all patients",
            tags = {"Patient"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @GetMapping
    public ResponseEntity<List<Patient>> getPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @Operation(
            summary = "Get a patient by ID",
            description = "For a valid response try integer IDs between 1 and 4. Anything above 4 or below 1 will generate API errors",
            tags = {"Patient"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Patient> savePatient(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @Operation(
            summary = "Get a patient by ID for risk assessment",
            description = "For a valid response try integer IDs between 1 and 4. Anything above 4 or below 1 will generate API errors",
            tags = {"Patient"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
    })
    @GetMapping("/risk-assessment/{id}")
    public ResponseEntity<PatientDto> getPatientDto(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(patientService.getPatientDto(id));
    }

    @Operation(
            summary = "Create a patient",
            description = "Creates a patient. The patient must contain firstname, lastname, and other details.",
            tags = {"Patient"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PostMapping
    public ResponseEntity<Void> savePatient(@RequestBody @Valid Patient patient) {

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(patientService.savePatient(patient).getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(
            summary = "Update a patient",
            description = "Updates a patient. The patient must contain firstname, lastname, and other details.",
            tags = {"Patient"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@RequestBody @Valid Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(patient));
    }
}
