package com.diabetesrisk.patient_service.controller;

import com.diabetesrisk.patient_service.TestUtils;
import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.model.PatientDto;
import com.diabetesrisk.patient_service.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest extends TestUtils {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
         mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    void getPatientsTest() throws Exception {
        Patient patient = createPatient();
        List<Patient> patients = List.of(patient);

        when(patientService.getPatients()).thenReturn(patients);

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patients)));
    }

    @Test
    void getPatientTest() throws Exception {
        Patient patient = createPatient();
        when(patientService.getPatient(anyInt())).thenReturn(patient);

        mockMvc.perform(get("/patients/" + patient.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patient)));
    }

    @Test
    void getPatientDtoTest() throws Exception {
        PatientDto patientDto = new PatientDto()
                .id(2)
                .age(30)
                .gender("F");

        when(patientService.getPatientDto(anyInt())).thenReturn(patientDto);

        mockMvc.perform(get("/patients/risk-assessment/" + patientDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patientDto)));
    }

    @Test
    void postGetPatientTest() throws Exception {
        Patient newPatient = createPatient();

        when(patientService.savePatient(any(Patient.class))).thenReturn(newPatient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/patients/" + newPatient.getId()));
    }

    @Test
    void updatePatientTest() throws Exception {
        Patient updatedPatient = createPatient();
        updatedPatient.setFirstname("Jane");

        when(patientService.updatePatient(any(Patient.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/patients/" + updatedPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedPatient)));
    }
}