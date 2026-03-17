package com.aitrading.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Error Response DTO
 */
@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private int status_code;
    private String error;
    private String message;
    private long timestamp;
}
