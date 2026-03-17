package com.aitrading.agents;

import com.aitrading.models.DataQualityReport;
import com.aitrading.utils.Logger;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Base Agent Class
 * Abstract base class for all trading research agents
 * Corresponds to Python BaseAgent class
 */
public abstract class BaseAgent {
    
    protected String name;
    protected String role;
    protected LocalDateTime createdAt;
    
    public BaseAgent(String name, String role) {
        this.name = name;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Main analysis method - must be implemented by subclasses
     * 
     * @param context Analysis context containing symbol and market data
     * @return Analysis result
     */
    public abstract Map<String, Object> analyze(Map<String, Object> context);
    
    /**
     * Validate input context
     */
    protected boolean validateInput(Map<String, Object> context) {
        if (context == null) {
            Logger.warn("Context is null");
            return false;
        }
        if (!context.containsKey("symbol")) {
            Logger.warn("Symbol not found in context");
            return false;
        }
        return true;
    }
    
    /**
     * Log analysis results
     */
    protected void logAnalysis(String symbol, Map<String, Object> result) {
        Logger.info("Agent {} analyzed {}: conviction={}", 
            name, symbol, result.get("investment_conviction_level"));
    }
    
    /**
     * Get agent information
     */
    public Map<String, Object> getInfo() {
        return Map.of(
            "name", name,
            "role", role,
            "created_at", createdAt.toString()
        );
    }
}
