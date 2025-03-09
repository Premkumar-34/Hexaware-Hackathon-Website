# Use an official JDK runtime as a parent image
FROM openjdk:21

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper files and source code
COPY . .

# Build the application
RUN ./mvnw clean package

# Run the application
CMD ["java", "-jar", "target/recruiting-application-0.0.1-SNAPSHOT.jar"]
