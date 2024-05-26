FROM openjdk:21-jdk-slim

WORKDIR /usr/src/app

COPY target/gateway-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]
