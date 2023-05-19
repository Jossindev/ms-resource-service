FROM openjdk:17-jdk-slim

# Add a volume pointing to /tmp
VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=build/libs/ms-resource-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} my-app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/my-app.jar"]
