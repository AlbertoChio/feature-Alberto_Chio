package com.example.aplazo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.aplazo.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000002", "INVALID_CUSTOMER_REQUEST",
				Instant.now().getEpochSecond(), message, request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000003", "RATE_LIMIT_ERROR",
				Instant.now().getEpochSecond(), ex.getMessage(), request.getRequestURI());
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex,
			HttpServletRequest request) {
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000004", "INVALID_REQUEST", Instant.now().getEpochSecond(),
				ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex,
			HttpServletRequest request) {
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000007", "UNAUTHORIZED", Instant.now().getEpochSecond(),
				"Invalid username or password", request.getRequestURI());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException ex,
			HttpServletRequest request) {
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000007", "UNAUTHORIZED", Instant.now().getEpochSecond(),
				ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000001", "INTERNAL_SERVER_ERROR",
				Instant.now().getEpochSecond(), ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(RateLimitException.class)
	public ResponseEntity<ErrorResponseDTO> handleRateLimitException(RateLimitException ex,
			HttpServletRequest request) {
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000003", "RATE_LIMIT_ERROR",
				Instant.now().getEpochSecond(), ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);

	}

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleCustomerNotFound(CustomerNotFoundException ex,
			HttpServletRequest request) {
		ErrorResponseDTO response = new ErrorResponseDTO("APZ000005", "CUSTOMER_NOT_FOUND",
				Instant.now().getEpochSecond(), ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}