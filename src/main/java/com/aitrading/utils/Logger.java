package com.aitrading.utils;

import org.slf4j.LoggerFactory;

/**
 * Logging utility
 * Provides consistent logging across the application
 */
public class Logger {
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("AiTradingApp");
    
    public static void debug(String message, Object... args) {
        logger.debug(message, args);
    }
    
    public static void info(String message, Object... args) {
        logger.info(message, args);
    }
    
    public static void warn(String message, Object... args) {
        logger.warn(message, args);
    }
    
    public static void error(String message, Object... args) {
        logger.error(message, args);
    }
    
    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
