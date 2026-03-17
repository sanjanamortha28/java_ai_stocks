package com.aitrading.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

/**
 * Structured data quality assessment attached to every agent output
 * Corresponds to Python DataQualityReport class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataQualityReport {
    
    private int totalFields = 0;
    private int presentFields = 0;
    private List<String> missingFields = new ArrayList<>();
    private List<SuspiciousField> suspiciousFields = new ArrayList<>();
    private List<String> notes = new ArrayList<>();
    
    /**
     * Calculate completeness percentage
     */
    public double getCompletenessPercentage() {
        if (totalFields == 0) {
            return 0.0;
        }
        return (presentFields / (double) totalFields) * 100;
    }
    
    /**
     * Get quality grade based on completeness and suspicious count
     * A/B/C/D/F grading system
     */
    public String getQualityGrade() {
        double completeness = getCompletenessPercentage();
        int penalty = suspiciousFields.size() * 5;
        double score = Math.max(completeness - penalty, 0);
        
        if (score >= 90) return "A";
        if (score >= 75) return "B";
        if (score >= 60) return "C";
        if (score >= 40) return "D";
        return "F";
    }
    
    /**
     * Get confidence modifier based on quality grade
     */
    public double getConfidenceModifier() {
        return switch (getQualityGrade()) {
            case "A" -> 1.0;
            case "B" -> 0.85;
            case "C" -> 0.65;
            case "D" -> 0.45;
            case "F" -> 0.30;
            default -> 0.5;
        };
    }
    
    /**
     * Convert to dictionary for JSON serialization
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("completeness_pct", Math.round(getCompletenessPercentage() * 10.0) / 10.0);
        map.put("quality_grade", getQualityGrade());
        map.put("confidence_modifier", getConfidenceModifier());
        map.put("total_fields", totalFields);
        map.put("present_fields", presentFields);
        map.put("missing_fields", missingFields);
        map.put("suspicious_fields", suspiciousFields);
        map.put("notes", notes);
        return map;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuspiciousField {
        private String field;
        private String reason;
    }
}
