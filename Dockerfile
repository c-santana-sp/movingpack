# Use Eclipse Temurin Java 21 base image
FROM eclipse-temurin:21-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml for dependency resolution
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies only
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests

# ---- Stage 2: Minimal runtime image ----
FROM eclipse-temurin:21-jre-alpine

# Create user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership and drop to non-root user
RUN chown spring:spring app.jar
USER spring

# Expose application port
EXPOSE 8080

# Set default command
ENTRYPOINT ["java", "-jar", "app.jar"]
