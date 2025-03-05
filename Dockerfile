FROM openjdk:17-jdk-alpine
COPY /target/vot.e-1.2.0.jar /vote-backend.jar

# ENV HOST=0.0.0.0
ENV PORT=8090
EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/vote-backend.jar"]