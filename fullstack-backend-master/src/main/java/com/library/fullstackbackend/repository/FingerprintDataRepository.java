package com.library.fullstackbackend.repository;
import com.library.fullstackbackend.model.FingerprintData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FingerprintDataRepository extends JpaRepository<FingerprintData, UUID> {
}
