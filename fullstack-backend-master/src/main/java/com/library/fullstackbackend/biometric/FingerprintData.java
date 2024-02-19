package com.library.fullstackbackend.biometric;
import com.library.fullstackbackend.model.User;

import javax.persistence.*;

@Entity
@Table(name = "user_biometric")
public class FingerprintData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean success;
    private String message;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
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
    protected FingerprintData() {}

    public FingerprintData(boolean success, String message, byte[] biometricData ) {
        this.success = success;
        this.message = message;
        this.biometricData = biometricData;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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