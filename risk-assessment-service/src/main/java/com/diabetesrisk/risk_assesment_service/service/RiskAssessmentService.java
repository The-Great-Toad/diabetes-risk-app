package com.diabetesrisk.risk_assesment_service.service;

import com.diabetesrisk.risk_assesment_service.io.DiabetesTriggerReader;
import com.diabetesrisk.risk_assesment_service.model.Note;
import com.diabetesrisk.risk_assesment_service.model.Patient;
import com.diabetesrisk.risk_assesment_service.model.RiskLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class RiskAssessmentService {

    private static final Logger log = LoggerFactory.getLogger(RiskAssessmentService.class);

    private final WebClient patientClient;

    private final WebClient notesClient;

    private final DiabetesTriggerReader diabetesReader;


    public RiskAssessmentService(WebClient patientClient, WebClient notesClient, DiabetesTriggerReader diabetesReader) {
        this.patientClient = patientClient;
        this.notesClient = notesClient;
        this.diabetesReader = diabetesReader;
    }

    /**
     * This method calculates the risk assessment for a given patient ID.
     *
     * @param patientId The ID of the patient for whom to calculate the risk assessment.
     * @param token     The JWT token for authentication.
     * @return A string representing the risk assessment result.
     */
    public RiskLevel getRiskAssessment(int patientId, String token) {
        log.info("Fetching risk assessment for patient ID: {}", patientId);

        /* Fetch patient */
        Patient patient = getPatient(patientId, token);

        if (Objects.isNull(patient)) {
            log.error("Patient not found for ID: {}", patientId);
            return null;
        }
        log.info("Patient details: {}", patient);

        /* Fetch notes */
        List<Note> notes = getNotes(patientId, token);

        if (Objects.isNull(notes)) {
            log.error("No notes found for patient ID: {}", patientId);
            return null;
        } else if (notes.isEmpty()) {
            log.info("Empty notes for patient ID: {}", patientId);
            return RiskLevel.NONE;
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
    private Patient getPatient(int patientId, String token) {
        HttpHeaders headers = setWebClientHeaders(token);

        log.info("Fetching patient details for ID: {}", patientId);
        String uri = String.format("/patients/risk-assessment/%s", patientId);

        return patientClient.get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
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
    private List<Note> getNotes(int patientId, String token) {
        HttpHeaders headers = setWebClientHeaders(token);

        return notesClient.get()
                .uri("/notes/" + patientId)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToFlux(Note.class)
                .collectList()
                .block();
    }

    private HttpHeaders setWebClientHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("X-User-Validated", "true");
        return headers;
    }

    /**
     * This method calculates the risk assessment based on the patient's details and notes.
     *
     * @param patient The Patient object containing the patient's details.
     * @param notes   A list of Note objects containing the patient's notes.
     * @return A RiskAssessment object containing the calculated risk assessment.
     */
    private RiskLevel calculateRiskAssessment(Patient patient, List<Note> notes) {
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
                default -> {
                    final String errorMsg = String.format("Invalid gender: %s", patient.getGender());
                    log.error(errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
            }
        } else {
            log.info("Patient age: {} is above 30 years", patient.getAge());
            switch (triggerCount) {
                case 0,1 -> riskLevel = RiskLevel.NONE;
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
        List<String> triggers = diabetesReader.getTriggers()
                .stream()
                .map(String::toLowerCase)
                .toList();
        log.info("Triggers: {}", triggers);

        return (int) notes.stream()
                .flatMap(note -> extractWordsFromNote(note.getNote()).stream())
                .filter(word -> isTriggerWord(word, triggers))
                .count();
    }

    /**
     * This method checks if a given word is a trigger word.
     *
     * @param word    The word to check.
     * @param triggers A list of trigger words.
     * @return True if the word is a trigger, false otherwise.
     */
    private static boolean isTriggerWord(String word, List<String> triggers) {
        boolean isTriggerWord = triggers.contains(word.toLowerCase());
        if (isTriggerWord) {
            log.info("Trigger found: {}", word);
        }
        return isTriggerWord;
    }

    /**
     * This method extracts words from a note and normalizes them.
     * The normalization process includes removing extra spaces, new line, comma, and converting to lowercase.
     *
     * @param noteContent The content of the note to extract words from.
     * @return A list of normalized words extracted from the note.
     */
    private List<String> extractWordsFromNote(String noteContent) {
        String normalizedNote = noteContent.replaceAll(",", " ").replaceAll("\\s+", " ").trim();
        log.info("Normalized note: {}", normalizedNote);
        return List.of(normalizedNote.toLowerCase().split(" "));
    }
}
