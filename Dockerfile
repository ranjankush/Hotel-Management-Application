# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests && \
    rm -rf ~/.m2/repository

# Run Stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/HotelManagement-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085

# Create a non-root user
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

# Run the application
CMD ["java", "-jar", "app.jar"]
