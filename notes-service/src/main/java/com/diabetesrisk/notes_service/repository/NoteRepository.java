package com.diabetesrisk.notes_service.repository;

import com.diabetesrisk.notes_service.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatientId(Integer patientId);
}
