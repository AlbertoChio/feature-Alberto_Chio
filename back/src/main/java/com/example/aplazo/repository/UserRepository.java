package com.example.aplazo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aplazo.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);
}