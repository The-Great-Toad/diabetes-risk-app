package com.diabetesrisk.patient_service.service;

import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.model.PatientDto;
import com.diabetesrisk.patient_service.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    @Override
    public List<Patient> getPatients() {
        return patientRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Patient getPatient(int id) {
        log.info("Retrieving patient with id {}...", id);

        if (patientRepository.findById(id).isEmpty()) {
            throwPatientNotFoundException(id);
        }

        log.info("Patient with id {} found, retrieving...", id);
        return patientRepository.findById(id).get();
    }

    @Override
    public Patient savePatient(Patient patient) {
        log.info("Saving patient {}", patient);
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        log.info("Updating patient {}", patient);

        if (!patientRepository.existsById(patient.getId())) {
            throwPatientNotFoundException(patient.getId());
        }

        log.info("Patient with id {} found, updating...", patient.getId());
        return patientRepository.save(patient);
    }

    @Override
    public PatientDto getPatientDto(Integer id) {
        log.info("Retrieving patient DTO with id {}...", id);

        if (patientRepository.findById(id).isEmpty()) {
            throwPatientNotFoundException(id);
        }

        log.info("Patient with id {} found, retrieving DTO...", id);
        return mapToPatientDto(patientRepository.findById(id).get());
    }

    /**
     * Map a Patient to a PatientDto.
     */
    private PatientDto mapToPatientDto(Patient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .age(calculatePatientAge(patient.getBirthDate()))
                .gender(patient.getGender());
    }

    /**
     * Calculate the age of a patient based on their birthdate.
     *
     * @param birthDate the birthdate of the patient
     * @return the age of the patient
     */
    private int calculatePatientAge(String birthDate) {
        log.info("Calculating patient age for {}", birthDate);

        LocalDate patientBirthDate = LocalDate.parse(birthDate);
        LocalDate now = LocalDate.now();
        int age = now.getYear() - patientBirthDate.getYear();

        if (now.getDayOfYear() < patientBirthDate.getDayOfYear()) {
            age--;
        }
        log.info("Patient birthdate {}, current date {} - Patient age is {}", patientBirthDate, now, age);

        return age;
    }

    private static void throwPatientNotFoundException(int id) {
        log.error("Patient with id {} not found", id);
        throw new EntityNotFoundException("Patient not found");
    }
}
