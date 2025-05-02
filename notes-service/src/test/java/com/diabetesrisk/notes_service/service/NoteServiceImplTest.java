package com.diabetesrisk.notes_service.service;

import com.diabetesrisk.notes_service.model.Note;
import com.diabetesrisk.notes_service.repository.NoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Nested
    @DisplayName("getPatientNotes")
    class GetPatientNotes {

        @Test
        @DisplayName("Returns empty list when no notes exist for patient")
        void getPatientNotesTest_emptyList() {
            int patientId = 1;
            when(noteRepository.findByPatientId(patientId)).thenReturn(Collections.emptyList());

            List<Note> result = noteService.getPatientNotes(patientId);

            assertEquals(0, result.size());
            verify(noteRepository, times(1)).findByPatientId(patientId);
        }

        @Test
        @DisplayName("Returns sorted notes by date in descending order")
        void getPatientNotesTest_sorted() {
            int patientId = 1;
            Note note1 = new Note(patientId, "Patient1", LocalDate.of(2024, 4, 12), "Note 1");
            Note note2 = new Note(patientId, "Patient1", LocalDate.of(2025, 4, 12), "Note 2");
            when(noteRepository.findByPatientId(patientId)).thenReturn(new ArrayList<>(List.of(note1, note2)));

            List<Note> result = noteService.getPatientNotes(patientId);

            assertEquals(2, result.size());
            assertEquals(note2, result.get(0));
            assertEquals(note1, result.get(1));
            verify(noteRepository, times(1)).findByPatientId(patientId);
        }
    }

    @Nested
    @DisplayName("addNote")
    class AddNote {

        @Test
        @DisplayName("Saves and returns the added note")
        void addNoteTest() {
            Note note = new Note(1, "Patient1", LocalDate.now(), "New Note");
            when(noteRepository.save(note)).thenReturn(note);

            Note result = noteService.addNote(note);

            assertEquals(note, result);
            verify(noteRepository, times(1)).save(note);
        }
    }
}