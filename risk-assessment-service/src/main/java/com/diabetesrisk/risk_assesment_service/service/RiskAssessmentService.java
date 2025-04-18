package com.diabetesrisk.risk_assesment_service.service;

import com.diabetesrisk.risk_assesment_service.io.DiabetesTriggerReader;
import com.diabetesrisk.risk_assesment_service.model.Note;
import com.diabetesrisk.risk_assesment_service.model.Patient;
import com.diabetesrisk.risk_assesment_service.model.RiskLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class RiskAssessmentService {

    private static final Logger log = LoggerFactory.getLogger(RiskAssessmentService.class);

    private final WebClient patientClient;

    private final WebClient notesClient;

    private final List<String> triggers;

    public RiskAssessmentService(WebClient patientClient, WebClient notesClient, DiabetesTriggerReader diabetesReader) {
        this.patientClient = patientClient;
        this.notesClient = notesClient;
        this.triggers = diabetesReader.getTriggers();
    }

    /**
     * This method calculates the risk assessment for a given patient ID.
     *
     * @param patientId The ID of the patient for whom to calculate the risk assessment.
     * @return A string representing the risk assessment result.
     */
    public RiskLevel getRiskAssessment(String patientId) {
        log.info("Fetching risk assessment for patient ID: {}", patientId);

        /* Fetch patient */
        Patient patient = getPatient(patientId);

        if (patient == null) {
            log.error("Patient not found for ID: {}", patientId);
            return null;
        }
        log.info("Patient details: {}", patient);

        /* Fetch notes */
        List<Note> notes = getNotes(patientId);

        if (notes == null) {
            log.error("No notes found for patient ID: {}", patientId);
            return null;
        }
        log.info("Notes for patient ID {}: {}", patientId, notes);

        /* Calculate risk assessment */
        return calculateRiskAssessment(patient, notes);
    }

    /**
     * This method fetches the patient details from the patient service.
     *
     * @param patientId The ID of the patient to fetch.
     * @return The Patient object containing the patient's details.
     */
    private Patient getPatient(String patientId) {
        log.info("Fetching patient details for ID: {}", patientId);
        String uri = String.format("/patients/risk-assessment/%s", patientId);

        return patientClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    /**
     * This method fetches the notes for a given patient ID from the note service.
     *
     * @param patientId The ID of the patient whose notes to fetch.
     * @return A list of Note objects containing the patient's notes.
     */
    private List<Note> getNotes(String patientId) {
        return notesClient.get()
                .uri("/notes/" + patientId)
                .retrieve()
                .bodyToFlux(Note.class)
                .collectList()
                .block();
    }

    /**
     * This method calculates the risk assessment based on the patient's details and notes.
     *
     * @param patient The Patient object containing the patient's details.
     * @param notes   A list of Note objects containing the patient's notes.
     * @return A RiskAssessment object containing the calculated risk assessment.
     */
    private RiskLevel calculateRiskAssessment(Patient patient, List<Note> notes) {
        if (notes.isEmpty()) {
            log.info("No notes found for patient ID: {}", patient.getId());
            return RiskLevel.NONE;
        }

        RiskLevel riskLevel;
        int triggerCount = calculateTriggerCount(notes);

        if (patient.getAge() <= 30) {
            log.info("Patient age: {} is under 30 years", patient.getAge());
            switch (patient.getGender()) {
                case "M" -> {
                    switch (triggerCount) {
                        case 0,1,2 -> riskLevel = RiskLevel.NONE;
                        case 3,4 -> riskLevel = RiskLevel.IN_DANGER;
                        default -> riskLevel = RiskLevel.EARLY_ONSET;
                    }
                }
                case "F" -> {
                    switch (triggerCount) {
                        case 0,1,2,3 -> riskLevel = RiskLevel.NONE;
                        case 4,5,6 -> riskLevel = RiskLevel.IN_DANGER;
                        default -> riskLevel = RiskLevel.EARLY_ONSET;
                    }
                }
                default -> throw new IllegalArgumentException("Invalid gender");
            }
        } else {
            log.info("Patient age: {} is above 30 years", patient.getAge());
            switch (triggerCount) {
                case 0 -> riskLevel = RiskLevel.NONE;
                case 2,3,4,5 -> riskLevel = RiskLevel.BORDERLINE;
                case 6,7 -> riskLevel = RiskLevel.IN_DANGER;
                default -> riskLevel = RiskLevel.EARLY_ONSET;
            }
        }
        log.info("Calculated risk level: {}", riskLevel);

        return riskLevel;
    }

    /**
     * This method calculates the number of triggers found in the patient's notes.
     *
     * @param notes A list of Note objects containing the patient's notes.
     * @return The count of triggers found in the notes.
     */
    private int calculateTriggerCount(List<Note> notes) {
        int triggerCount = 0;
        for (Note note : notes) {
            List<String> noteWords = List.of(note.getNote().split(" "));
            log.info("Note words: {}", noteWords);
            for (String word : noteWords) {
                // Normalize the word by removing comma and converting to lowercase
                word = word.replaceAll(",", "").toLowerCase();
                log.debug("Normalized word: {}", word);
                // Check if the word is a trigger
                if (triggers.contains(word)) {
                    log.info("Trigger found: {}", word);
                    triggerCount++;
                }
            }
        }
        log.info("Trigger count: {}", triggerCount);
        return triggerCount;
    }
}
