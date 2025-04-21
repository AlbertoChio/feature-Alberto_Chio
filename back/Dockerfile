# Use OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy built JAR
COPY target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
