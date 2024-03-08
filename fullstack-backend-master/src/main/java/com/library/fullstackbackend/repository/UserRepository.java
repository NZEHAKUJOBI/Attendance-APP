package com.library.fullstackbackend.repository;

import com.library.fullstackbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
