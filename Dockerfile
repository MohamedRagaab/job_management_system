# Use OpenJDK 21 as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged application JAR file into the container
COPY build/libs/job_management_system-0.0.1-SNAPSHOT.jar /app/job_management_system-0.0.1-SNAPSHOT.jar

# Copy the executableTasks directory into the container
COPY executableTasks /app/executableTasks

# Expose the port your application will run on (if necessary)
EXPOSE 8080

# Command to run your application
CMD ["java", "-jar", "/app/job_management_system-0.0.1-SNAPSHOT.jar"]
