package com.attendance.fullstackbackend.repository;
import com.attendance.fullstackbackend.model.VerifyFinger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface VerifyFingerDataRepository extends JpaRepository<VerifyFinger, UUID> {
    @Query("SELECT fd.user.id FROM FingerprintData fd")
    List<Long> getUserIdFromUserBiometric();
}
