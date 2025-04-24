package com.diabetesrisk.risk_assesment_service.controller;

import com.diabetesrisk.risk_assesment_service.model.RiskLevel;
import com.diabetesrisk.risk_assesment_service.service.RiskAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/risk-assessment")
@Validated
@Tag(name = "Risk Assessment", description = "The risk assessment API")
@SecurityRequirement(name = "basicAuth")
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
    @Operation(
            summary = "Get the diabetes risk assessment for a patient",
            description = "For a valid response try integer IDs between 1 and 4. Anything above 4 or below 1 will generate API errors",
            tags = {"Risk Assessment"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Risk assessed"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @GetMapping("/{patientId}")
    public RiskLevel getRiskAssessment(@PathVariable @Min(1) int patientId) {
        return riskAssessmentService.getRiskAssessment(patientId);
    }
}
