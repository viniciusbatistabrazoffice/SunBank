# syntax=docker/dockerfile:1

FROM maven:3.8.3-openjdk-16-slim AS build
WORKDIR /app

# Baixa dependências primeiro para aproveitar cache de camadas
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM adoptopenjdk/openjdk16:jre-16.0.1_9-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
