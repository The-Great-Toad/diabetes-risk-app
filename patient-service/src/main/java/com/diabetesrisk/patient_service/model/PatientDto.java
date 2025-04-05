package com.diabetesrisk.patient_service.model;


public class PatientDto {

    private Integer id;

    private int age;

    private String gender;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        final StringBuilder sb = new StringBuilder("PatientDto{");
        sb.append("id=").append(id);
        sb.append(", age=").append(age);
        sb.append(", gender='").append(gender).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static PatientDto builder() {
        return new PatientDto();
    }

    public PatientDto id(Integer id) {
        this.id = id;
        return this;
    }

    public PatientDto age(int age) {
        this.age = age;
        return this;
    }

    public PatientDto gender(String gender) {
        this.gender = gender;
        return this;
    }
}

