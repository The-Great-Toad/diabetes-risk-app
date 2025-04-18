package com.diabetesrisk.notes_service.controller;

import com.diabetesrisk.notes_service.model.Note;
import com.diabetesrisk.notes_service.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Note", description = "The note API")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping(value = "/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(
            summary = "Get all notes for a patient",
            description = "For a valid response try integer IDs between 1 and 4. Anything above 4 or below 1 will generate API errors",
            tags = {"Note"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @GetMapping("/{patientId}")
    public List<Note> getPatientNotes(@Min(1) @PathVariable int patientId) {
        return noteService.getPatientNotes(patientId);
    }

    @Operation(
            summary = "Add a note for a patient",
            description = "Adds a note for a patient. The note must contain a valid patient ID, a patient name and text for the note.",
            tags = {"Note"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note addNote(@Valid @RequestBody Note note) {
        return noteService.addNote(note);
    }

}
