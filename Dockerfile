FROM openjdk:21-ea-23-jdk-bullseye

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/java-api-boilerplate-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
