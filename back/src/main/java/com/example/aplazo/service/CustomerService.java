package com.example.aplazo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.aplazo.dto.CustomerResponseDTO;
import com.example.aplazo.entity.Customer;
import com.example.aplazo.entity.User;
import com.example.aplazo.exception.CustomerNotFoundException;
import com.example.aplazo.repository.CustomerRepository;

import jakarta.validation.Valid;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final AuditLogService auditLogService;

	public CustomerService(CustomerRepository customerRepository, AuditLogService auditLogService) {
		this.customerRepository = customerRepository;
		this.auditLogService = auditLogService;
	}

	public CustomerResponseDTO createCustomer(@Valid Customer customer) {
		Customer customerSaved = customerRepository.save(customer);
		CustomerResponseDTO response = new CustomerResponseDTO(customerSaved);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();
		UUID userId = userDetails.getId();
		auditLogService.log(userId, "CREATE", "Customer", userId, customer.toString());
		return response;
	}

	public CustomerResponseDTO getCustomer(UUID customerId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();
		UUID userId = userDetails.getId();
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
		CustomerResponseDTO customerResponse = new CustomerResponseDTO(customer);
		auditLogService.log(userId, "READ", "Customer", userId, null);
		return customerResponse;
	}
}