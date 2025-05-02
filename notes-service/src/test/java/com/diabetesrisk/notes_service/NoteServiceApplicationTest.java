package com.diabetesrisk.notes_service;

import com.diabetesrisk.notes_service.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(
        classes = NotesServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
public class NoteServiceApplicationTest {

    private static final String MONGO_DATABASE = "test_db";

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withEnv("MONGO_INITDB_DATABASE", MONGO_DATABASE);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final HttpHeaders headers = new HttpHeaders();

    @AfterAll
    static void tearDown() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void mongoDBProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        String mongoDBUri = String.format("mongodb://%s:%s/%s", mongoDBContainer.getHost(), mongoDBContainer.getFirstMappedPort(), MONGO_DATABASE);
        System.out.println("MongoDB URI: " + mongoDBUri);
        registry.add("spring.data.mongodb.uri", () -> mongoDBUri);

        headers.setBearerAuth("token");
        headers.set("X-User-Validated", "true");
    }

    @Test
    @DisplayName("should start MongoDB container")
    void connectionEstablished() {
        assertThat(mongoDBContainer.isCreated()).isTrue();
        assertThat(mongoDBContainer.isRunning()).isTrue();
    }

    @Nested
    @DisplayName("getPatientNotesIT")
    class getPatientNotesIT {

        @Test
        @DisplayName("should find 1 note for patient 1")
        void shouldFind1NoteForPatient1() throws Exception {
            mockMvc.perform(get("/notes/{patientId}", 1)
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        @DisplayName("should find 2 notes for patient 2")
        void shouldFind2NoteForPatient2() throws Exception {
            mockMvc.perform(get("/notes/{patientId}", 2)
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("should find 3 notes for patient 3")
        void shouldFind2NoteForPatient3() throws Exception {
            mockMvc.perform(get("/notes/{patientId}", 3)
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("should find 4 notes for patient 4")
        void shouldFind4NoteForPatient4() throws Exception {
            mockMvc.perform(get("/notes/{patientId}", 4)
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(4));
        }

        @Test
        @DisplayName("should find 0 notes for patient 5")
        void shouldFind0NoteForPatient5() throws Exception {
            mockMvc.perform(get("/notes/{patientId}", 5)
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("should return 400 for invalid patientId")
        void shouldReturn400ForInvalidPatientId() throws Exception {
            mockMvc.perform(get("/notes/{patientId}", -1)
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("addNoteIT")
    class addNoteIT {

        @Test
        @DisplayName("should add a note")
        @Rollback
        void shouldAddANote() throws Exception {
            Note note = new Note(6, "Test", LocalDate.now(), "Test note content");

            mockMvc.perform(post("/notes")
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(note)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.id").isString())
                    .andExpect(jsonPath("$.patientId").value(note.getPatientId()))
                    .andExpect(jsonPath("$.patient").value(note.getPatient()))
                    .andExpect(jsonPath("$.date").value(note.getDate().toString()))
                    .andExpect(jsonPath("$.note").value(note.getNote()));
        }

        @Test
        @DisplayName("should return 400 for invalid note")
        void shouldReturn400ForInvalidNote() throws Exception {
            Note note = new Note(null, "", LocalDate.now(), "");
            String expectedPatientIdError = "Patient ID cannot be null";
            String expectedPatientError = "Patient name cannot be empty";
            String expectedNoteError = "Note cannot be empty";

            mockMvc.perform(post("/notes")
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(note)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.patientId").value(expectedPatientIdError))
                    .andExpect(jsonPath("$.patient").value(expectedPatientError))
                    .andExpect(jsonPath("$.note").value(expectedNoteError));

            Note note2 = new Note(-1, "", LocalDate.now(), "");
            expectedPatientIdError = "Patient ID must be greater than 0";

            mockMvc.perform(post("/notes")
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(note2)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.patientId").value(expectedPatientIdError))
                    .andExpect(jsonPath("$.patient").value(expectedPatientError))
                    .andExpect(jsonPath("$.note").value(expectedNoteError));
        }
    }

}
