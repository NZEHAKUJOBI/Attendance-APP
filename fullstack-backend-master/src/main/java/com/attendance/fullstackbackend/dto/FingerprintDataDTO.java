package com.attendance.fullstackbackend.dto;

import java.time.Instant;
import java.util.UUID;

public class FingerprintDataDTO {
    private UUID id;
    private boolean success;
    private String message;
    private byte[] biometricData;
    private Instant dateRegistered;
    private UUID userId;
    private String userName; // Assuming you want to include user's name in the DTO

    // Constructors
    public FingerprintDataDTO() {
    }

    public FingerprintDataDTO(UUID id, boolean success, String message, byte[] biometricData, Instant dateRegistered, UUID userId, String userName) {
        this.id = id;
        this.success = success;
        this.message = message;
        this.biometricData = biometricData;
        this.dateRegistered = dateRegistered;
        this.userId = userId;
        this.userName = userName;
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

    public Instant getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Instant dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
