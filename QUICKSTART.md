# Quick Start - Java AI Trading Platform

## Prerequisites

- Java 17+
- Maven 3.6+
- Redis (optional, for caching)

## 1. Clone and Setup

```bash
cd /Users/sanjanamortha/practice-java/java_ai_stocks
cp .env.example .env
```

Edit `.env` with your API keys:

```env
ANTHROPIC_API_KEY=your_key_here
ENABLE_LLM=true
```

## 2. Build the Project

```bash
# Using Maven
mvn clean install

# Or with the provided script
./build.sh
```

## 3. Run the Application

```bash
# Development environment
mvn spring-boot:run

# Or
./run.sh

# With Docker
docker-compose up
```

The API will be available at: `http://localhost:8080/api/v1`

## 4. Test the API

### Health Check
```bash
curl http://localhost:8080/api/v1/analyze/health
```

### Validate a Symbol
```bash
curl "http://localhost:8080/api/v1/analyze/validate?symbol=AAPL&country=USA"
```

### Analyze a Stock
```bash
curl -X POST http://localhost:8080/api/v1/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "AAPL",
    "country": "USA",
    "strategy": "balanced",
    "riskLevel": "moderate",
    "includeTechnical": true,
    "includeFundamental": true,
    "includeNews": true,
    "lookbackDays": 90
  }'
```

## Project Structure

```
java_ai_stocks/
├── src/main/java/com/aitrading/
│   ├── AiTradingPlatformApplication.java
│   ├── agents/              # Specialist analysis agents
│   ├── api/                 # REST endpoints
│   ├── config/              # Configuration classes
│   ├── models/              # Data models
│   ├── services/            # Business logic
│   ├── cache/               # Caching logic
│   └── utils/               # Utilities
├── src/main/resources/
│   └── application.yml      # Spring Boot config
├── pom.xml                  # Maven configuration
├── Dockerfile               # Docker build
└── docker-compose.yml       # Docker Compose config
```

## Key Agents

1. **Fundamental Analyst** - CFA Level III analysis
2. **Technical Analyst** - Institutional technical analysis
3. **News Intelligence** - Event-driven strategy
4. **Risk Management** - Hedge fund-level risk assessment
5. **Strategy Construction** - Quantitative strategy design
6. **Options Agent** - Options trading analysis
7. **Market Scanner** - Cross-market opportunity detection
8. **CIO Orchestrator** - Final investment decision

## Configuration

Edit `src/main/resources/application.yml`:

```yaml
app:
  enable-llm: false              # Enable Claude API
  force-file-cache: true         # Use file cache instead of Redis
  disable-cache: false           # Enable caching
  cache-expiry: 3600             # Cache TTL in seconds
  max-tokens: 2048               # LLM max tokens
  temperature: 0.7               # LLM temperature
```

## Environment Variables

```bash
# API Keys
ANTHROPIC_API_KEY=sk-...
ALPHA_VANTAGE_API_KEY=...
FMP_API_KEY=...
TWELVE_DATA_API_KEY=...

# Features
ENABLE_LLM=true|false
FORCE_FILE_CACHE=true|false
DISABLE_CACHE=true|false

# Redis
REDIS_URL=redis://localhost:6379/0

# Server
SERVER_PORT=8080
```

## Troubleshooting

### Build Fails
```bash
# Clean Maven cache
mvn clean
mvn install -DskipTests

# Check Java version
java -version  # Should be 17+
```

### Redis Connection Error
```bash
# Use file-based cache instead
export FORCE_FILE_CACHE=true

# Or install Redis
brew install redis          # macOS
```

### API Key Issues
```bash
# Check .env file
cat .env

# Ensure ANTHROPIC_API_KEY is set
export ANTHROPIC_API_KEY=your_key_here
```

## Development

### Running Tests
```bash
mvn test
```

### Adding New Agents
1. Extend `BaseAgent`  
2. Implement `analyze()` method
3. Add `@Service` annotation
4. Register in `AnalysisEngine`

### Debugging
```bash
# Enable debug logging
export SPRING_PROFILES_ACTIVE=debug

#Run with debug output
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

## Performance Optimization

- Cache fundamental data (30 min TTL)  
- Use Redis for distributed caching  
- Batch API calls
- Monitor LLM call costs

## Production Deployment

```bash
# Build production JAR
mvn clean package -DskipTests

# Run with container
docker build -t ai-trading:latest .
docker run -p 8080:8080 \
  -e ANTHROPIC_API_KEY=sk-... \
  -e ENABLE_LLM=true \
  ai-trading:latest
```

## Next Steps

- Configure API keys
- Enable Claude LLM (`ENABLE_LLM=true`)
- Set up Redis for production
- Monitor LLM call logs
- Backtest strategies
