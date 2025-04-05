package com.diabetesrisk.risk_assesment_service.model;

public class Patient {

    private String id;

    private int age;

    private String gender;


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Patient id(String id) {
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
