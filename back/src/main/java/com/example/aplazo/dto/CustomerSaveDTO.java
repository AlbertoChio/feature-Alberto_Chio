package com.example.aplazo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

public class CustomerSaveDTO {
	private String firstName;
	private String lastName;
	private String secondLastName;
	private LocalDate dateOfBirth;
	@Pattern(regexp = "(?i)hombre|mujer", message = "Gender must be 'Hombre' or 'Mujer'")
	private String gender;
	private String placeOfBirth;
	@Pattern(regexp = "^[A-Z]{4}\\d{6}[A-Z]{6}\\d{2}$", message = "CURP must follow the format: 4 letters, 6 digits (date), 6 letters, and 2 digits. Example: GODE561231HDFABC09")
	private String curp;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}
}