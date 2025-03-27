package com.diabetesrisk.patient_service.service;

import com.diabetesrisk.patient_service.model.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getPatients();

    Patient getPatient(int id);

    Patient savePatient(Patient patient);

    Patient updatePatient(Patient patient);
}
