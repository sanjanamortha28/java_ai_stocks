package com.aitrading.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

/**
 * Health Check Response DTO
 */
@Data
@AllArgsConstructor
public class HealthCheckResponse {
    private String status;
    private String timestamp;
    private Map<String, Object> components;
}
