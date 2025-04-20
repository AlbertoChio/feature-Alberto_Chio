package com.example.aplazo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aplazo.entity.AuditLog;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}