package com.example.aplazo.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LoanRequestDTO {

	@NotNull
	private UUID customerId;

	@Min(value = 0, message = "Amount must be at least 0")
	@NotNull
	private Double amount;

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}