package com.aitrading.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

/**
 * Domain Model: Data Quality Report
 * Institutional-grade data quality assessment attached to every agent output.
 * Ensures traceability and confidence scoring at every decision point.
 * 
 * Clean Architecture - Domain Layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataQualityReport {
    
    // ═══════════════════ COMPLETENESS METRICS ═══════════════════
    
    private int totalFields;
    private int presentFields;
    
    private List<String> missingFields = new ArrayList<>();
    private List<SuspiciousField> suspiciousFields = new ArrayList<>();
    private List<String> notes = new ArrayList<>();
    
    // ═══════════════════ COMPUTED PROPERTIES ═══════════════════
    
    /**
     * Calculate completeness as percentage
     * 0.0 = no data, 100.0 = all fields present
     */
    public double getCompletenessPercentage() {
        if (totalFields == 0) return 0.0;
        return (presentFields / (double) totalFields) * 100.0;
    }
    
    /**
     * Institutional-grade quality scoring: A/B/C/D/F
     * 
     * A (90+): Excellent data, use with full confidence
     * B (75-89): Good data, minor gaps
     * C (60-74): Acceptable, moderate gaps
     * D (40-59): Poor, significant gaps
     * F (<40): Insufficient data, use with caution
     */
    public String getQualityGrade() {
        double completeness = getCompletenessPercentage();
        int penalty = suspiciousFields.size() * 5;  // Each suspicious field costs 5 points
        double score = Math.max(completeness - penalty, 0);
        
        if (score >= 90) return "A";
        if (score >= 75) return "B";
        if (score >= 60) return "C";
        if (score >= 40) return "D";
        return "F";
    }
    
    /**
     * Confidence modifier for CIO (Chief Investment Officer) decision-making
     * Scales conviction levels based on data quality
     * 
     * A → 1.00 (full confidence, no adjustment)
     * B → 0.85 (minor adjustment)
     * C → 0.65 (moderate adjustment)  
     * D → 0.45 (significant caution)
     * F → 0.30 (major caution, use as secondary signal only)
     * 
     * Example: If CIO conviction is 70/100 and data quality is C,
     * adjusted conviction = 70 * 0.65 = 45.5/100
     */
    public double getConfidenceModifier() {
        String grade = getQualityGrade();
        return switch(grade) {
            case "A" -> 1.00;
            case "B" -> 0.85;
            case "C" -> 0.65;
            case "D" -> 0.45;
            case "F" -> 0.30;
            default -> 0.50;
        };
    }
    
    /**
     * Convert to API response format
     */
    public Map<String, Object> toMap() {
        return Map.ofEntries(
            Map.entry("completeness_pct", Math.round(getCompletenessPercentage() * 10.0) / 10.0),
            Map.entry("quality_grade", getQualityGrade()),
            Map.entry("confidence_modifier", Math.round(getConfidenceModifier() * 100.0) / 100.0),
            Map.entry("total_fields", totalFields),
            Map.entry("present_fields", presentFields),
            Map.entry("missing_fields", missingFields),
            Map.entry("suspicious_fields", suspiciousFields.stream()
                .map(sf -> Map.of(
                    "field", sf.field,
                    "reason", sf.reason
                )).toList()),
            Map.entry("notes", notes)
        );
    }
    
    /**
     * Nested class for suspicious data points
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuspiciousField {
        public String field;
        public String reason;
    }
    
    /**
     * Builder method for quality assessment
     */
    public static DataQualityReportBuilder withFields(int total, int present) {
        return builder()
            .totalFields(total)
            .presentFields(present);
    }
}
