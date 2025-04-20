package com.example.aplazo.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(nullable = false)
	private String action;

	@Column(name = "entity_type", nullable = false)
	private String entityType;

	@Column(name = "entity_id", nullable = false)
	private UUID entityId;

	@Lob
	@Column(columnDefinition = "jsonb")
	private String details;

	@Column(name = "created_at", nullable = false, updatable = false)
	private final Instant createdAt = Instant.now();
}