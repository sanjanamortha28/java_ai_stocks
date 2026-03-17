FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

# Create necessary directories
RUN mkdir -p /app/data/cache /app/data/performance /app/logs

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
