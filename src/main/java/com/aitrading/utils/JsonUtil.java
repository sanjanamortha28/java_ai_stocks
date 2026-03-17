package com.aitrading.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;

/**
 * JSON utility for common operations
 */
public class JsonUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Convert object to JSON string
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            Logger.error("Error converting to JSON: {}", e.getMessage());
            return "{}";
        }
    }
    
    /**
     * Convert JSON string to Map
     */
    public static Map<String, Object> toMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            Logger.error("Error converting JSON to Map: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
    
    /**
     * Sanitize for JSON (handle NaN and Inf)
     */
    public static Object sanitizeForJson(Object obj) {
        if (obj instanceof Double) {
            double d = (Double) obj;
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                return null;
            }
            return obj;
        } else if (obj instanceof Float) {
            float f = (Float) obj;
            if (Float.isNaN(f) || Float.isInfinite(f)) {
                return null;
            }
            return obj;
        }
        return obj;
    }
}
