package com.aitrading.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * Application configuration properties
 * Corresponds to Python Settings class
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class ApplicationProperties {
    
    private String name;
    private String environment;
    private boolean debug;
    private String logLevel;
    private int apiPort;
    private String apiHost;
    
    // API Keys
    private String anthropicApiKey;
    private String alphaVantageApiKey;
    private String fmpApiKey;
    private String twelveDataApiKey;
    
    // Cache & Retry
    private int cacheExpiry;
    private int maxRetries;
    private int retryDelay;
    
    // Model Configuration
    private int maxTokens;
    private double temperature;
    
    // Feature Flags
    private boolean enableLlm;
    private boolean forceFileCache;
    private boolean disableCache;
    private String redisUrl;
    
    /**
     * Validate critical configuration
     */
    public void validate() {
        if (enableLlm && (anthropicApiKey == null || anthropicApiKey.isEmpty())) {
            throw new IllegalArgumentException("ANTHROPIC_API_KEY not configured but ENABLE_LLM is true");
        }
    }
}
