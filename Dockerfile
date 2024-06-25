FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/FlightReservation-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} FlightReservation-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "FlightReservation-0.0.1-SNAPSHOT.jar"]
