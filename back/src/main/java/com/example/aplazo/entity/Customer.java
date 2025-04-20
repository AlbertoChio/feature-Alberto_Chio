package com.example.aplazo.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.example.aplazo.dto.CustomerSaveDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

	public Customer(CustomerSaveDTO dto) {
		this.firstName = dto.getFirstName();
		this.lastName = dto.getLastName();
		this.secondLastName = dto.getSecondLastName();
		this.dateOfBirth = dto.getDateOfBirth();
		this.gender = dto.getGender();
		this.placeOfBirth = dto.getPlaceOfBirth();
		this.curp = dto.getCurp();
	}

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
	@Pattern(regexp = "(?i)hombre|mujer", message = "Gender must be 'Hombre' or 'Mujer'")
	private String gender;

	@Column(nullable = false)
	private String placeOfBirth;

	@Column(nullable = false, unique = true, length = 18)
	@Pattern(regexp = "^[A-Z]{4}\\d{6}[A-Z]{6}\\d{2}$", message = "CURP must follow the format: 4 letters, 6 digits (date), 6 letters, and 2 digits. Example: GODE561231HDFABC09")
	private String curp;

	@Column(nullable = false)
	private Double creditLineAmount;

	@Column(nullable = false)
	private Double availableCreditLineAmount;

	@Column(nullable = false)
	private final OffsetDateTime createdAt = OffsetDateTime.now();

	public String getCurp() {
		return curp != null ? curp.toUpperCase() : null;
	}

	public void setCurp(String curp) {
		this.curp = curp != null ? curp.toUpperCase() : null;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", secondLastName="
				+ secondLastName + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", placeOfBirth="
				+ placeOfBirth + ", curp=" + curp + ", creditLineAmount=" + creditLineAmount
				+ ", availableCreditLineAmount=" + availableCreditLineAmount + ", createdAt=" + createdAt + "]";
	}

}
