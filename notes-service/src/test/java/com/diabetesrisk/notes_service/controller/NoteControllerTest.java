package com.diabetesrisk.notes_service.controller;

import com.diabetesrisk.notes_service.model.Note;
import com.diabetesrisk.notes_service.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @InjectMocks
    private NoteController noteController;

    @Mock
    private NoteService noteService;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .setMessageConverters()
                .build();
    }

    @Nested
    @DisplayName("getPatientNotes")
    class GetPatientNotes {

        @Test
        @DisplayName("Returns notes for a valid patient ID")
        void getPatientNotes_OK() throws Exception {
            int patientId = 1;
            List<Note> notes = List.of(
                    new Note(1, "Patient1", LocalDate.now(), "Note 1"),
                    new Note(1, "Patient1", LocalDate.now(), "Note 2")
            );

            when(noteService.getPatientNotes(patientId)).thenReturn(notes);

            mockMvc.perform(get("/notes/{patientId}", patientId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));

            verify(noteService, times(1)).getPatientNotes(patientId);
        }

        @Test
        @DisplayName("Returns 400 for invalid patient ID")
        void getPatientNotes_invalidPatientId() throws Exception {
            int patientId = -1;

            mockMvc.perform(get("/notes/{patientId}", patientId))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Returns empty list when no notes exist for patient ID")
        void getPatientNotes_noNotesFound() throws Exception {
            int patientId = 1;

            when(noteService.getPatientNotes(patientId)).thenReturn(List.of());

            mockMvc.perform(get("/notes/{patientId}", patientId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(noteService, times(1)).getPatientNotes(patientId);
        }
    }

    @Nested
    @DisplayName("addNote")
    class AddNote {

        @Test
        @DisplayName("Adds a valid note and returns it")
        void addNote_OK() throws Exception {
            Note note = new Note(1, "Patient1", LocalDate.now(), "New Note");

            when(noteService.addNote(any(Note.class))).thenReturn(note);

            mockMvc.perform(post("/notes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(note)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.patientId").value(1))
                    .andExpect(jsonPath("$.note").value("New Note"));

            verify(noteService, times(1)).addNote(any(Note.class));
        }

        @Test
        @DisplayName("Returns 400 for invalid note input")
        void addNote_invalidNote() throws Exception {
            Note invalidNote = new Note(-1, "", null, "");

            mockMvc.perform(post("/notes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(invalidNote)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(noteService, never()).addNote(any(Note.class));
        }
    }
}