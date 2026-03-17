#!/bin/bash

# Build script for Java AI Trading Platform

set -e

echo "Building AI Trading Research Platform (Java)..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Clean and build
echo "Running Maven clean install..."
mvn clean install -DskipTests

echo "Build completed successfully!"
echo "To run the application:"
echo "  mvn spring-boot:run"
echo ""
echo "Or with Docker:"
echo "  docker-compose up"
