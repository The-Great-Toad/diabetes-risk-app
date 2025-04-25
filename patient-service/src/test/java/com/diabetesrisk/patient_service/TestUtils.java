package com.diabetesrisk.patient_service;

import com.diabetesrisk.patient_service.model.Patient;

import java.time.LocalDate;

public class TestUtils {

    protected Patient createPatient() {
        return new Patient()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .birthDate("1985-03-12")
                .gender("M")
                .address("123 Main St")
                .phone("123-456-7890");
    }

    protected int calculatePatientAge(String birthDate) {
        LocalDate patientBirthDate = LocalDate.parse(birthDate);
        LocalDate now = LocalDate.now();
        int age = now.getYear() - patientBirthDate.getYear();

        return now.getDayOfYear() < patientBirthDate.getDayOfYear() ? --age : age;
    }
}
