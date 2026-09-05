# SunBank - Backend

Backend REST para aplicacao bancaria simples, responsavel por autenticacao de usuarios e operacoes financeiras (deposito, saque, transferencia, saldo e extrato).

## Tecnologias

- Java 17
- Spring Boot 4.0.8
- Spring Data JPA
- Spring Web MVC
- PostgreSQL
- Maven

## Requisitos

- JDK 17+
- Maven 3.9+ (ou use `./mvnw`)
- PostgreSQL 14+
- Banco de dados `sunbank` criado

## Configuracao

As configuracoes estao em `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/sunbank}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=update
```

Voce pode sobrescrever via variaveis de ambiente:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/sunbank
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

## Executando

Compile e execute:

```bash
./mvnw spring-boot:run
```

Ou compile o jar:

```bash
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

A aplicacao inicia na porta `8080`.

## Estrutura do Projeto

```
com.backend
├── controller      # Endpoints REST
├── service         # Regras de negocio
├── repository      # Acesso a dados (Spring Data JPA)
├── entity          # Entidades JPA
├── dto             # Objetos de transferencia de dados
```

## Endpoints

Todas as rotas de operacoes exigem o cabecalho `Authorization` com o token retornado no login ou registro.

### Autenticacao

#### POST /api/auth/register

Cadastra um novo usuario.

**Request:**

```json
{
  "username": "joao",
  "email": "joao@email.com",
  "password": "123456"
}
```

**Response:**

```json
{
  "id": 1,
  "username": "joao",
  "email": "joao@email.com",
  "token": "<token>"
}
```

#### POST /api/auth/login

Autentica um usuario existente.

**Request:**

```json
{
  "username": "joao",
  "password": "123456"
}
```

**Response:**

```json
{
  "id": 1,
  "username": "joao",
  "email": "joao@email.com",
  "token": "<token>"
}
```

#### GET /api/auth/me

Retorna os dados do usuario autenticado.

**Header:** `Authorization: <token>`

**Response:**

```json
{
  "id": 1,
  "username": "joao",
  "email": "joao@email.com",
  "token": "<token>"
}
```

#### POST /api/auth/logout

Invalida o token do usuario.

**Header:** `Authorization: <token>`

### Operacoes

#### POST /api/operacoes/deposito

Realiza um deposito na conta do usuario autenticado.

**Header:** `Authorization: <token>`

**Request:**

```json
{
  "valor": 100.00,
  "descricao": "Deposito inicial"
}
```

#### POST /api/operacoes/saque

Realiza um saque, desde que haja saldo suficiente.

**Header:** `Authorization: <token>`

**Request:**

```json
{
  "valor": 50.00,
  "descricao": "Saque caixa"
}
```

#### POST /api/operacoes/transferencia

Transfere valor para outro usuario.

**Header:** `Authorization: <token>`

**Request:**

```json
{
  "valor": 30.00,
  "descricao": "Pagamento",
  "destinoUsername": "maria"
}
```

#### GET /api/operacoes/saldo

Retorna o saldo atual.

**Header:** `Authorization: <token>`

**Response:**

```json
{
  "saldo": 120.00
}
```

#### GET /api/operacoes/extrato

Retorna o extrato de operacoes do usuario.

**Header:** `Authorization: <token>`

**Response:**

```json
[
  {
    "id": 1,
    "tipo": "DEPOSITO",
    "valor": 100.00,
    "descricao": "Deposito inicial",
    "origem": "joao",
    "destino": "joao",
    "createdAt": "2026-09-05T10:00:00"
  }
]
```

## Modelo de Dados

### auth_users

Armazena os usuarios cadastrados.

| Campo        | Tipo          | Observacao                |
|--------------|---------------|---------------------------|
| id           | BIGINT (PK)   | Auto incremento           |
| username     | VARCHAR       | Unico, obrigatorio        |
| email        | VARCHAR       | Unico, obrigatorio        |
| passwordHash | VARCHAR       | Hash PBKDF2 com salt      |
| salt         | VARCHAR       | Salt gerado aleatoriamente|
| token        | VARCHAR       | Token de sessao           |
| createdAt    | TIMESTAMP     | Data de criacao           |

### operacoes

Armazena as movimentacoes financeiras.

| Campo       | Tipo              | Observacao                   |
|-------------|-------------------|------------------------------|
| id          | BIGINT (PK)       | Auto incremento              |
| tipo        | VARCHAR           | DEPOSITO, SAQUE, TRANSFERENCIA |
| valor       | DECIMAL(15,2)     | Valor da operacao            |
| descricao   | VARCHAR           | Descricao opcional           |
| origem_id   | BIGINT (FK)       | Usuario de origem            |
| destino_id  | BIGINT (FK)       | Usuario de destino           |
| createdAt   | TIMESTAMP         | Data da operacao             |

## Seguranca

- As senhas sao armazenadas com hash PBKDF2WithHmacSHA256, 10000 iteracoes e salt aleatorio.
- A autenticacao e baseada em token gerado aleatoriamente e enviado no cabecalho `Authorization`.
- Nao e recomendado usar `ddl-auto=update` em producao. Prefira migrations com Flyway ou Liquibase.
