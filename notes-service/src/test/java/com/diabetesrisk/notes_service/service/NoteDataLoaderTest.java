package com.diabetesrisk.notes_service.service;

import com.diabetesrisk.notes_service.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteDataLoaderTest {

    @InjectMocks
    private NoteDataLoader noteDataLoader;

    @Mock
    private NoteRepository noteRepository;

    @Test
    void run() {
        when(noteRepository.count()).thenReturn(0L);

        noteDataLoader.run();

        verify(noteRepository, times(1)).count();
        verify(noteRepository, times(1)).saveAll(anyList());
    }
}