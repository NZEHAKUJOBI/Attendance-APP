package com.attendance.fullstackbackend.repository;
import com.attendance.fullstackbackend.model.FingerprintData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FingerprintDataRepository extends JpaRepository<FingerprintData, UUID> {
}
