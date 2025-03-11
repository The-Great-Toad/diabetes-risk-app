package com.diabetesrisk.patient_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "birthdate")
    private String birthDate;

    @Column
    private String gender;

    @Column
    private String address;

    @Column(name = "phonenumber")
    private String phoneNumber;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
