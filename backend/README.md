# SunBank Backend

Backend do projeto SunBank, responsável pelo gerenciamento de operações bancárias através de uma API REST desenvolvida com Spring Boot.

## Tecnologias

- **Java 17**
- **Spring Boot 4.0.9-SNAPSHOT**
- **Spring Data JPA**
- **Spring Security**
- **Spring Web MVC**
- **PostgreSQL**
- **Maven**

## Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+ (ou usar o wrapper `./mvnw` incluso)
- PostgreSQL 12+ com banco de dados `sunbank` criado
- Credenciais de acesso ao banco configuradas em `src/main/resources/application.properties`

## Configuração

As configurações do banco de dados estão no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sunbank
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Ajuste a URL, usuário e senha conforme o seu ambiente.

## Como executar

Compile e execute a aplicação com Maven:

```bash
./mvnw spring-boot:run
```

Ou, se preferir usar Maven instalado:

```bash
mvn spring-boot:run
```

A aplicação será iniciada e estará disponível por padrão em `http://localhost:8080`.

## Estrutura do projeto

```
src/main/java/com/backend/
├── BackendApplication.java      # Ponto de entrada da aplicação
├── controller/
│   └── OperacaoController.java  # Endpoints REST para operações
├── entity/
│   └── Operacao.java            # Entidade JPA de operação bancária
├── repository/
│   └── OperacaoRepository.java  # Acesso ao banco de dados via Spring Data JPA
└── service/
    ├── OperacaoService.java     # Interface do serviço de operações
    └── OperacaoServiceImpl.java # Implementação do serviço
```

## Endpoints da API

Base URL: `http://localhost:8080/operacoes`

| Método | Endpoint      | Descrição                          |
|--------|---------------|------------------------------------|
| POST   | `/operacoes`  | Cria uma nova operação             |
| GET    | `/operacoes`  | Lista todas as operações           |
| PUT    | `/operacoes/{id}` | Atualiza uma operação existente |
| DELETE | `/operacoes/{id}` | Remove uma operação             |

### Exemplo de payload (POST /operacoes)

```json
{
  "tipo": "DEPOSITO",
  "valor": 1500.00,
  "contaOrigemId": 1,
  "contaDestinoId": 2,
  "descricao": "Depósito inicial"
}
```

### Tipos de operação

- `DEPOSITO`
- `SAQUE`
- `TRANSFERENCIA`
- `CONVERSAO`

### Status da operação

- `PENDENTE`
- `CONClUIDA`
- `CANCELADA`

> Atenção: o status `CONClUIDA` possui uma inconsistência de capitalização no enum (`CONClUIDA` em vez de `CONCLUIDA`).

## Banco de dados

A aplicação utiliza PostgreSQL. O Hibernate está configurado com `ddl-auto=update`, ou seja, as tabelas são criadas/atualizadas automaticamente com base nas entidades JPA.

## Testes

Para executar os testes:

```bash
./mvnw test
```

O projeto conta com um teste básico de carregamento de contexto em `src/test/java/com/backend/BackendApplicationTests.java`.

## Build e execução via JAR

Para compilar e gerar o JAR executável:

```bash
./mvnw clean package
```

Depois de gerado, execute o JAR na pasta `target`:

```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Variáveis de ambiente

Você pode sobrescrever as configurações do banco de dados usando variáveis de ambiente ao invés de editar o `application.properties`:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sunbank
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
```

Ou passar diretamente na execução:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:postgresql://localhost:5432/sunbank --spring.datasource.username=postgres --spring.datasource.password=postgres"
```

## Solução de problemas

| Problema | Possível causa | Solução |
|----------|----------------|---------|
| Falha ao conectar no banco | PostgreSQL não está rodando ou banco `sunbank` não existe | Verifique se o PostgreSQL está ativo e crie o banco `sunbank` |
| Erro de autenticação | Usuário ou senha incorretos | Confira as credenciais em `application.properties` ou variáveis de ambiente |
| Porta 8080 em uso | Outro processo está usando a porta | Altere a porta com `--server.port=8081` ou finalize o processo anterior |
| Spring Boot não inicia | Versão do JDK inferior a 17 | Instale e configure o JDK 17 ou superior |

## Referências

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [PostgreSQL](https://www.postgresql.org/)
