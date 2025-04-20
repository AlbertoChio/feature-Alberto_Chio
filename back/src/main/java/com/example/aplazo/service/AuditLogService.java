package com.example.aplazo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.aplazo.entity.AuditLog;
import com.example.aplazo.repository.AuditLogRepository;

@Service
public class AuditLogService {

	private final AuditLogRepository auditLogRepository;

	public AuditLogService(AuditLogRepository auditLogRepository) {
		this.auditLogRepository = auditLogRepository;
	}

	public AuditLog log(UUID userId, String action, String entityType, UUID entityId, String details) {
		AuditLog log = new AuditLog();
		log.setUserId(userId);
		log.setAction(action);
		log.setEntityType(entityType);
		log.setEntityId(entityId);
		log.setDetails(details);
		return auditLogRepository.save(log);
	}

	public Optional<AuditLog> getLogbyId(UUID logId) {
		return auditLogRepository.findById(logId);
	}
}