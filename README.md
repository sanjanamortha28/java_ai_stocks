# AI Trading Research Platform - Java Version

This is a Java implementation of the AI Trading Research Platform, converted from the original Python FastAPI application.

## Overview

Institutional-Grade AI-Powered Trading Research System that performs comprehensive 360° financial analysis on stocks through a coordinated multi-agent system powered by Claude AI.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Cache**: Redis
- **Database**: SQLite
- **AI**: Anthropic Claude API
- **HTTP Client**: OkHttp

## Project Structure

```
src/main/java/com/aitrading/
├── AiTradingPlatformApplication.java
├── agents/
│   ├── BaseAgent.java
│   ├── FundamentalAnalystAgent.java
│   ├── TechnicalAnalystAgent.java
│   ├── OrchestratorAgent.java
│   └── ... (other agents)
├── api/
│   └── AnalysisController.java
├── config/
│   ├── ApplicationProperties.java
│   └── CacheConfig.java
├── models/
│   ├── AnalysisRequest.java
│   ├── DataQualityReport.java
│   └── ... (other models)
├── services/
│   ├── AnalysisEngine.java
│   ├── AnthropicClient.java
│   └── ... (other services)
├── cache/
│   └── ... (caching logic)
└── utils/
    └── Logger.java
```

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- Redis (optional, defaults to file-based cache)

### Installation

```bash
cd java_ai_stocks
mvn clean install
```

### Configuration

Create a `.env` file in the root directory:

```env
ANTHROPIC_API_KEY=your_api_key_here
ALPHA_VANTAGE_API_KEY=your_key
FMP_API_KEY=your_key
TWELVE_DATA_API_KEY=your_key
ENABLE_LLM=true
FORCE_FILE_CACHE=true
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api/v1`

## Endpoints

- **GET** `/analyze/health` - Health check
- **GET** `/analyze/validate?symbol=AAPL&country=USA` - Validate stock symbol
- **POST** `/analyze` - Analyze stock

### Example Analysis Request

```json
{
  "symbol": "AAPL",
  "country": "USA",
  "strategy": "balanced",
  "riskLevel": "moderate",
  "includeTechnical": true,
  "includeFundamental": true,
  "includeNews": true,
  "lookbackDays": 90
}
```

## Features

### 1. Multi-Agent System

- **Fundamental Analyst**: CFA Level III standard analysis
- **Technical Analyst**: Institutional-grade technical analysis
- **News Intelligence Agent**: Event-driven strategy
- **Risk Management Agent**: Hedge fund risk officer perspective
- **Strategy Agent**: Quantitative strategy construction
- **CIO Orchestrator**: Final investment decision

### 2. Data Quality Gates

All agent outputs include data quality reports with:
- Completeness percentage
- Quality grade (A/B/C/D/F)
- Confidence modifiers
- Suspicious field detection

### 3. Caching

- Redis-based caching (production)
- File-based fallback caching
- Configurable TTL

### 4. Performance Tracking

- LLM call tracking
- Execution time monitoring
- Performance metrics aggregation

## API Response Format

```json
{
  "symbol": "AAPL",
  "investment_conviction_level": 72,
  "recommendation": "BUY",
  "expected_return_range": {
    "low": -5.0,
    "high": 15.0
  },
  "risk_metrics": {
    "var_95": 8.5,
    "max_drawdown": 12.0
  },
  "_data_quality": {
    "completeness_pct": 95.0,
    "quality_grade": "A",
    "confidence_modifier": 1.0
  }
}
```

## Configuration Properties

Located in `src/main/resources/application.yml`:

- `app.enable-llm`: Enable/disable Claude API calls (default: false)
- `app.force-file-cache`: Use file cache instead of Redis (default: true)
- `app.cache-expiry`: Cache expiration time in seconds (default: 3600)
- `app.max-tokens`: Max tokens in LLM requests (default: 2048)
- `app.temperature`: LLM temperature parameter (default: 0.7)

## Development

### Running Tests

```bash
mvn test
```

### Building JAR

```bash
mvn clean package
```

### Docker Deployment

```bash
docker build -t ai-trading-platform .
docker run -p 8080:8080 ai-trading-platform
```

## Key Differences from Python Version

1. **Dependency Management**: Maven replaces pip
2. **Type Safety**: Strongly typed Java classes replace dynamic Python dicts
3. **Spring Boot**: Replaces FastAPI for REST API
4. **Async Operations**: Spring async/reactive features
5. **Configuration**: YAML-based application properties
6. **Logging**: SLF4J/Logback replaces Python logging

## Port Progress

### Completed ✓
- [x] Maven project structure
- [x] Spring Boot application setup
- [x] Configuration management
- [x] Base Agent classes
- [x] Fundamental Analyst
- [x] Technical Analyst
- [x] Orchestrator
- [x] REST API endpoints
- [x] Data models

### In Progress
- [ ] News Intelligence Agent
- [ ] Risk Management Agent
- [ ] Strategy Construction Agent
- [ ] Market Data Services
- [ ] Cache management
- [ ] Performance tracking
- [ ] Additional providers

## Contributing

To add new agents or services:

1. Extend `BaseAgent`
2. Implement the `analyze()` method
3. Add data quality reporting
4. Register in `AnalysisEngine`

## License

Same as original Python version

## Notes

- Mock responses are returned when LLM is disabled
- All financial analysis is probabilistic - no guaranteed profits
- This system is for educational and research purposes
