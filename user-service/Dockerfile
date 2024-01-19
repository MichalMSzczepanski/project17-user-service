# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/

# Specify the default command to run on container startup
CMD ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar"]