package com.example.aplazo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String status;

	@Column(name = "created_at", nullable = false, updatable = false)
	private final Instant createdAt = Instant.now();

	@Column(name = "commission_amount", nullable = false)
	private BigDecimal commissionAmount;
}