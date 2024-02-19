package com.library.fullstackbackend.model;

import com.library.fullstackbackend.biometric.FingerprintData;
import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;




@Entity
@Table(name = "user_user") // Specify a different table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    private String fullName;
    private String designation;
    private String facility;
    private String phone_number;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private FingerprintData fingerprintData;
    private List<FingerprintData> fingerprintData = new ArrayList<FingerprintData>();


    public User() {
        // Default constructor with no arguments
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    // Getter and setter for fingerprintData

    public List<FingerprintData> getFingerprintData() {
        return fingerprintData;
    }

    public void setFingerprintData(List<FingerprintData> fingerprintData) {
        this.fingerprintData = fingerprintData;
    }
}