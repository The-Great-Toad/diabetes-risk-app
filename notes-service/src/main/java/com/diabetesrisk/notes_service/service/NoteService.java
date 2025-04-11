package com.diabetesrisk.notes_service.service;

import com.diabetesrisk.notes_service.model.Note;
import com.diabetesrisk.notes_service.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteService.class);
    private static final String LOG_PREFIX = "[NoteService]";

    private final NoteRepository noteRepository;

    private static final List<Note> notes = new ArrayList<>();

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
        initializeNotes();
    }

    private void initializeNotes() {
        noteRepository.deleteAll();
        createNote();
        noteRepository.saveAll(notes);
    }

    private static void createNote() {
        notes.addAll(List.of(
                new Note(1, "TestNone", Date.from(OffsetDateTime.now().minusDays(35).toInstant()), "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé"),
                new Note(2, "TestBorderline", Date.from(OffsetDateTime.now().minusDays(35).toInstant()), "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement"),
                new Note(2, "TestBorderline", Date.from(OffsetDateTime.now().minusDays(90).toInstant()), "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale"),
                new Note(3, "TestInDanger", Date.from(OffsetDateTime.now().minusDays(35).toInstant()), "Le patient déclare qu'il fume depuis peu"),
                new Note(3, "TestInDanger", Date.from(OffsetDateTime.now().minusDays(90).toInstant()), "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d’apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé"),
                new Note(4, "TestEarlyOnset", Date.from(OffsetDateTime.now().minusDays(35).toInstant()), "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments"),
                new Note(4, "TestEarlyOnset", Date.from(OffsetDateTime.now().minusDays(90).toInstant()), "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps"),
                new Note(4, "TestEarlyOnset", Date.from(OffsetDateTime.now().minusDays(150).toInstant()), "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé"),
                new Note(4, "TestEarlyOnset", Date.from(OffsetDateTime.now().minusDays(300).toInstant()), "Taille, Poids, Cholestérol, Vertige et Réaction")
        ));
    }

    public List<Note> getPatientNotes(int patientId) {
        log.info("{} - Fetching notes for patient ID: {}", LOG_PREFIX, patientId);
        List<Note> patientNotes = noteRepository.findByPatientId(patientId);
        log.info("{} - Found {} notes for patient ID: {}", LOG_PREFIX, patientNotes.size(), patientId);
        patientNotes.sort(Comparator.comparing(Note::getDate).reversed());
        return patientNotes;
    }

    public Note addNote(Note note) {
        log.info("{} - Adding note: {}", LOG_PREFIX, note);
        return noteRepository.save(note);
    }
}