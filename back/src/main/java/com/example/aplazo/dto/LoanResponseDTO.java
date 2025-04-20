package com.example.aplazo.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {
	private UUID id;
	private UUID customerId;
	private OffsetDateTime createdAt;
	private String status;
	private PaymentPlanDTO paymentPlan;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaymentPlanDTO {
		private List<InstallmentDTO> installments;
		private Double commissionAmount;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InstallmentDTO {
		private Double amount;
		private LocalDate scheduledPaymentDate;
		private String status;
	}
}