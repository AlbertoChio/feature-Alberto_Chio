package com.example.aplazo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	
	@Id
	private UUID id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String secondLastName;

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	@Column(nullable = false)
	private Double creditLineAmount;

	@Column(nullable = false)
	private Double availableCreditLineAmount;

	@Column(nullable = false)
	private final OffsetDateTime createdAt = OffsetDateTime.now();
}
