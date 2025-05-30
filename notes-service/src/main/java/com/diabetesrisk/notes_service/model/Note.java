package com.diabetesrisk.notes_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "notes")
public class Note {

    @Schema(description = "Unique identifier for the note", example = "123-456-789")
    @Id
    private String id;

    @Schema(description = "Unique identifier for the patient", example = "1")
    @NotNull(message = "Patient ID cannot be null")
    @Min(value = 1, message = "Patient ID must be greater than 0")
    private Integer patientId;

    @Schema(description = "Name of the patient", example = "John Doe")
    @NotEmpty(message = "Patient name cannot be empty")
    private String patient;

    @Schema(description = "Date of the note", example = "2023-10-01")
    private LocalDate date;

    @Schema(description = "Content of the note", example = "Patient is doing well.")
    @NotEmpty(message = "Note cannot be empty")
    private String note;

    public Note() {
    }

    public Note(Integer patientId, String patient, LocalDate date, String note) {
        this.patientId = patientId;
        this.patient = patient;
        this.date = date;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Note{");
        sb.append("patientId=").append(patientId);
        sb.append(", patient='").append(patient).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Note builder() {
        return new Note();
    }

    public Note patientId(Integer patId) {
        this.patientId = patId;
        return this;
    }

    public Note patient(String patient) {
        this.patient = patient;
        return this;
    }

    public Note date(LocalDate date) {
        this.date = date;
        return this;
    }

    public Note note(String note) {
        this.note = note;
        return this;
    }
}
