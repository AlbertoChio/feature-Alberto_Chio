package com.example.aplazo.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aplazo.config.RateLimit;
import com.example.aplazo.dto.ErrorResponseDTO;
import com.example.aplazo.dto.LoanRequestDTO;
import com.example.aplazo.dto.LoanResponseDTO;
import com.example.aplazo.entity.Loan;
import com.example.aplazo.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Loans", description = "Manage loans")
@RestController
@RequestMapping("/v1/loans")
@RequiredArgsConstructor
public class LoanController {

	private final LoanService loanService;

	@Operation(summary = "Create a new loan", description = "Creates a new loan for a customer and generates 12 monthly installments", operationId = "createLoan")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Loan created successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@RateLimit(limit = 5, timeWindow = 60)
	@PostMapping
	public ResponseEntity<LoanResponseDTO> createLoan(@Valid @RequestBody LoanRequestDTO loanRequest) {
		LoanResponseDTO loan = loanService.createLoan(loanRequest.getCustomerId(), loanRequest.getAmount());
		return ResponseEntity.created(null).body(loan);
	}

	@Operation(summary = "Get loan identified by `loanId`", operationId = "getLoanById")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Ok"),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@RateLimit(limit = 5, timeWindow = 60)
	@GetMapping("/{loanId}")
	public ResponseEntity<LoanResponseDTO> getLoan(@PathVariable UUID loanId) {
		return ResponseEntity.ok(loanService.getLoan(loanId));
	}

}