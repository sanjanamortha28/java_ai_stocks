package com.aitrading.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

/**
 * Analysis Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisRequestDTO {
    
    @NotBlank(message = "Symbol is required")
    @Size(min = 1, max = 10, message = "Symbol must be 1-10 characters")
    private String symbol;
    
    @NotNull(message = "Country is required")
    private String country = "USA";
    
    @NotNull(message = "Strategy is required")
    private String strategy = "long_term_investment";
    
    private String riskLevel = "moderate";
    private int lookbackDays = 90;
    
    private boolean includeFundamental = true;
    private boolean includeTechnical = true;
    private boolean includeNews = true;
    private boolean includeOptions = false;
    
    private String language = "english";
    private String knowledgeLevel = "intermediate";
    
    private boolean includeEducation = true;
    private boolean includeDataLineage = true;
}
