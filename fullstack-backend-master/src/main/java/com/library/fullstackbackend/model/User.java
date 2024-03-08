package com.library.fullstackbackend.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "use_user") // Changed table name to "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    private String designation;
    private String facility;
    private String phone_number;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FingerprintData> fingerprintData = new ArrayList<>();

    // Default constructor
    public User() {
    }

    // Getter and setter methods
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public List<FingerprintData> getFingerprintData() {
        return fingerprintData;
    }

    public void setFingerprintData(List<FingerprintData> fingerprintData) {
        this.fingerprintData = fingerprintData;
    }
}
