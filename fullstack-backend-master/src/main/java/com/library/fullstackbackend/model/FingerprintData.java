package com.library.fullstackbackend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.fullstackbackend.model.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_biometric")
public class FingerprintData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    public UUID id;

    private boolean success;
    private String message;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore // Ignore serialization of this field
    private User user;
    @Column(columnDefinition = "bytea")
    private byte[] biometricData;





    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }









    // Default constructor (required by JPA)
    public FingerprintData() {}

    public FingerprintData(boolean success, String message, byte[] biometricData) {
        this.success = success;
        this.message = message;
        this.biometricData = biometricData;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getBiometricData() {
        return biometricData;
    }

    public void setBiometricData(byte[] biometricData) {
        this.biometricData = biometricData;
    }
}