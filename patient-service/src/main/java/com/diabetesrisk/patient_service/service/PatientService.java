package com.diabetesrisk.patient_service.service;

import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.model.PatientDto;

import java.util.List;

public interface PatientService {

    /**
     * Get all patients.
     *
     * @return List of patients
     */
    List<Patient> getPatients();

    /**
     * Get a patient by ID.
     *
     * @param id the ID of the patient
     * @return the patient
     */
    Patient getPatient(int id);

    /**
     * Save a patient.
     *
     * @param patient the patient to save
     * @return the saved patient
     */
    Patient savePatient(Patient patient);

    /**
     * Update a patient.
     *
     * @param patient the patient to update
     * @return the updated patient
     */
    Patient updatePatient(Patient patient);

    /**
     * Get a patient DTO by ID.
     *
     * @param id the ID of the patient
     * @return the patient DTO
     */
    PatientDto getPatientDto(Integer id);
}
