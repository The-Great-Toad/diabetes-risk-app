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
        noteRepository.deleteAll();
        noteRepository.saveAll(notes);
    }

    static {
        log.info("{} - NoteService initialized", LOG_PREFIX);

        Note note1 = Note.builder()
                .patientId(1)
                .patient("TestNone")
                .date(Date.from(OffsetDateTime.now().minusDays(35).toInstant()))
                .note("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé");
        Note note2 = Note.builder()
                .patientId(2)
                .patient("TestBorderline")
                .date(Date.from(OffsetDateTime.now().minusDays(35).toInstant()))
                .note("Le patient déclare qu'il ressent beaucoup de stress au travail.\nIl se plaint également que son audition est anormale dernièrement");
        Note note3 = Note.builder()
                .patientId(2)
                .patient("TestBorderline")
                .date(Date.from(OffsetDateTime.now().minusDays(90).toInstant()))
                .note("Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois.\nIl remarque également que son audition continue d'être anormale");
        Note note4 = Note.builder()
                .patientId(3)
                .patient("TestInDanger")
                .date(Date.from(OffsetDateTime.now().minusDays(35).toInstant()))
                .note("Le patient déclare qu'il fume depuis peu");
        Note note5 = Note.builder()
                .patientId(3)
                .patient("TestInDanger")
                .date(Date.from(OffsetDateTime.now().minusDays(90).toInstant()))
                .note("Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière.\nIl se plaint également de crises d’apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé");
        Note note6 = Note.builder()
                .patientId(4)
                .patient("TestEarlyOnset")
                .date(Date.from(OffsetDateTime.now().minusDays(35).toInstant()))
                .note("Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments");
        Note note7 = Note.builder()
                .patientId(4)
                .patient("TestEarlyOnset")
                .date(Date.from(OffsetDateTime.now().minusDays(150).toInstant()))
                .note("Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps");
        Note note8 = Note.builder()
                .patientId(4)
                .patient("TestEarlyOnset")
                .date(Date.from(OffsetDateTime.now().minusDays(350).toInstant()))
                .note("Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé");
        Note note9 = Note.builder()
                .patientId(4)
                .patient("TestEarlyOnset")
                .date(Date.from(OffsetDateTime.now().minusDays(450).toInstant()))
                .note("Taille, Poids, Cholestérol, Vertige et Réaction");

        notes.addAll(List.of(note1, note2, note3, note4, note5, note6, note7, note8, note9));
    }

    public List<Note> getPatientNotes(int patientId) {
        log.info("{} - Fetching notes for patient ID: {}", LOG_PREFIX, patientId);
        List<Note> notes = noteRepository.findByPatientId(patientId);
        log.info("{} - Found {} notes for patient ID: {}", LOG_PREFIX, notes.size(), patientId);
        notes.sort(Comparator.comparing(Note::getDate));
        return notes;
    }

    public Note addNote(Note note) {
        log.info("{} - Adding note: {}", LOG_PREFIX, note);
        return noteRepository.save(note);
    }
}
