package com.diabetesrisk.patient_service.it;

import com.diabetesrisk.patient_service.PatientServiceApplication;
import com.diabetesrisk.patient_service.model.Patient;
import com.diabetesrisk.patient_service.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(
		classes = PatientServiceApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
class PatientServiceIT {

	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
			"postgres:16-alpine"
	);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	static void beforeAll() {
		postgresContainer.start();
	}

	@AfterAll
	static void afterAll() {
		postgresContainer.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
	}

	@Test
	@DisplayName("should start Postgres container")
	void connectionEstablished() {
		assertThat(postgresContainer.isCreated()).isTrue();
		assertThat(postgresContainer.isRunning()).isTrue();
	}

	@Test
	@DisplayName("should find 4 patients in database")
	void getPatients() throws Exception {
		List<Patient> patients = patientRepository.findAll();
		assertThat(patients).isNotEmpty();
		assertThat(patients).hasSize(4);

		mockMvc.perform(get("/patients"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(content().json(objectMapper.writeValueAsString(patients)));
	}

	@Nested
	@DisplayName("getPatient")
	class getPatientIT {

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4})
		@DisplayName("should find 1 patient with id 1, 2, 3 and 4")
		void getPatient(int patientId) throws Exception {
			mockMvc.perform(get("/patients/" + patientId))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id").value(patientId));
		}

		@Test
		@DisplayName("should not find patient with id 99")
		void getPatient_notFound() throws Exception {
			mockMvc.perform(get("/patients/99"))
					.andDo(print())
					.andExpect(status().isNotFound());
		}

		@ParameterizedTest
		@ValueSource(ints = {-9, -1, 0})
		@DisplayName("should throw 400 Bad Request for invalid id")
		void getPatient_InvalidId(int invalidId) throws Exception {
			mockMvc.perform(get("/patients/" + invalidId))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.id")
							.value("must be greater than or equal to 1"));
		}
	}

	@Nested
	@DisplayName("getPatientDto")
	class getPatientDtoIT {

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4})
		@DisplayName("should find 1 patientDto with id 1, 2, 3 and 4")
		void getPatientDto(int patientId) throws Exception {
			mockMvc.perform(get("/patients/risk-assessment/" + patientId))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id").value(patientId))
					.andExpect(jsonPath("$.age").exists())
					.andExpect(jsonPath("$.gender").exists());
		}

		@Test
		@DisplayName("should not find patientDto with id 88")
		void getPatientDto_notFound() throws Exception {
			mockMvc.perform(get("/patients/risk-assessment/88"))
					.andDo(print())
					.andExpect(status().isNotFound());
		}

		@ParameterizedTest
		@ValueSource(ints = {-9, -1, 0})
		@DisplayName("should throw 400 Bad Request for invalid id")
		void getPatientDto_InvalidId(int invalidId) throws Exception {
			mockMvc.perform(get("/patients/risk-assessment/" + invalidId))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.id")
							.value("must be greater than or equal to 1"));
		}
	}

	@Nested
	@DisplayName("savePatient")
	class savePatientIT {

		@Test
		@DisplayName("should save valid patient")
		@Rollback()
		void saveValidPatient() throws Exception {
			Patient patient = new Patient()
					.firstname("John")
					.lastname("Doe")
					.birthDate("1985-03-12")
					.gender("M");

			mockMvc.perform(post("/patients")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(patient)))
					.andDo(print())
					.andExpect(status().isCreated())
					.andExpect(header().string("location", "http://localhost/patients/5"));
		}

		@Test
		@DisplayName("should not save invalid patient")
		void shouldReturnBadRequestForInvalidPatient() throws Exception {
			Patient patient = new Patient();

			mockMvc.perform(post("/patients")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(patient)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.firstname").value("Firstname is mandatory"))
					.andExpect(jsonPath("$.lastname").value("Lastname is mandatory"))
					.andExpect(jsonPath("$.birthDate").value("Birthdate is mandatory"))
					.andExpect(jsonPath("$.gender").value("Gender is mandatory"));
		}

		@Test
		@DisplayName("should not save invalid patient 2")
		void shouldReturnBadRequestForInvalidPatient2() throws Exception {
			Patient patient = new Patient()
					.firstname("John")
					.lastname("Doe")
					.birthDate("12031985")
					.gender("S");

			mockMvc.perform(post("/patients")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(patient)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.birthDate").value("Birthdate must be in the format YYYY-MM-DD"))
					.andExpect(jsonPath("$.gender").value("Gender must be either 'F' or 'M'"));
		}
	}

	@Nested
	@DisplayName("updatePatient")
	class updatePatientIT {

		@Test
		@DisplayName("should update existing and valid patient")
		@Rollback()
		void saveValidPatient() throws Exception {
			Patient patient = patientRepository.findById(1).get();
			patient.setFirstname("John");

			mockMvc.perform(put("/patients/" + patient.getId())
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(patient)))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(objectMapper.writeValueAsString(patient)));

		}

		@Test
		@DisplayName("should throw 404 for non-existing patient")
		void shouldReturn404() throws Exception {
			Patient patient = patientRepository.findById(1).get();
			patient.setId(6);

			mockMvc.perform(put("/patients/" + patient.getId())
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(patient)))
					.andDo(print())
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$").value("Patient not found"));
		}

		@Test
		@DisplayName("should not update invalid patient")
		void shouldReturnBadRequestForInvalidPatient() throws Exception {
			Patient patient = new Patient();

			mockMvc.perform(put("/patients/" + patient.getId())
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(patient)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.firstname").value("Firstname is mandatory"))
					.andExpect(jsonPath("$.lastname").value("Lastname is mandatory"))
					.andExpect(jsonPath("$.birthDate").value("Birthdate is mandatory"))
					.andExpect(jsonPath("$.gender").value("Gender is mandatory"));
		}

		@Test
		@DisplayName("should not save invalid patient 2")
		void shouldReturnBadRequestForInvalidPatient2() throws Exception {
			Patient patient = new Patient()
					.firstname("John")
					.lastname("Doe")
					.birthDate("12031985")
					.gender("S");

			mockMvc.perform(put("/patients/" + patient.getId())
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(patient)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.birthDate").value("Birthdate must be in the format YYYY-MM-DD"))
					.andExpect(jsonPath("$.gender").value("Gender must be either 'F' or 'M'"));
		}
	}

}
