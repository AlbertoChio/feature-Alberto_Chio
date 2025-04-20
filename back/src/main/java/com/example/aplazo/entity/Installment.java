package com.example.aplazo.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "installments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Installment {

	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	@Column(name = "loan_id", nullable = false, updatable = false)
	private UUID loanId;

	@Column(nullable = false)
	private Double amount;

	@Column(name = "scheduled_payment_date", nullable = false)
	private LocalDate scheduledPaymentDate;

	@Column(nullable = false)
	private String status;

	@Override
	public String toString() {
		return "Installment [id=" + id + ", loanId=" + loanId + ", amount=" + amount + ", scheduledPaymentDate="
				+ scheduledPaymentDate + ", status=" + status + "]";
	}

}