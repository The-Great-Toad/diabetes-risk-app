package com.diabetesrisk.patient_service.service;

import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);
    private static final String LOG_ID = "[PatientService]";

    private final PatientRepository patientRepository;

    private static final List<Patient> patients;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        patientRepository.saveAll(patients);
    }

    static {
        LOGGER.info(LOG_ID + " - Patient Service Initialized");
        Patient patient = Patient.builder()
                .lastName("TestNone")
                .firstName("Test")
                .birthDate("1966-12-31")
                .gender("F")
                .address("1 Brookside St")
                .phoneNumber("100-222-3333")
                .build();

        Patient patient2 = Patient.builder()
                .lastName("TestBorderline")
                .firstName("Test")
                .birthDate("1945-06-24")
                .gender("M")
                .address("2 High St")
                .phoneNumber("200-333-4444")
                .build();

        Patient patient3 = Patient.builder()
                .lastName("TestInDanger")
                .firstName("Test")
                .birthDate("2004-06-18")
                .gender("M")
                .address("3 Club Road")
                .phoneNumber("300-444-5555")
                .build();

        Patient patient4 = Patient.builder()
                .lastName("TestEarlyOnset")
                .firstName("Test")
                .birthDate("2002-06-28")
                .gender("F")
                .address("4 Valley Dr")
                .phoneNumber("400-555-6666")
                .build();

        patients = List.of(patient, patient2, patient3, patient4);
    }

    @Override
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatient(int id) {
        /* Error */
        if (patientRepository.findById(id).isEmpty()) {
            throwPatientNotFoundExcetion(Integer.toString(id));
        }

        return patientRepository.findById(id).get();
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        if (Objects.isNull(patient.getId())) {
            throwPatientNotFoundExcetion(patient.getFullName());
        }
        return patientRepository.save(patient);
    }

    private void throwPatientNotFoundExcetion(String identification) throws EntityNotFoundException {
        String error;
        try {
            int id = Integer.parseInt(identification);
            error = String.format("%s - Patient with id %d not found", LOG_ID, id);

        } catch (NumberFormatException e) {
            error = String.format("%s - Patient, %s, doesn't exist", LOG_ID,identification);
        }

        LOGGER.error(error);
        throw new EntityNotFoundException(error);
    }
}
