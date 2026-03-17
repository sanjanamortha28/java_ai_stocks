package com.aitrading.cache;

import com.aitrading.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Cache Manager
 * Handles caching for analysis results and market data
 * Corresponds to Python CacheManager
 */
@Service
public class AnalysisCacheManager {
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    private final Map<String, CachedItem> fileCache = new HashMap<>();
    
    /**
     * Get cached value
     */
    public Object get(String key) {
        try {
            if (redisTemplate != null) {
                return redisTemplate.opsForValue().get(key);
            }
        } catch (Exception e) {
            Logger.warn("Redis get failed, using file cache: {}", e.getMessage());
        }
        
        CachedItem item = fileCache.get(key);
        if (item != null && !item.isExpired()) {
            return item.getValue();
        }
        
        return null;
    }
    
    /**
     * Set cached value
     */
    public void set(String key, Object value, int ttlSeconds) {
        try {
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
                return;
            }
        } catch (Exception e) {
            Logger.warn("Redis set failed, using file cache: {}", e.getMessage());
        }
        
        fileCache.put(key, new CachedItem(value, ttlSeconds));
    }
    
    /**
     * Clear cache
     */
    @CacheEvict(allEntries = true)
    public void clear() {
        Logger.info("Clearing cache");
        fileCache.clear();
    }
    
    /**
     * Clear specific key
     */
    public void clearKey(String key) {
        fileCache.remove(key);
        if (redisTemplate != null) {
            redisTemplate.delete(key);
        }
    }
    
    /**
     * Get cache stats
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("file_cache_size", fileCache.size());
        stats.put("timestamp", System.currentTimeMillis());
        return stats;
    }
    
    /**
     * Inner class for file-based cache items
     */
    private static class CachedItem {
        private final Object value;
        private final long expirationTime;
        
        public CachedItem(Object value, int ttlSeconds) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
        }
        
        public Object getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
