#!/bin/bash

# Run script for Java AI Trading Platform

echo "Starting AI Trading Research Platform..."

# Check if .env file exists
if [ ! -f .env ]; then
    echo "Warning: .env file not found. Please create it with your API keys."
    echo "Example:"
    echo "  ANTHROPIC_API_KEY=sk-..."
    echo "  ENABLE_LLM=false"
fi

# Run with Maven
mvn spring-boot:run
