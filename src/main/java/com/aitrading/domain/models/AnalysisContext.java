package com.aitrading.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Domain Model: Analysis Context
 * Contains all context data flowing through the analysis workflow.
 * Clean Architecture - Domain Layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisContext {
    
    // ═══════════════════ CORE ANALYSIS PARAMETERS ═══════════════════
    
    private String analysisId = UUID.randomUUID().toString();
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // User request parameters
    private String symbol;
    private String country;           // USA / INDIA
    private String strategy;          // intraday / swing / long-term / options / commodities
    private String riskLevel;         // conservative / moderate / aggressive
    private int lookbackDays;
    
    // Analysis flags
    private boolean includeFundamental;
    private boolean includeTechnical;
    private boolean includeNews;
    private boolean includeOptions;
    private boolean includeRiskManagement;
    
    // User preferences
    private String knowledgeLevel;    // beginner / intermediate / expert
    private String language;          // english
    private boolean includeEducation;
    private boolean includeDataLineage;
    
    // ═══════════════════ MARKET DATA ═══════════════════
    
    private Map<String, Object> marketData = new HashMap<>();
    private Map<String, Object> companyInfo = new HashMap<>();
    private Map<String, Object> technicalMetrics = new HashMap<>();
    
    // ═══════════════════ AGENT ANALYSIS RESULTS ═══════════════════
    
    private Map<String, Object> fundamentalAnalysis = new HashMap<>();
    private Map<String, Object> technicalAnalysis = new HashMap<>();
    private Map<String, Object> newsIntelligence = new HashMap<>();
    private Map<String, Object> riskAssessment = new HashMap<>();
    private Map<String, Object> strategyConstruction = new HashMap<>();
    
    // ═══════════════════ DATA QUALITY TRACKING ═══════════════════
    
    private Map<String, DataQualityReport> agentQualityReports = new HashMap<>();
    
    // ═══════════════════ EXECUTION METADATA ═══════════════════
    
    private String executionSource;   // REAL_LLM / MOCK_LLM / CACHE
    private long totalExecutionTimeMs;
    private List<String> executionLog = new ArrayList<>();
    
    /**
     * Get quality report for a specific agent
     */
    public DataQualityReport getQualityReport(String agentName) {
        return agentQualityReports.getOrDefault(agentName, new DataQualityReport());
    }
    
    /**
     * Set quality report for an agent
     */
    public void setQualityReport(String agentName, DataQualityReport report) {
        agentQualityReports.put(agentName, report);
    }
    
    /**
     * Log execution step
     */
    public void logExecution(String message) {
        executionLog.add(String.format("[%s] %s", LocalDateTime.now(), message));
    }
    
    /**
     * Calculate average data quality across all agents
     */
    public double getAverageQualityModifier() {
        if (agentQualityReports.isEmpty()) return 1.0;
        double sum = agentQualityReports.values().stream()
            .mapToDouble(DataQualityReport::getConfidenceModifier)
            .sum();
        return sum / agentQualityReports.size();
    }
}
