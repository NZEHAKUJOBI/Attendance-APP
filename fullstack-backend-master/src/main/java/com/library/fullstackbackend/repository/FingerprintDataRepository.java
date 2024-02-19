package com.library.fullstackbackend.repository;
import com.library.fullstackbackend.biometric.FingerprintData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FingerprintDataRepository extends JpaRepository<FingerprintData, Long> {
}
