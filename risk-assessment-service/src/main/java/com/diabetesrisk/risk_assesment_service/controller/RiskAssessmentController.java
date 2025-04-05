package com.diabetesrisk.risk_assesment_service.controller;

import com.diabetesrisk.risk_assesment_service.model.RiskLevel;
import com.diabetesrisk.risk_assesment_service.service.RiskAssessmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/risk-assessment")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    /**
     * Get the diabetes risk assessment for a patient
     *
     * @param patientId the ID of the patient
     * @return the risk assessment for the patient
     */
    @GetMapping("/{patientId}")
    public RiskLevel getRiskAssessment(@PathVariable String patientId) {
        return riskAssessmentService.getRiskAssessment(patientId);
    }
}
