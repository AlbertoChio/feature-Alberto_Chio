package com.example.aplazo.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	@Column(name = "customer_id", nullable = false, updatable = false)
	private UUID customerId;

	@Column(nullable = false)
	private Double amount;

	@Column(nullable = false)
	private String status;

	@Column(name = "created_at", nullable = false, updatable = false)
	private final OffsetDateTime createdAt = OffsetDateTime.now();

	@Column(name = "commission_amount", nullable = false)
	private Double commissionAmount;

	@OneToMany(mappedBy = "loanId", fetch = FetchType.LAZY)
	private List<Installment> installments;

	@Override
	public String toString() {
		return "Loan [id=" + id + ", customerId=" + customerId + ", amount=" + amount + ", status=" + status
				+ ", createdAt=" + createdAt + ", commissionAmount=" + commissionAmount + "]";
	}

}