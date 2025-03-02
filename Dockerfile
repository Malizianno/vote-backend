FROM openjdk:17-jdk-alpine
LABEL "vote-backend"="1.2.0"
COPY ../target/vot.e-1.2.0.jar vote-backend.jar

ENTRYPOINT ["java", "-jar", "/vote-backend.jar"]