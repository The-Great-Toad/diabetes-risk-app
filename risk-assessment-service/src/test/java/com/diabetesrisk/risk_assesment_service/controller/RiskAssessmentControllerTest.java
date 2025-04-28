package com.diabetesrisk.risk_assesment_service.controller;

import com.diabetesrisk.risk_assesment_service.model.RiskLevel;
import com.diabetesrisk.risk_assesment_service.service.RiskAssessmentService;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class RiskAssessmentControllerTest {

    @InjectMocks
    private RiskAssessmentController riskAssessmentController;

    @Mock
    private RiskAssessmentService riskAssessmentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(riskAssessmentController).build();
    }

    @Nested
    @DisplayName("getRiskAssessmentTest")
    public class getRiskAssessmentTest {
        @Test
        void shouldReturnRiskLevel_NONE() throws Exception {
            int patientId = 1;
            RiskLevel riskLevel = RiskLevel.NONE;
            String expectedResult = "NONE";

            when(riskAssessmentService.getRiskAssessment(anyInt(), anyString())).thenReturn(riskLevel);

            mockMvc.perform(get("/risk-assessment/{patientId}", patientId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expectedResult));
        }

        @Test
        void shouldReturnRiskLevel_BORDERLINE() throws Exception {
            int patientId = 1;
            RiskLevel riskLevel = RiskLevel.BORDERLINE;
            String expectedResult = "BORDERLINE";

            when(riskAssessmentService.getRiskAssessment(anyInt(), anyString())).thenReturn(riskLevel);

            mockMvc.perform(get("/risk-assessment/{patientId}", patientId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expectedResult));
        }

        @Test
        void shouldReturnRiskLevel_IN_DANGER() throws Exception {
            int patientId = 1;
            RiskLevel riskLevel = RiskLevel.IN_DANGER;
            String expectedResult = "IN_DANGER";

            when(riskAssessmentService.getRiskAssessment(anyInt(), anyString())).thenReturn(riskLevel);

            mockMvc.perform(get("/risk-assessment/{patientId}", patientId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expectedResult));
        }

        @Test
        void shouldReturnRiskLevel_EARLY_ONSET() throws Exception {
            int patientId = 1;
            RiskLevel riskLevel = RiskLevel.EARLY_ONSET;
            String expectedResult = "EARLY_ONSET";

            when(riskAssessmentService.getRiskAssessment(anyInt(), anyString())).thenReturn(riskLevel);

            mockMvc.perform(get("/risk-assessment/{patientId}", patientId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expectedResult));
        }


        @ParameterizedTest
        @ValueSource(ints = {-52, -1, 0})
        void shouldReturnBadRequestOnInvalidPatientId(int invalidPatientId) {
            ConstraintViolationException exception = new ConstraintViolationException("Invalid patient ID", null);

            when(riskAssessmentService.getRiskAssessment(anyInt(), anyString())).thenThrow(exception);

            assertThatExceptionOfType(ServletException.class)
                    .isThrownBy(() -> mockMvc
                                    .perform(get("/risk-assessment/{patientId}", invalidPatientId))
                                    .andDo(print())
                                    .andExpect(status().isBadRequest()))
                    .withMessageContaining("Invalid patient ID");
        }

    }
}