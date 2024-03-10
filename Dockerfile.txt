# Use a Maven image with JDK 17
FROM maven:3.8.4-openjdk-17-slim as build

# Set the working directory in the Docker
WORKDIR /app

# Copy pom.xml , .mvn folder
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw.cmd ./

# Copy the application's source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Use JDK 17 image for runtime
FROM openjdk:17-jdk-slim

# Set the working directory in the Docker
WORKDIR /app

# Copy the built jar file
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 9060

# Run the jar file
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=default", "-Dserver.port=9060", "app.jar"]
