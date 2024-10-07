FROM openjdk:21-jdk
WORKDIR /app
COPY target/orderService-0.0.1-SNAPSHOT.jar orderservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "orderservice.jar"]