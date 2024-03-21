package com.attendance.fullstackbackend.dto;

import java.time.Instant;
import java.util.UUID;

public class VerifyFingerDTO {

    private UUID id;
    private boolean success;
    private String message;
    private Instant checkIn;
    private Instant checkOut;

    // Constructors
    public VerifyFingerDTO() {
    }

    public VerifyFingerDTO(UUID id, boolean success, String message, Instant checkIn, Instant checkOut) {
        this.id = id;
        this.success = success;
        this.message = message;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    // Getters and Setters
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

    public Instant getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Instant checkIn) {
        this.checkIn = checkIn;
    }

    public Instant getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Instant checkOut) {
        this.checkOut = checkOut;
    }
}
