package com.diabetesrisk.risk_assesment_service.service;

import com.diabetesrisk.risk_assesment_service.io.DiabetesTriggerReader;
import com.diabetesrisk.risk_assesment_service.model.Note;
import com.diabetesrisk.risk_assesment_service.model.Patient;
import com.diabetesrisk.risk_assesment_service.model.RiskLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiskAssessmentServiceTest {

    @InjectMocks
    private RiskAssessmentService riskAssessmentService;

    @Mock
    private DiabetesTriggerReader reader;

    private static MockWebServer patientMockServer;

    private static MockWebServer notesMockServer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        patientMockServer = new MockWebServer();
        patientMockServer.start();

        notesMockServer = new MockWebServer();
        notesMockServer.start();
    }

    @BeforeEach
    void init() {
        WebClient patientClient = WebClient.builder()
                .baseUrl("http://localhost:" + patientMockServer.getPort())
                .build();

        WebClient notesClient = WebClient.builder()
                .baseUrl("http://localhost:" + notesMockServer.getPort())
                .build();

        ReflectionTestUtils.setField(riskAssessmentService, "patientClient", patientClient);
        ReflectionTestUtils.setField(riskAssessmentService, "notesClient", notesClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        patientMockServer.shutdown();
        notesMockServer.shutdown();
    }

    @Nested
    @DisplayName("male patient under 30")
    class GetRiskAssessmentTest_maleUnder30 {
        @ParameterizedTest
        @ValueSource(strings = {
                "",
                "note without trigger",
                "note with 1 trigger : taille",
                "note with 2 triggers : taille, poids",
        })
        void shouldReturn_None(String note) throws JsonProcessingException {
            Patient patient = new Patient().id(1).age(29).gender("M");
            RiskLevel expected = RiskLevel.NONE;

            testRiskAssessment(note, patient, expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "note with 3 triggers : taille, poids, fumeur",
                "note with 4 triggers : taille, poids, fumeur, Anormal",
        })
        void shouldReturn_InDanger(String note) throws JsonProcessingException {
            Patient patient = new Patient().id(1).age(29).gender("M");
            RiskLevel expected = RiskLevel.IN_DANGER;

            testRiskAssessment(note, patient, expected);
        }

        @Test
        void shouldReturn_EarlyOnset() throws JsonProcessingException {
            Patient patient = new Patient().id(1).age(29).gender("M");
            String note = String.join(" ", getTriggers());
            RiskLevel expected = RiskLevel.EARLY_ONSET;

            testRiskAssessment(note, patient, expected);
        }
    }

    @Nested
    @DisplayName("female patient under 30")
    class GetRiskAssessmentTest_femaleUnder30 {
        @ParameterizedTest
        @ValueSource(strings = {
                "note without trigger",
                "note with 1 trigger : taille",
                "note with 2 triggers : taille, poids",
                "note with 3 triggers : taille, poids, fumeur",
        })
        void shouldReturn_None(String note) throws JsonProcessingException {
            Patient patient = new Patient().id(2).age(29).gender("F");
            RiskLevel expected = RiskLevel.NONE;

            testRiskAssessment(note, patient, expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "note with 4 triggers : taille, poids, fumeur, Anormal",
                "note with 5 triggers : taille, poids, fumeur, Anormal, Cholestérol",
                "note with 6 triggers : taille, poids, fumeur, Anormal, Cholestérol, Vertiges",
        })
        void shouldReturn_InDanger(String note) throws JsonProcessingException {
            Patient patient = new Patient().id(2).age(29).gender("F");
            RiskLevel expected = RiskLevel.IN_DANGER;

            testRiskAssessment(note, patient, expected);
        }

        @Test
        void shouldReturn_EarlyOnset() throws JsonProcessingException {
            Patient patient = new Patient().id(2).age(29).gender("F");
            String note = String.join(" ", getTriggers());
            RiskLevel expected = RiskLevel.EARLY_ONSET;

            testRiskAssessment(note, patient, expected);
        }
    }

    @Test
    void shouldThrowIllegalArgumentException_invalidGender() throws JsonProcessingException {
        Patient patient = new Patient().id(3).age(29).gender("S");
        String token = "token";
        String note = String.join(" ", getTriggers());

        mockClientsResponse(patient, note);

        when(reader.getTriggers()).thenReturn(getTriggers());

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> riskAssessmentService.getRiskAssessment(patient.getId(), token)
        );
    }

    @Nested
    @DisplayName("Patient over 30")
    class GetRiskAssessmentTest_patientOver30 {
        @ParameterizedTest
        @ValueSource(strings = {
                "note without trigger",
                "note with 1 trigger : taille",
        })
        void shouldReturn_None(String note) throws JsonProcessingException {
            Patient patient = new Patient().id(4).age(31).gender("M");
            RiskLevel expected = RiskLevel.NONE;

            testRiskAssessment(note, patient, expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "note with 2 triggers : taille, poids",
                "note with 3 triggers : taille, poids, fumeur",
                "note with 4 triggers : taille, poids, fumeur, Anormal",
                "note with 5 triggers : taille, poids, fumeur, Anormal, Cholestérol",
        })
        void shouldReturn_Borderline(String note) throws JsonProcessingException {
            Patient patient = new Patient().id(5).age(55).gender("F");
            RiskLevel expected = RiskLevel.BORDERLINE;

            testRiskAssessment(note, patient, expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "note with 6 triggers : taille, poids, fumeur, Anormal, Cholestérol, Vertiges",
                "note with 7 triggers : taille, poids, fumeur, Anormal, Cholestérol, Vertiges, Rechute",
        })
        void shouldReturn_InDanger(String note) throws JsonProcessingException {
            Patient patient = new Patient().id(6).age(69).gender("M");
            RiskLevel expected = RiskLevel.IN_DANGER;

            testRiskAssessment(note, patient, expected);
        }

        @Test
        void shouldReturn_EarlyOnset() throws JsonProcessingException {
            Patient patient = new Patient().id(7).age(72).gender("F");
            String note = String.join(" ", getTriggers());
            RiskLevel expected = RiskLevel.EARLY_ONSET;

            testRiskAssessment(note, patient, expected);
        }
    }

    @Test
    void shouldReturnNullOnNullPatient() throws JsonProcessingException {
        int patientId = 8;
        String token = "token";

        patientMockServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(null))
                .addHeader("Content-Type", "application/json"));

        RiskLevel result = riskAssessmentService.getRiskAssessment(patientId, token);

        assertNull(result);
    }

    private List<String> getTriggers() {
        return List.of(
                "Hémoglobine A1C",
                "Microalbumine",
                "Taille",
                "Poids",
                "Fumeur",
                "Fumeuse",
                "Anormal",
                "Cholestérol",
                "Vertiges",
                "Rechute",
                "Réaction",
                "Anticorps");
    }

    private void mockClientsResponse(Patient patient, String note) throws JsonProcessingException {
        List<Note> notes = Objects.equals("", note) ?
                Collections.emptyList() :
                List.of(new Note().note(note));

        patientMockServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(patient))
                .addHeader("Content-Type", "application/json"));

        notesMockServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(notes))
                .addHeader("Content-Type", "application/json"));
    }

    private void testRiskAssessment(String note, Patient patient, RiskLevel expected) throws JsonProcessingException {
        mockClientsResponse(patient, note);

        if (!Objects.equals("", note)) {
            when(reader.getTriggers()).thenReturn(getTriggers());
        }

        RiskLevel result = riskAssessmentService.getRiskAssessment(patient.getId(), "token");

        assertNotNull(result);
        assertEquals(expected, result);
    }
}