package com.example.aplazo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aplazo.config.RateLimit;
import com.example.aplazo.dto.ErrorResponseDTO;
import com.example.aplazo.dto.LoginRequestDTO;
import com.example.aplazo.dto.LoginResponseDTO;
import com.example.aplazo.entity.User;
import com.example.aplazo.service.AuthService;
import com.example.aplazo.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	private final JwtService jwtService;
	private final AuthService authService;

	public AuthController(JwtService jwtService, AuthService authService) {
		this.jwtService = jwtService;
		this.authService = authService;
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody LoginRequestDTO registerUserDto) {
		User registeredUser = authService.costumerSignup(registerUserDto);
		return ResponseEntity.ok(registeredUser);
	}

	@Operation(summary = "User login", operationId = "login")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
			@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@RateLimit(limit = 5, timeWindow = 60)
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

		User authenticatedUser = authService.authenticate(loginRequestDTO);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponseDTO loginResponse = new LoginResponseDTO();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}

}