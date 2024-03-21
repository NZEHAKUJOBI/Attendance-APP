package com.attendance.fullstackbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "attendance_log")
public class VerifyFinger {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(nullable = false)
    private boolean success;

    @Column(nullable = false)
    private String message;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore // Ignore serialization of this field
    private User user;

    @CreationTimestamp
    private Instant check_in;

    @CreationTimestamp
    private Instant check_out;

    @Column(columnDefinition = "bytea")
    private byte[] biometricMatch;

    public VerifyFinger() {
    }

    public VerifyFinger(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public VerifyFinger(boolean b, String fingerprintVerificationSuccessful, UUID id) {
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Instant check_in) {
        this.check_in = check_in;
    }

    public Instant getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Instant check_out) {
        this.check_out = check_out;
    }

    public byte[] getBiometricMatch() {
        return biometricMatch;
    }

    public void setBiometricMatch(byte[] biometricMatch) {
        this.biometricMatch = biometricMatch;
    }

    public Map<String, Object> getVerified() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("message", message);
        return map;
    }
}
