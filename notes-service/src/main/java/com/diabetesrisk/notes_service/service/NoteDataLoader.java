package com.diabetesrisk.notes_service.service;

import com.diabetesrisk.notes_service.model.Note;
import com.diabetesrisk.notes_service.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class NoteDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(NoteDataLoader.class);

    private static final List<Note> notes = new ArrayList<>();

    private final NoteRepository noteRepository;

    public NoteDataLoader(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) {
        /* Initializes the notes in the database if none exist. */
        if (noteRepository.count() == 0) {
            log.info("No notes found in the database. Initializing with sample data.");
            createNote();
            noteRepository.saveAll(notes);
            log.info("Sample notes have been added to the database.");
        }
    }

    private static void createNote() {
        log.info("Creating new notes...");
        notes.addAll(List.of(
                new Note(1, "TestNone", LocalDate.of(2025, 4, 12), "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé"),
                new Note(2, "TestBorderline", LocalDate.of(2025, 4, 12), "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement"),
                new Note(2, "TestBorderline", LocalDate.of(2025, 1, 12), "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale"),
                new Note(3, "TestInDanger", LocalDate.of(2025, 4, 12), "Le patient déclare qu'il fume depuis peu"),
                new Note(3, "TestInDanger", LocalDate.of(2025, 1, 12), "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d’apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé"),
                new Note(4, "TestEarlyOnset", LocalDate.of(2025, 4, 12), "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments"),
                new Note(4, "TestEarlyOnset", LocalDate.of(2025, 1, 12), "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps"),
                new Note(4, "TestEarlyOnset", LocalDate.of(2024, 10, 12), "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé"),
                new Note(4, "TestEarlyOnset", LocalDate.of(2024, 5, 12), "Taille, Poids, Cholestérol, Vertige et Réaction")
        ));
        log.info("{} notes created", notes.size());
    }
}
