package com.example.aplazo.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.aplazo.entity.Customer;

public class CustomerResponseDTO {

	private UUID id;
	private Double creditLineAmount;
	private Double availableCreditLineAmount;
	private OffsetDateTime createdAt;
	
	public CustomerResponseDTO(Customer customer) {
		
		this.id = customer.getId();
		this.creditLineAmount = customer.getCreditLineAmount();
		this.availableCreditLineAmount = customer.getAvailableCreditLineAmount();
		this.createdAt = customer.getCreatedAt();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Double getCreditLineAmount() {
		return creditLineAmount;
	}

	public void setCreditLineAmount(Double creditLineAmount) {
		this.creditLineAmount = creditLineAmount;
	}

	public Double getAvailableCreditLineAmount() {
		return availableCreditLineAmount;
	}

	public void setAvailableCreditLineAmount(Double availableCreditLineAmount) {
		this.availableCreditLineAmount = availableCreditLineAmount;
	}
}