# Stage 1: Build
FROM maven:3.8.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar vote-backend.jar
ENV PORT=8090
EXPOSE 8090
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "vote-backend.jar"]
