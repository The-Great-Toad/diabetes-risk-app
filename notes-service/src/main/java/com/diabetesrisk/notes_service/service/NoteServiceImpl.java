package com.diabetesrisk.notes_service.service;

import com.diabetesrisk.notes_service.model.Note;
import com.diabetesrisk.notes_service.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> getPatientNotes(int patientId) {
        log.info("Fetching notes for patient ID: {}", patientId);
        List<Note> patientNotes = noteRepository.findByPatientId(patientId);

        if (patientNotes.isEmpty()) {
            log.info("No patient notes found for patient ID: {}", patientId);
            return new ArrayList<>();
        }

        /* Sort notes by date in descending order */
        log.info("Found {} notes for patient ID: {}", patientNotes.size(), patientId);
        patientNotes.sort(Comparator.comparing(Note::getDate).reversed());

        return patientNotes;
    }

    @Override
    public Note addNote(Note note) {
        log.info("Adding note: {}", note);
        return noteRepository.save(note);
    }
}