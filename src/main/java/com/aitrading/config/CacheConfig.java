package com.aitrading.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration
 */
@Configuration
@EnableCaching
public class  CacheConfig {
    // Cache configuration will be handled by Spring Boot auto-configuration
}
