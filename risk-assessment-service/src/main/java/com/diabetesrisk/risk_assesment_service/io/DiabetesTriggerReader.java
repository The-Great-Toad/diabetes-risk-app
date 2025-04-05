package com.diabetesrisk.risk_assesment_service.io;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiabetesTriggerReader {

	private static final Logger log = LoggerFactory.getLogger(DiabetesTriggerReader.class);

	private final List<String> triggers;

	@Value("${spring.triggers.file.name}")
	private String fileName;

	public DiabetesTriggerReader() {
		this.triggers = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		log.info("Reading triggers from file: {}", fileName);
		if (fileName != null) {
			try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
				String line = reader.readLine();

				while (line != null) {
					triggers.add(line.toLowerCase());
					line = reader.readLine();
				}
			} catch (IOException e) {
				log.error("Error reading triggers from file: {}", fileName, e);
			}
		}

		if (triggers.isEmpty()) {
			log.warn("No triggers found in the file");
		} else {
			log.info("Triggers loaded successfully: {}", triggers);
		}
	}

	/**
	 * Returns the list of triggers.
	 *
	 * @return the list of triggers
	 */
	public List<String> getTriggers() {
		return triggers;
	}
}
