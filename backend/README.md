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

### Carteira Ethereum

A carteira conecta na testnet **Sepolia** por padrao:

```properties
ethereum.rpc-url=${ETH_RPC_URL:https://ethereum-sepolia-rpc.publicnode.com}
ethereum.chain-id=${ETH_CHAIN_ID:11155111}
wallet.encryption-secret=${WALLET_SECRET:altere-esta-chave-em-producao}
```

- `ETH_RPC_URL`: endpoint RPC Ethereum (Infura, Alchemy ou RPC publico).
- `ETH_CHAIN_ID`: `11155111` para Sepolia. Para mainnet use `1` e um RPC de mainnet.
- `WALLET_SECRET`: segredo usado para criptografar as chaves privadas no banco (AES-GCM). **Defina um valor forte em producao.**

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

### Carteira Ethereum

Carteira custodial estilo MetaMask: o backend gera o par de chaves (secp256k1), guarda a chave privada criptografada no banco e assina transacoes on-chain na rede configurada (Sepolia por padrao).

#### POST /api/carteira

Cria a carteira do usuario autenticado (uma por usuario).

**Header:** `Authorization: <token>`

**Response (201):**

```json
{
  "id": 1,
  "endereco": "0x3f8a2c1b9d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a",
  "rede": "sepolia",
  "createdAt": "2026-09-05T10:00:00"
}
```

#### GET /api/carteira

Retorna a carteira do usuario autenticado.

**Header:** `Authorization: <token>`

**Response:** mesmo formato do `POST /api/carteira`.

#### GET /api/carteira/saldo

Consulta o saldo on-chain do endereco.

**Header:** `Authorization: <token>`

**Response:**

```json
{
  "endereco": "0x3f8a2c1b9d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a",
  "saldoWei": "50000000000000000",
  "saldoEth": 0.05
}
```

#### POST /api/carteira/enviar

Assina e envia uma transacao de ETH para outro endereco.

**Header:** `Authorization: <token>`

**Request:**

```json
{
  "destino": "0x8ba1f109551bD432803012645Ac136ddd64DBA72",
  "valorEth": 0.001
}
```

**Response:**

```json
{
  "txHash": "0x9fc76417374aa880d4449a1f7f31ec597f00b1f6f3dd2d66f4c9c6c445836d8b",
  "origem": "0x3f8a2c1b9d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a",
  "destino": "0x8ba1f109551bD432803012645Ac136ddd64DBA72",
  "valorEth": 0.001,
  "status": "CONFIRMADA"
}
```

> Para testar na Sepolia, obtenha ETH de teste em um faucet (por exemplo, o faucet da Sepolia) enviando para o endereco retornado por `POST /api/carteira`.

## Testando a API (JSON)

Com a aplicacao rodando na porta `8080`, voce pode testar os JSONs com `curl`.

### 1. Registrar e obter o token

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "joao", "email": "joao@email.com", "password": "123456"}'
```

Guarde o valor do campo `token` da resposta. Para facilitar:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "joao", "password": "123456"}' | jq -r .token)
```

### 2. Depositar

```bash
curl -X POST http://localhost:8080/api/operacoes/deposito \
  -H "Content-Type: application/json" \
  -H "Authorization: $TOKEN" \
  -d '{"valor": 1000.00, "descricao": "Deposito inicial"}'
```

### 3. Consultar saldo

```bash
curl http://localhost:8080/api/operacoes/saldo \
  -H "Authorization: $TOKEN"
```

### 4. Transferir para outro usuario

```bash
curl -X POST http://localhost:8080/api/operacoes/transferencia \
  -H "Content-Type: application/json" \
  -H "Authorization: $TOKEN" \
  -d '{"valor": 30.00, "descricao": "Pagamento", "destinoUsername": "maria"}'
```

### 5. Consultar extrato

```bash
curl http://localhost:8080/api/operacoes/extrato \
  -H "Authorization: $TOKEN"
```

### 6. Criar carteira Ethereum

```bash
curl -X POST http://localhost:8080/api/carteira \
  -H "Authorization: $TOKEN"
```

### 7. Consultar saldo on-chain

```bash
curl http://localhost:8080/api/carteira/saldo \
  -H "Authorization: $TOKEN"
```

### 8. Enviar ETH

```bash
curl -X POST http://localhost:8080/api/carteira/enviar \
  -H "Content-Type: application/json" \
  -H "Authorization: $TOKEN" \
  -d '{"destino": "0x8ba1f109551bD432803012645Ac136ddd64DBA72", "valorEth": 0.001}'
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

### carteiras

Armazena as carteiras Ethereum (uma por usuario).

| Campo                       | Tipo          | Observacao                          |
|-----------------------------|---------------|-------------------------------------|
| id                          | BIGINT (PK)   | Auto incremento                     |
| usuario_id                  | BIGINT (FK)   | Unico, dono da carteira             |
| endereco                    | VARCHAR(42)   | Endereco publico `0x...`, unico     |
| chavePrivadaCriptografada   | VARCHAR       | Chave privada cifrada com AES-GCM   |
| createdAt                   | TIMESTAMP     | Data de criacao                     |

## Seguranca

- As senhas sao armazenadas com hash PBKDF2WithHmacSHA256, 10000 iteracoes e salt aleatorio.
- A autenticacao e baseada em token gerado aleatoriamente e enviado no cabecalho `Authorization`.
- As chaves privadas das carteiras sao cifradas com AES-GCM antes de serem salvas no banco, usando o segredo definido em `WALLET_SECRET`. Quem tiver acesso a esse segredo e ao banco consegue mover os fundos — trate-o como um segredo critico.
- Nao e recomendado usar `ddl-auto=update` em producao. Prefira migrations com Flyway ou Liquibase.
