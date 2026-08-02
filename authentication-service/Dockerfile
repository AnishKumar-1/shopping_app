# Step 1: Use an official Java 21 runtime image as the base
FROM eclipse-temurin:21-jre

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the built JAR file into the container
# Assumes the JAR is generated in the target folder after running 'mvn package'
COPY target/*.jar app.jar

# Step 4: Expose the port the auth service listens on (e.g., 8081 or 8080)
EXPOSE 8084

# Step 5: Command to run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]