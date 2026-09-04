FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia o POM primeiro para aproveitar o cache de camadas do Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e empacota o projeto
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre

WORKDIR /app

# Cria usuário não-root para execução
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Copia o artefato gerado
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar

USER appuser

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
