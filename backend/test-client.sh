#!/usr/bin/env bash
set -e

TIMESTAMP=$(date +%s)
CPF="2${TIMESTAMP}"
EMAIL="cliente-teste-${TIMESTAMP}@sunbraz.com"

BASE_URL="${BASE_URL:-http://localhost:9090}"

echo "==> Teste de criação de cliente com SunBraz"

CLIENT_RESPONSE=$(curl -f -s -X POST "$BASE_URL/clients" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Cliente Teste SunBraz",
    "cpf": "'"$CPF"'",
    "email": "'"$EMAIL"'",
    "phone": "11999998888",
    "criptoEscolhida": "sunbraz"
  }')

echo "Resposta do cliente:"
echo "$CLIENT_RESPONSE" | head -c 1000
echo ""

if ! echo "$CLIENT_RESPONSE" | grep -q 'cryptocurrencyTokenId'; then
  echo "ERRO: token da cripto não foi fixado no cliente"
  exit 1
fi

if ! echo "$CLIENT_RESPONSE" | grep -q 'SBZ'; then
  echo "ERRO: token do cliente não é SBZ"
  exit 1
fi

echo "OK: token SBZ fixado ao cliente"
echo ""

echo "==> Teste de listagem de criptos"

CRIPTOS_RESPONSE=$(curl -f -s "$BASE_URL/criptos")

echo "Resposta de criptos:"
echo "$CRIPTOS_RESPONSE" | head -c 1000
echo ""

if ! echo "$CRIPTOS_RESPONSE" | grep -q 'SunBraz'; then
  echo "ERRO: cripto SunBraz não encontrada"
  exit 1
fi

if ! echo "$CRIPTOS_RESPONSE" | grep -q 'SBZ'; then
  echo "ERRO: símbolo SBZ não encontrado"
  exit 1
fi

echo "OK: cripto SunBraz (SBZ) criada e vinculada ao cliente"
