# =====================================
# Stage 1: Build Spring Boot application
# =====================================

#Creates the build environment with Maven and Java 21
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy application source code
COPY src ./src

# Generate executable JAR
RUN mvn clean package -DskipTests

# =====================================
# Stage 2: Run Spring Boot application
# =====================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy JAR from the first stage
COPY --from=build /app/target/*.jar app.jar

# Application port
EXPOSE 8081

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]

