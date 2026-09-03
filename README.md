# SunBank Backend

Backend do projeto SunBank, responsável pela reutilização de criptomoedas e transferência/conversão dos valores para reais (BRL). Oferece autenticação, cadastro de clientes e registro de operações bancárias via API REST com Spring Boot.

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
- PostgreSQL 12+ com usuário `postgres`
- Banco de dados `sunbank` criado (o script `./run.sh` cria automaticamente)
- Credenciais de acesso ao banco configuradas em `src/main/resources/application.properties`

## Configuração

As configurações do banco de dados estão no arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=backend

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/sunbank
spring.datasource.username=postgres
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Ajuste a URL, usuário e senha conforme o seu ambiente.

## Como executar

A forma mais simples é usar o script `run.sh`, que cria o banco `sunbank` (se ainda não existir) e inicia a aplicação:

```bash
./run.sh
```

Ou, para executar manualmente com Maven:

```bash
./mvnw spring-boot:run
```

A aplicação será iniciada e estará disponível por padrão em `http://localhost:8080`.

## Estrutura do projeto

```
src/main/java/com/backend/
├── BackendApplication.java      # Ponto de entrada da aplicação
├── controller/
│   ├── AuthController.java      # Endpoints de autenticação
│   ├── ClientController.java    # Endpoints REST para clientes
│   └── OperacaoController.java  # Endpoints REST para operações
├── entity/
│   ├── Auth.java                # Entidade de autenticação
│   ├── Client.java              # Entidade de cliente
│   └── Operacao.java            # Entidade JPA de operação bancária
├── repository/
│   ├── AuthRepository.java
│   ├── ClientRepository.java
│   └── OperacaoRepository.java
└── service/
    ├── AuthService.java
    ├── AuthServiceImpl.java
    ├── ClientService.java
    ├── ClientServiceImpl.java
    ├── OperacaoService.java
    └── OperacaoServiceImpl.java

src/test/java/com/backend/
├── BackendApplicationTests.java
├── service/
│   ├── AuthServiceImplTest.java
│   ├── ClientServiceImplTest.java
│   └── OperacaoServiceImplTest.java
```

## Endpoints da API

A aplicação expõe as seguintes APIs REST:

### Autenticação — Base URL: `/auth`

| Método | Endpoint         | Descrição                         |
|--------|------------------|-----------------------------------|
| POST   | `/auth/signin`   | Realiza login                     |
| POST   | `/auth/signout`  | Realiza logout                    |
| POST   | `/auth/forgot`   | Solicita recuperação de acesso    |
| POST   | `/auth/reset`    | Redefine credenciais              |

### Clientes — Base URL: `/clients`

| Método | Endpoint        | Descrição                          |
|--------|-----------------|------------------------------------|
| POST   | `/clients`      | Cria um novo cliente               |
| GET    | `/clients`      | Lista todos os clientes            |
| PUT    | `/clients/{id}` | Atualiza um cliente existente      |
| DELETE | `/clients/{id}` | Remove um cliente                  |

### Operações — Base URL: `/operacoes`

| Método | Endpoint          | Descrição                          |
|--------|-------------------|------------------------------------|
| POST   | `/operacoes`      | Cria uma nova operação             |
| GET    | `/operacoes`      | Lista todas as operações           |
| PUT    | `/operacoes/{id}` | Atualiza uma operação existente    |
| DELETE | `/operacoes/{id}` | Remove uma operação                |

### Exemplo de payload para autenticação

```json
{
  "username": "usuario",
  "password": "senha123",
  "authToken": 1234567890123456789
}
```

### Exemplo de payload para criação de cliente

```json
{
  "fullName": "João da Silva",
  "cpf": "12345678901",
  "rg": "1234567",
  "email": "joao@email.com",
  "phone": "11999999999",
  "birthDate": "1990-05-15",
  "maritalStatus": "SOLTEIRO",
  "nationality": "Brasileiro",
  "occupation": "Engenheiro",
  "monthlyIncome": 7500.00,
  "netWorth": 150000.00,
  "cryptocurrencyTokenId": "BTC-123456",
  "zipCode": "01001000",
  "street": "Rua A",
  "number": "100",
  "complement": "Apto 1",
  "neighborhood": "Centro",
  "city": "São Paulo",
  "state": "SP"
}
```

> O campo `cryptocurrencyTokenId` vincula o identificador do token de criptomoeda do cliente, utilizado no processo de reutilização e conversão para reais.

### Exemplo de payload para criação de operação

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
- `CONVERSAO` — converte/reutiliza criptomoeda e transfere o valor correspondente em reais (BRL)

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

O projeto conta com testes de carregamento de contexto e testes unitários/integração para os serviços em `src/test/java/com/backend/service/`.

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
export SPRING_DATASOURCE_PASSWORD=admin123
```

Ou passar diretamente na execução:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:postgresql://localhost:5432/sunbank --spring.datasource.username=postgres --spring.datasource.password=admin123"
```

## Solução de problemas

| Problema | Possível causa | Solução |
|----------|----------------|---------|
| Falha ao conectar no banco | PostgreSQL não está rodando ou banco `sunbank` não existe | Verifique se o PostgreSQL está ativo e crie o banco `sunbank` (ou use `./run.sh`) |
| Erro de autenticação | Usuário ou senha incorretos | Confira as credenciais em `application.properties` ou variáveis de ambiente |
| Porta 8080 em uso | Outro processo está usando a porta | Altere a porta com `--server.port=8081` ou finalize o processo anterior |
| Spring Boot não inicia | Versão do JDK inferior a 17 | Instale e configure o JDK 17 ou superior |

## Referências

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [PostgreSQL](https://www.postgresql.org/)
