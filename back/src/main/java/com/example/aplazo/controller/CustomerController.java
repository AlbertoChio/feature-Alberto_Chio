package com.example.aplazo.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aplazo.config.RateLimit;
import com.example.aplazo.dto.CustomerResponseDTO;
import com.example.aplazo.dto.CustomerSaveDTO;
import com.example.aplazo.dto.ErrorResponseDTO;
import com.example.aplazo.entity.Customer;
import com.example.aplazo.entity.User;
import com.example.aplazo.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Customers", description = "Manage customers")
@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;

	@Operation(summary = "Creates customer", operationId = "createCustomer")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Created"),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@RateLimit(limit = 5, timeWindow = 60)
	@PostMapping()
	public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerSaveDTO customerDTO) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();
		UUID userId = userDetails.getId();

		Customer customer = new Customer(customerDTO);
		customer.setId(userId);
		customer.setCreditLineAmount(Double.valueOf(10000));
		customer.setAvailableCreditLineAmount(Double.valueOf(10000));

		return ResponseEntity.created(null).body(customerService.createCustomer(customer));
	}

	@Operation(summary = "Get customer identified by `customerId`", operationId = "getCustomerById")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Ok"),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@RateLimit(limit = 5, timeWindow = 60)
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable UUID customerId) {

		CustomerResponseDTO customer = customerService.getCustomer(customerId);
		return ResponseEntity.ok(customer);
	}
}