package com.diabetesrisk.patient_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.StringJoiner;

@Entity
@Table
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(1)
    private Integer id;

    @Column
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;

    @Column
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;

    @Column(name = "birthdate")
    @NotBlank(message = "Birthdate is mandatory")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Birthdate must be in the format YYYY-MM-DD")
    private String birthDate;

    @Column
    @NotBlank(message = "Gender is mandatory")
    @Pattern(regexp = "[F-M]", message = "Gender must be either 'F' or 'M'")
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

    public Patient id(Integer id) {
        setId(id);
        return this;
    }

    public Patient lastname(String lastname) {
        setLastname(lastname);
        return this;
    }

    public Patient firstname(String firstname) {
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
