package com.diabetesrisk.risk_assesment_service.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Patient {

    @NotNull(message = "Patient ID cannot be null")
    @Min(value = 1, message = "Patient ID must be greater than 0")
    private int id;

    @NotNull(message = "Age cannot be null")
    @Min(value = 1, message = "Patient ID must be greater than 0")
    @Max(value = 120, message = "Patient ID must be less than 120")
    private int age;

    @NotBlank(message = "Patient gender cannot be blank")
    private String gender;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Patient{");
        sb.append("patientId='").append(id).append('\'');
        sb.append(", age=").append(age);
        sb.append(", gender='").append(gender).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Patient builder() {
        return new Patient();
    }

    public Patient id(int id) {
        this.id = id;
        return this;
    }

    public Patient age(int age) {
        this.age = age;
        return this;
    }

    public Patient gender(String gender) {
        this.gender = gender;
        return this;
    }
}
