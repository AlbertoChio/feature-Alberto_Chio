package com.example.aplazo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aplazo.entity.Loan;

import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
	List<Loan> findByCustomerId(UUID customerId);
}