package com.aitrading.api.controller;

import com.aitrading.api.dto.AnalysisRequestDTO;
import com.aitrading.api.dto.AnalysisResponseDTO;
import com.aitrading.api.dto.HealthCheckResponse;
import com.aitrading.api.dto.ErrorResponseDTO;
import com.aitrading.domain.models.AnalysisContext;
import com.aitrading.application.service.AnalysisService;
import com.aitrading.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.*;

/**
 * REST API Controller
 * Exposes the analysis engine via REST endpoints
 * 
 * Clean Architecture - API/Controller Layer (Boundary)
 * 
 * Endpoints:
 * - POST /analyze - Main analysis endpoint
 * - GET /health - System health check
 */
@RestController
@RequestMapping("/api/v1")
@Validated
@CrossOrigin(origins = "*")
public class AnalysisController {
    
    @Autowired
    private AnalysisService analysisService;
    
    /**
     * Main Analysis Endpoint
     * 
     * POST /api/v1/analyze
     * 
     * Request body: AnalysisRequestDTO
     * Response: Complete analysis with all agent outputs and CIO decision
     * 
     * Example:
     * ```json
     * {
     *   "symbol": "AAPL",
     *   "country": "USA",
     *   "strategy": "long_term_investment",
     *   "risk_level": "moderate"
     * }
     * ```
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeStock(
            @Valid @RequestBody AnalysisRequestDTO request,
            @RequestHeader(value = "X-Client-Type", required = false) String clientType) {
        
        try {
            Logger.info("Received analysis request: symbol={}, country={}, strategy={}",
                    request.getSymbol(), request.getCountry(), request.getStrategy());
            
            // Create domain context from DTO
            AnalysisContext context = new AnalysisContext();
            context.setSymbol(request.getSymbol());
            context.setCountry(request.getCountry());
            context.setStrategy(request.getStrategy());
            context.setRiskLevel(request.getRiskLevel());
            context.setLookbackDays(request.getLookbackDays());
            context.setIncludeFundamental(request.isIncludeFundamental());
            context.setIncludeTechnical(request.isIncludeTechnical());
            context.setIncludeNews(request.isIncludeNews());
            context.setIncludeOptions(request.isIncludeOptions());
            context.setKnowledgeLevel(request.getKnowledgeLevel());
            context.setLanguage(request.getLanguage());
            context.setIncludeEducation(request.isIncludeEducation());
            context.setIncludeDataLineage(request.isIncludeDataLineage());
            
            // Execute analysis (application service)
            long startTime = System.currentTimeMillis();
            Map<String, Object> analysisResult = analysisService.executeFullAnalysis(context);
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Build response
            AnalysisResponseDTO response = buildAnalysisResponse(request, analysisResult, executionTime);
            
            Logger.info("Analysis complete for {} ({}ms)", request.getSymbol(), executionTime);
            
            return ResponseEntity.ok()
                    .header("X-Analysis-ID", response.getAnalysisId())
                    .header("X-Execution-Time-MS", String.valueOf(executionTime))
                    .body(Map.of(
                            "status", "success",
                            "data", response
                    ));
            
        } catch (IllegalArgumentException e) {
            Logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    new ErrorResponseDTO(
                            400,
                            "VALIDATION_ERROR",
                            e.getMessage(),
                            Instant.now().toEpochMilli()
                    )
            );
        } catch (Exception e) {
            Logger.error("Analysis error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponseDTO(
                            500,
                            "ANALYSIS_ERROR",
                            e.getMessage(),
                            Instant.now().toEpochMilli()
                    )
            );
        }
    }
    
    /**
     * Health Check Endpoint
     * 
     * GET /api/v1/health
     * 
     * Returns system status and component availability
     */
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> health() {
        Map<String, Object> components = new HashMap<>();
        components.put("api", "UP");
        components.put("services", "UP");
        
        HealthCheckResponse response = new HealthCheckResponse(
                "UP",
                java.time.LocalDateTime.now().toString(),
                components
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Build analysis response from domain result
     */
    private AnalysisResponseDTO buildAnalysisResponse(
            AnalysisRequestDTO request,
            Map<String, Object> analysisResult,
            long executionTime) {
        
        return AnalysisResponseDTO.builder()
                .statusCode("200")
                .symbol(request.getSymbol())
                .analysisId(UUID.randomUUID().toString())
                .timestamp(Instant.now().toEpochMilli())
                .investmentDecision((String) analysisResult.getOrDefault("investment_decision", "HOLD"))
                .investmentConvictioLevel((Double) analysisResult.getOrDefault("investment_conviction_level", 50.0))
                .rationale((String) analysisResult.getOrDefault("rationale", ""))
                .fundamental_analysis((Map) analysisResult.getOrDefault("fundamental_analysis", new HashMap<>()))
                .technical_analysis((Map) analysisResult.getOrDefault("technical_analysis", new HashMap<>()))
                .news_intelligence((Map) analysisResult.getOrDefault("news_intelligence", new HashMap<>()))
                .risk_assessment((Map) analysisResult.getOrDefault("risk_assessment", new HashMap<>()))
                .strategy_construction((Map) analysisResult.getOrDefault("strategy_construction", new HashMap<>()))
                .execution_source((String) analysisResult.getOrDefault("_execution_source", "MOCK_LLM"))
                .execution_time_ms(executionTime)
                .data_quality_report((Map) analysisResult.getOrDefault("_data_quality", new HashMap<>()))
                .build();
    }
}
