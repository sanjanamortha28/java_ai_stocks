package com.aitrading.domain.agents;

import com.aitrading.domain.models.AnalysisContext;
import com.aitrading.domain.models.DataQualityReport;
import com.aitrading.utils.Logger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Agent Abstract Class
 * Defines the institutional-grade interface for all trading research agents.
 * 
 * Architecture: Clean Architecture - Domain Services Layer  
 * 
 * All agents must:
 * - Implement specialized analysis logic
 * - Attach data quality reports to outputs
 * - Log execution with full traceability
 * - Handle errors gracefully
 * 
 * This is the foundational contract for all agent implementations.
 */
public abstract class BaseAgent {
    
    protected final String name;
    protected final String role;
    protected final LocalDateTime createdAt;
    
    public BaseAgent(String name, String role) {
        this.name = name;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Primary analysis method - MUST be implemented by all subclasses
     * 
     * @param context Complete analysis context with market data and prior agent outputs
     * @return Analysis results with attached data quality report
     */
    public abstract Map<String, Object> analyze(AnalysisContext context);
    
    /**
     * Validate input context before proceeding
     */
    protected boolean validateInput(AnalysisContext context) {
        if (context == null) {
            Logger.warn("{}: Null context provided", name);
            return false;
        }
        if (context.getSymbol() == null || context.getSymbol().isEmpty()) {
            Logger.warn("{}: No symbol provided", name);
            return false;
        }
        return true;
    }
    
    /**
     * Template method for analysis with built-in error handling
     */
    public final Map<String, Object> executeAnalysis(AnalysisContext context) {
        long startTime = System.currentTimeMillis();
        
        try {
            if (!validateInput(context)) {
                return createErrorResponse("Invalid input context");
            }
            
            Logger.info("{}: Starting analysis for {}", name, context.getSymbol());
            context.logExecution(name + " analysis started");
            
            Map<String, Object> result = analyze(context);
            
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("{}: Analysis complete for {} ({}ms)", name, context.getSymbol(), duration);
            context.logExecution(name + " analysis completed in " + duration + "ms");
            
            return result;
        } catch (Exception e) {
            Logger.error("{}: Analysis error for {}: {}", name, context.getSymbol(), e.getMessage());
            context.logExecution(name + " analysis FAILED: " + e.getMessage());
            return createErrorResponse(e.getMessage());
        }
    }
    
    /**
     * Assess data quality of provided data
     * Used internally by agents to rate their own outputs
     */
    protected DataQualityReport assessDataQuality(
            Map<String, Object> data,
            String[] expectedFields) {
        
        DataQualityReport report = new DataQualityReport();
        report.setTotalFields(expectedFields.length);
        
        int presentCount = 0;
        for (String field : expectedFields) {
            if (data.containsKey(field) && data.get(field) != null) {
                presentCount++;
            } else {
                report.getMissingFields().add(field);
            }
        }
        
        report.setPresentFields(presentCount);
        return report;
    }
    
    /**
     * Sanitize suspicious values (NaN, Infinity, etc.)
     */
    protected Object sanitizeValue(Object value) {
        if (value instanceof Double) {
            double d = (Double) value;
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                return null;
            }
        } else if (value instanceof Float) {
            float f = (Float) value;
            if (Float.isNaN(f) || Float.isInfinite(f)) {
                return null;
            }
        }
        return value;
    }
    
    /**
     * Create standardized error response
     */
    protected Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", errorMessage);
        response.put("_data_quality", new DataQualityReport());
        return response;
    }
    
    /**
     * Log agent analysis with context
     */
    protected void logAnalysis(String symbol, Map<String, Object> result) {
        String summary;
        if (result.containsKey("error")) {
            summary = "ERROR: " + result.get("error");
        } else {
            summary = "Result keys: " + String.join(", ", result.keySet());
        }
        Logger.debug("{}: {} - {}", name, symbol, summary);
    }
    
    /**
     * Get agent metadata
     */
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("name", name);
        meta.put("role", role);
        meta.put("created_at", createdAt.toString());
        return meta;
    }
    
    /**
     * Get agent name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get agent role
     */
    public String getRole() {
        return role;
    }
}
