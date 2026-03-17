package com.aitrading.application.service;

import com.aitrading.domain.models.AnalysisContext;
import com.aitrading.domain.models.DataQualityReport;
import com.aitrading.domain.agents.*;
import com.aitrading.domain.service.MarketDataService;
import com.aitrading.utils.Logger;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;

/**
 * Analysis Service
 * Application layer - coordinates domain services and agents
 * 
 * Clean Architecture: Application Layer (Use Cases)
 * 
 * Responsibilities:
 * - Orchestrate agent execution
 * - Aggregate results
 * - Calculate final CIO decision
 * - Track execution metrics
 */
@Service
public class AnalysisService {
    
    private final TechnicalAnalystAgent technicalAgent;
    private final FundamentalAnalystAgent fundamentalAgent;
    private final NewsIntelligenceAgent newsAgent;
    private final RiskManagementAgent riskAgent;
    private final StrategyConstructionAgent strategyAgent;
    private final OrchestratorAgent orchestratorAgent;
    
    private final MarketDataService marketDataService;
    
    public AnalysisService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
        
        // Initialize all agents
        this.technicalAgent = new TechnicalAnalystAgent();
        this.fundamentalAgent = new FundamentalAnalystAgent();
        this.newsAgent = new NewsIntelligenceAgent();
        this.riskAgent = new RiskManagementAgent();
        this.strategyAgent = new StrategyConstructionAgent();
        this.orchestratorAgent = new OrchestratorAgent();
    }
    
    /**
     * Execute full analysis pipeline
     * 
     * Process:
     * 1. Load market data
     * 2. Run specialist agents in parallel where possible
     * 3. Aggregate quality reports
     * 4. Run orchestrator for CIO decision
     * 5. Compile final report
     */
    public Map<String, Object> executeFullAnalysis(AnalysisContext context) {
        long pipelineStart = System.currentTimeMillis();
        
        try {
            Logger.info("=== Starting Analysis Pipeline for {} ===", context.getSymbol());
            context.logExecution("Pipeline started");
            
            // ───────────────────────────────────────────────────────────
            // Phase 1: Load Market Data
            // ───────────────────────────────────────────────────────────
            Logger.info("Phase 1: Loading market data for {}", context.getSymbol());
            loadMarketData(context);
            context.logExecution("Market data loaded");
            
            // ───────────────────────────────────────────────────────────
            // Phase 2: Parallel Agent Execution
            // ───────────────────────────────────────────────────────────
            ExecutorService executor = Executors.newFixedThreadPool(5);
            Map<String, Future<Map<String, Object>>> agentResults = new HashMap<>();
            
            Logger.info("Phase 2: Launching specialist agents");
            
            if (context.isIncludeTechnical())
                agentResults.put("technical", executor.submit(() -> 
                    technicalAgent.executeAnalysis(context)));
            
            if (context.isIncludeFundamental())
                agentResults.put("fundamental", executor.submit(() -> 
                    fundamentalAgent.executeAnalysis(context)));
            
            if (context.isIncludeNews())
                agentResults.put("news", executor.submit(() -> 
                    newsAgent.executeAnalysis(context)));
            
            if (context.isIncludeRiskManagement())
                agentResults.put("risk", executor.submit(() -> 
                    riskAgent.executeAnalysis(context)));
            
            // Wait for all agents to complete
            agentResults.values().forEach(future -> {
                try {
                    future.get(30, TimeUnit.SECONDS);
                } catch (Exception e) {
                    Logger.error("Agent execution timeout or error: {}", e.getMessage());
                }
            });
            
            executor.shutdown();
            context.logExecution("All specialist agents completed");
            
            // ───────────────────────────────────────────────────────────
            // Phase 3: Strategy Construction
            // ───────────────────────────────────────────────────────────
            Logger.info("Phase 3: Constructing strategy");
            Map<String, Object> strategyResult = strategyAgent.executeAnalysis(context);
            context.logExecution("Strategy construction completed");
            
            // ───────────────────────────────────────────────────────────
            // Phase 4: CIO Orchestration
            // ───────────────────────────────────────────────────────────
            Logger.info("Phase 4: CIO Decision Making");
            Map<String, Object> cioDecision = orchestratorAgent.executeAnalysis(context);
            context.logExecution("CIO orchestration completed");
            
            // ───────────────────────────────────────────────────────────
            // Phase 5: Compile Final Report
            // ───────────────────────────────────────────────────────────
            long pipelineTime = System.currentTimeMillis() - pipelineStart;
            context.setTotalExecutionTimeMs(pipelineTime);
            context.setExecutionSource("MOCK_LLM");  // Will be REAL_LLM when Spring AI is integrated
            
            Map<String, Object> finalReport = compileFinalReport(context, cioDecision);
            
            Logger.info("=== Analysis Complete for {} ({}ms) ===", 
                    context.getSymbol(), pipelineTime);
            
            return finalReport;
            
        } catch (Exception e) {
            Logger.error("Pipeline error: {}", e.getMessage(), e);
            context.logExecution("Pipeline FAILED: " + e.getMessage());
            return Map.of(
                    "error", e.getMessage(),
                    "_execution_log", context.getExecutionLog()
            );
        }
    }
    
    /**
     * Load market data for analysis
     */
    private void loadMarketData(AnalysisContext context) {
        try {
            // In production, fetch from real market data providers
            Map<String, Object> marketData = new HashMap<>();
            marketData.put("current_price", 150.25);
            marketData.put("market_cap", 2500000000000L);
            marketData.put("pe_ratio", 25.5);
            marketData.put("dividend_yield", 0.85);
            
            context.setMarketData(marketData);
        } catch (Exception e) {
            Logger.warn("Failed to load market data: ", e.getMessage());
        }
    }
    
    /**
     * Compile final analysis report
     */
    private Map<String, Object> compileFinalReport(
            AnalysisContext context,
            Map<String, Object> cioDecision) {
        
        Map<String, Object> report = new LinkedHashMap<>();
        
        // Header
        report.put("symbol", context.getSymbol());
        report.put("analysis_id", context.getAnalysisId());
        report.put("timestamp", System.currentTimeMillis());
        
        // CIO Decision (primary output)
        report.put("investment_decision", cioDecision.getOrDefault("investment_decision", "HOLD"));
        report.put("investment_conviction_level", cioDecision.getOrDefault("investment_conviction_level", 50.0));
        report.put("rationale", cioDecision.getOrDefault("rationale", ""));
        
        // Component analyses
        report.put("fundamental_analysis", context.getFundamentalAnalysis());
        report.put("technical_analysis", context.getTechnicalAnalysis());
        report.put("news_intelligence", context.getNewsIntelligence());
        report.put("risk_assessment", context.getRiskAssessment());
        report.put("strategy_construction", context.getStrategyConstruction());
        
        // Data quality tracking
        Map<String, Object> qualityAggregate = new HashMap<>();
        qualityAggregate.put("reports", context.getAgentQualityReports());
        qualityAggregate.put("average_modifier", context.getAverageQualityModifier());
        
        report.put("_data_quality", qualityAggregate);
        report.put("_execution_source", context.getExecutionSource());
        report.put("_execution_time_ms", context.getTotalExecutionTimeMs());
        report.put("_execution_log", context.getExecutionLog());
        
        return report;
    }
}
