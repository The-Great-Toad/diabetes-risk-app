package com.diabetesrisk.notes_service.service;

import com.diabetesrisk.notes_service.model.Note;

import java.util.List;

public interface NoteService {

    /**
     * Get all notes for a patient
     *
     * @param patientId the id of the patient
     * @return a list of notes
     */
    List<Note> getPatientNotes(int patientId);

    /**
     * Add a note for a patient
     *
     * @param note the note to add
     * @return the added note
     */
    Note addNote(Note note);
}
