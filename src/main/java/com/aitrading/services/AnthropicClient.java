package com.aitrading.services;

import com.aitrading.utils.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Anthropic Claude API Client
 * Handles communication with Claude AI
 * Corresponds to Python AnthropicClient
 */
@Service
public class AnthropicClient {
    
    private static final String ANTHROPIC_API_URL = "https://api.anthropic.com/v1/messages";
    private static final String ANTHROPIC_MODEL = "claude-3-5-sonnet-20241022";
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public AnthropicClient() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Send a message to Claude and get response
     */
    public String sendMessage(String userMessage, int maxTokens) {
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        boolean enableLlm = "true".equalsIgnoreCase(System.getenv("ENABLE_LLM"));
        
        if (!enableLlm) {
            Logger.info("LLM disabled - returning mock response");
            return getMockResponse(userMessage);
        }
        
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalArgumentException("Anthropic API key not configured");
            }
            
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", ANTHROPIC_MODEL);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", 0.7);
            
            List<Map<String, Object>> messages = new ArrayList<>();
            Map<String, Object> message = new LinkedHashMap<>();
            message.put("role", "user");
            message.put("content", userMessage);
            messages.add(message);
            
            requestBody.put("messages", messages);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            
            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(ANTHROPIC_API_URL)
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .header("content-type", "application/json")
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    Logger.error("Anthropic API error: {}", response.code());
                    return getMockResponse(userMessage);
                }
                
                String responseBody = response.body().string();
                Map responseMap = objectMapper.readValue(responseBody, Map.class);
                List<Map> content = (List<Map>) responseMap.get("content");
                if (!content.isEmpty()) {
                    return (String) content.get(0).get("text");
                }
            }
        } catch (Exception e) {
            Logger.error("Error calling Anthropic API: {}", e.getMessage());
            return getMockResponse(userMessage);
        }
        
        return getMockResponse(userMessage);
    }
    
    /**
     * Send a message to Claude expecting JSON response
     */
    public Map<String, Object> sendMessageJson(String userMessage, int maxTokens) {
        String response = sendMessage(userMessage, maxTokens);
        try {
            return objectMapper.readValue(response, Map.class);
        } catch (Exception e) {
            Logger.error("Error parsing JSON response: {}", e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * Mock response for development/testing
     */
    private String getMockResponse(String message) {
        Map<String, Object> mockResponse = new LinkedHashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("message", "Mock response");
        mockResponse.put("investment_conviction_level", 50);
        try {
            return objectMapper.writeValueAsString(mockResponse);
        } catch (Exception e) {
            return "{}";
        }
    }
}
