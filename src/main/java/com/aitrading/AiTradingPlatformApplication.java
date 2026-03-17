package com.aitrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class AiTradingPlatformApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(AiTradingPlatformApplication.class);

    public static void main(String[] args) {
        // Ensure required directories exist
        try {
            Files.createDirectories(Paths.get("./data/cache"));
            Files.createDirectories(Paths.get("./data/performance"));
            Files.createDirectories(Paths.get("./logs"));
        } catch (Exception e) {
            logger.warn("Could not create directories: {}", e.getMessage());
        }

        SpringApplication.run(AiTradingPlatformApplication.class, args);
        logger.info("AI Trading Research Platform started successfully");
    }
}
