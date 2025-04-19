package com.example.aplazo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponseDTO {

	public ErrorResponseDTO(String code, String error, Long timestamp, String message, String path) {
		super();
		this.code = code;
		this.error = error;
		this.timestamp = timestamp;
		this.message = message;
		this.path = path;
	}

	@Schema(example = "APZ000001", description = "Internal error code")
	private String code;

	@Schema(example = "INTERNAL_SERVER_ERROR", description = "Internal error name")
	private String error;

	@Schema(example = "1739397485", description = "Unix timestamp")
	private Long timestamp;

	@Schema(example = "Error detail", description = "Error message or error description")
	private String message;

	@Schema(example = "/v1/customers", description = "The path where the error occurred")
	private String path;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}