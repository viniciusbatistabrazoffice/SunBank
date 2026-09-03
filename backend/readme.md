# SunBank Backend

Este diretório contém a aplicação backend do SunBank, desenvolvida com Spring Boot.

Para a documentação completa do projeto — arquitetura, endpoints, modelos de dados, variáveis de ambiente e solução de problemas — consulte o [README.md](../README.md) na raiz do repositório.

## Início rápido

### Requisitos

- JDK 17+
- Maven 3.6+ (ou `./mvnw` incluso)
- Docker / Docker Compose (recomendado) ou PostgreSQL 12+

### Subir com Docker Compose

```bash
docker compose up -d --build
```

A API estará disponível em `http://localhost:9090`.

### Subir localmente

1. Crie o banco `sunbank` no PostgreSQL (ou execute `./run.sh`, que cria o banco via `psql` quando disponível).
2. Ajuste `src/main/resources/application.properties` se necessário.
3. Execute:

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:9090`.

## Testes

```bash
./mvnw test
```

## Build

```bash
./mvnw clean package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Estrutura

```
src/main/java/com/backend/
├── BackendApplication.java    # Ponto de entrada
├── controller/                # REST controllers
├── entity/                    # Entidades JPA
├── repository/                # Repositórios Spring Data JPA
└── service/                   # Lógica de negócio e implementações
```

> Para detalhes da API REST e payloads, consulte a [documentação principal](../README.md).
