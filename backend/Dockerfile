FROM openjdk:11-jre-slim
ARG JAR_FILE=app-target/cinema-tickets-core-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} cinema-tickets-core.jar
ENTRYPOINT ["java","-jar","/cinema-tickets-core.jar"]