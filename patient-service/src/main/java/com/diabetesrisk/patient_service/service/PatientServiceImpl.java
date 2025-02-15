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

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
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
