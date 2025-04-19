package com.example.aplazo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "installments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Installment {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_id", nullable = false)
	private Loan loan;

	@Column(nullable = false)
	private Double amount;

	@Column(name = "scheduled_payment_date", nullable = false)
	private LocalDate scheduledPaymentDate;

	@Column(nullable = false)
	private String status; // NEXT, PENDING, ERROR
}