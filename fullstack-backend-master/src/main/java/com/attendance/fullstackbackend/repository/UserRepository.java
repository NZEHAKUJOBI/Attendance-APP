package com.attendance.fullstackbackend.repository;

import com.attendance.fullstackbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
