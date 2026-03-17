package com.aitrading.agents;

import com.aitrading.services.AnthropicClient;
import com.aitrading.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/**
 * Chief Investment Officer Orchestrator Agent
 * Coordinates all agents and produces final investment decision
 * Corresponds to Python OrchestratorAgent
 */
public class OrchestratorAgent extends BaseAgent {
    
    @Autowired
    private AnthropicClient anthropicClient;
    
    public OrchestratorAgent() {
        super("CIOOrchestrator", "Chief Investment Officer");
    }
    
    @Override
    public Map<String, Object> analyze(Map<String, Object> context) {
        if (!validateInput(context)) {
            return Map.of("error", "Invalid context");
        }
        
        String symbol = (String) context.get("symbol");
        Logger.info("Orchestrating final decision for: {}", symbol);
        
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            
            // Aggregate insights from all agents
            Map<String, Object> fundamentalAnalysis = 
                (Map<String, Object>) context.getOrDefault("fundamental_analysis", new HashMap<>());
            Map<String, Object> technicalAnalysis = 
                (Map<String, Object>) context.getOrDefault("technical_analysis", new HashMap<>());
            
            // Calculate final recommendation
            int fundamentalConviction = ((Number) fundamentalAnalysis.getOrDefault("conviction_level", 50)).intValue();
            int technicalConviction = ((Number) technicalAnalysis.getOrDefault("conviction_level", 50)).intValue();
            int finalConviction = (fundamentalConviction + technicalConviction) / 2;
            
            result.put("symbol", symbol);
            result.put("investment_conviction_level", finalConviction);
            result.put("recommendation", getRecommendation(finalConviction));
            result.put("expected_return_range", new HashMap<String, Double>() {{
                put("low", -5.0);
                put("high", 15.0);
            }});
            result.put("risk_metrics", new HashMap<String, Object>() {{
                put("var_95", 8.5);
                put("max_drawdown", 12.0);
            }});
            
            logAnalysis(symbol, result);
            return result;
            
        } catch (Exception e) {
            Logger.error("Orchestrator error for {}: {}", symbol, e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }
    
    private String getRecommendation(int conviction) {
        if (conviction >= 70) return "STRONG_BUY";
        if (conviction >= 55) return "BUY";
        if (conviction >= 45) return "HOLD";
        if (conviction >= 30) return "SELL";
        return "STRONG_SELL";
    }
}
