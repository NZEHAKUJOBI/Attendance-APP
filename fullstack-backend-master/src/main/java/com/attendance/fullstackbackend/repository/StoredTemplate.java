package com.attendance.fullstackbackend.repository;
import com.attendance.fullstackbackend.model.FingerprintData;


public interface StoredTemplate {
    String getUser();
    byte[]  getBiometricData();
}
