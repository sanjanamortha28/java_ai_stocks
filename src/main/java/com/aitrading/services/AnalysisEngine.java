package com.aitrading.services;

import com.aitrading.agents.*;
import com.aitrading.models.AnalysisRequest;
import com.aitrading.utils.Logger;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Analysis Engine
 * Orchestrates all agents and produces final analysis
 * Corresponds to Python AnalysisEngine class
 */
@Service
public class AnalysisEngine {
    
    private final FundamentalAnalystAgent fundamentalAgent;
    private final TechnicalAnalystAgent technicalAgent;
    private final OrchestratorAgent orchestratorAgent;
    
    public AnalysisEngine() {
        this.fundamentalAgent = new FundamentalAnalystAgent();
        this.technicalAgent = new TechnicalAnalystAgent();
        this.orchestratorAgent = new OrchestratorAgent();
    }
    
    /**
     * Perform comprehensive analysis
     */
    public Map<String, Object> analyze(AnalysisRequest request) {
        Logger.info("Starting analysis for: {}", request.getSymbol());
        
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("symbol", request.getSymbol());
        context.put("country", request.getCountry());
        context.put("strategy", request.getStrategy());
        context.put("riskLevel", request.getRiskLevel());
        context.put("lookbackDays", request.getLookbackDays());
        
        // Perform analysis
        if (request.isIncludeFundamental()) {
            Map<String, Object> fundamentalAnalysis = fundamentalAgent.analyze(context);
            context.put("fundamental_analysis", fundamentalAnalysis);
        }
        
        if (request.isIncludeTechnical()) {
            Map<String, Object> technicalAnalysis = technicalAgent.analyze(context);
            context.put("technical_analysis", technicalAnalysis);
        }
        
        // Get final orchestrated result
        Map<String, Object> finalResult = orchestratorAgent.analyze(context);
        
        Logger.info("Analysis completed for: {}", request.getSymbol());
        return finalResult;
    }
}
