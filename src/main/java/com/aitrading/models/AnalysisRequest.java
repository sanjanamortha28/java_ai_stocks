package com.aitrading.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * Analysis Request Model
 * Corresponds to Python AnalysisRequest Pydantic model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {
    
    @NotBlank(message = "Symbol is required")
    private String symbol;
    
    private String country = "USA";
    
    private String strategy = "balanced";
    
    private String riskLevel = "moderate";
    
    private int lookbackDays = 90;
    
    private boolean includeTechnical = true;
    
    private boolean includeFundamental = true;
    
    private boolean includeNews = true;
    
    private boolean includeOptions = false;
    
    private String language = "english";
    
    private String knowledgeLevel = "intermediate";
}
