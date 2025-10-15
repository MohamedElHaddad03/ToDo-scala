FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/scala-2.12/*.jar app.jar

CMD ["java", "-jar", "app.jar"]

EXPOSE 8080