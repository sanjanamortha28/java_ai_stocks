package com.aitrading.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Analysis Response Model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {
    private String symbol;
    private String status;
    private int investmentConvictionLevel;
    private String recommendation;
    private ExpectedReturn expectedReturnRange;
    private RiskMetrics riskMetrics;
    private DataQualityReport dataQuality;
    private long timestamp;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpectedReturn {
        private double low;
        private double high;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskMetrics {
        private double var95;
        private double var99;
        private double maxDrawdown;
    }
}
