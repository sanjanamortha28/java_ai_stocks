package com.aitrading.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Analysis Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResponseDTO {
    
    private String statusCode;
    private String symbol;
    private String analysisId;
    private long timestamp;
    
    // Master CIO decision and conviction
    private String investmentDecision;
    private double investmentConvictioLevel;
    private String rationale;
    
    // Component analyses
    private Map<String, Object> fundamental_analysis;
    private Map<String, Object> technical_analysis;
    private Map<String, Object> news_intelligence;
    private Map<String, Object> risk_assessment;
    private Map<String, Object> strategy_construction;
    
    // Execution metadata
    private String execution_source;
    private long execution_time_ms;
    private java.util.List<String> execution_log;
    
    // Data quality tracking across all agents
    private Map<String, Object> data_quality_report;
}
