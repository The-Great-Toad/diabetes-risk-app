package com.diabetesrisk.patient_service.model;

import jakarta.persistence.*;

import java.util.StringJoiner;

@Entity
@Table
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(name = "birthdate")
    private String birthDate;

    @Column
    private String gender;

    @Column
    private String address;

    @Column
    private String phone;

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phoneNumber) {
        this.phone = phoneNumber;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Patient.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstname + "'")
                .add("lastName='" + lastname + "'")
                .add("birthDate='" + birthDate + "'")
                .add("gender='" + gender + "'")
                .add("address='" + address + "'")
                .add("phone='" + phone + "'")
                .toString();
    }

    public static Patient builder() {
        return new Patient();
    }

    public Patient lastName(String lastname) {
        setLastname(lastname);
        return this;
    }

    public Patient firstName(String firstname) {
        setFirstname(firstname);
        return this;
    }

    public Patient birthDate(String birthdate) {
        setBirthDate(birthdate);
        return this;
    }

    public Patient gender(String gender) {
        setGender(gender);
        return this;
    }

    public Patient address(String address) {
        setAddress(address);
        return this;
    }

    public Patient phone(String phone) {
        setPhone(phone);
        return this;
    }

}
