package com.diabetesrisk.notes_service.controller;

import com.diabetesrisk.notes_service.model.Note;
import com.diabetesrisk.notes_service.service.NoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{patientId}")
    public List<Note> getPatientNotes(@PathVariable int patientId) {
        return noteService.getPatientNotes(patientId);
    }

    @PostMapping
    public Note addNote(@RequestBody Note note) {
        return noteService.addNote(note);
    }

}
