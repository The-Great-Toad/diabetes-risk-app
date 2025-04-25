package com.diabetesrisk.patient_service.service;

import com.diabetesrisk.patient_service.TestUtils;
import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.model.PatientDto;
import com.diabetesrisk.patient_service.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest extends TestUtils {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    @Test
    void getPatientsTest() {
        Patient patient = createPatient();
        List<Patient> patients = List.of(patient);

        when(patientRepository.findAll(any(Sort.class))).thenReturn(patients);

        List<Patient> result = patientService.getPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Nested
    @DisplayName("getPatient")
    class getPatientTest {

        @Test
        void patientFound() {
            Patient patient = createPatient();

            when(patientRepository.findById(anyInt())).thenReturn(Optional.of(patient));

            Patient result = patientService.getPatient(patient.getId());

            assertNotNull(result);
            assertEquals(patient, result);
        }

        @Test
        void patientNotFound() {
            when(patientRepository.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () -> patientService.getPatient(1));
            assertEquals("Patient not found", exception.getMessage());
        }
    }



    @Test
    void savePatientTest() {
        Patient patient = createPatient();

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.savePatient(patient);

        assertNotNull(result);
        assertEquals(patient, result);
    }

    @Nested
    @DisplayName("updatePatient")
    class updatePatientTest {

        @Test
        void patientFound() {
            Patient patient = createPatient();

            when(patientRepository.existsById(anyInt())).thenReturn(true);
            when(patientRepository.save(any(Patient.class))).thenReturn(patient);

            Patient result = patientService.updatePatient(patient);

            assertNotNull(result);
            assertEquals(patient, result);
        }

        @Test
        void patientNotFound() {
            Patient patient = createPatient();

            Exception exception = assertThrows(EntityNotFoundException.class, () -> patientService.updatePatient(patient));
            assertEquals("Patient not found", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("getPatientDto")
    class getPatientDtoTest {

        @Test
        void patientFound() {
            Patient patient = createPatient();
            int age = calculatePatientAge(patient.getBirthDate());

            when(patientRepository.findById(anyInt())).thenReturn(Optional.of(patient));

            PatientDto result = patientService.getPatientDto(1);

            assertNotNull(result);
            assertEquals(patient.getId(), result.getId());
            assertEquals(age, result.getAge());
            assertEquals(patient.getGender(), result.getGender());
        }

        @Test
        void patientNotFound() {
            when(patientRepository.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () -> patientService.getPatientDto(1));
            assertEquals("Patient not found", exception.getMessage());
        }
    }


}