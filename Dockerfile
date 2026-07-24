# Stage 1: Build application using Maven & Temurin JDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY demo-website demo-website
COPY mcp-server mcp-server
COPY testing-scenarios testing-scenarios
RUN mvn clean package -DskipTests

# Stage 2: Minimal JRE 21 Runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/demo-website/target/demo-website-1.0.0-SNAPSHOT.jar /app/demo-website.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo-website.jar"]
