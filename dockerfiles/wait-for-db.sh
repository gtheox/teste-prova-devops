#!/bin/sh
# Script para aguardar bancos de dados ficarem prontos
# Substitui o sleep fixo por health checks reais

ORACLE_HOST=${ORACLE_HOST:-localhost}
ORACLE_PORT=${ORACLE_PORT:-1521}
MONGO_HOST=${MONGO_HOST:-localhost}
MONGO_PORT=${MONGO_PORT:-27017}
MAX_WAIT=${MAX_WAIT:-300}  # 5 minutos máximo
RETRY_INTERVAL=${RETRY_INTERVAL:-5}  # 5 segundos entre tentativas

echo "Aguardando bancos de dados ficarem prontos..."
echo "Oracle: $ORACLE_HOST:$ORACLE_PORT"
echo "MongoDB: $MONGO_HOST:$MONGO_PORT"

elapsed=0
oracle_ready=false
mongo_ready=false

while [ $elapsed -lt $MAX_WAIT ]; do
  # Testa Oracle
  if [ "$oracle_ready" = "false" ]; then
    if nc -z -w 2 "$ORACLE_HOST" "$ORACLE_PORT" 2>/dev/null; then
      echo "Oracle esta pronto! (${elapsed}s)"
      oracle_ready=true
    fi
  fi
  
  # Testa MongoDB
  if [ "$mongo_ready" = "false" ]; then
    if nc -z -w 2 "$MONGO_HOST" "$MONGO_PORT" 2>/dev/null; then
      echo "MongoDB esta pronto! (${elapsed}s)"
      mongo_ready=true
    fi
  fi
  
  # Se ambos estiverem prontos, sai
  if [ "$oracle_ready" = "true" ] && [ "$mongo_ready" = "true" ]; then
    echo "Todos os bancos de dados estao prontos! (${elapsed}s)"
    exit 0
  fi
  
  sleep $RETRY_INTERVAL
  elapsed=$((elapsed + RETRY_INTERVAL))
done

# Se chegou aqui, timeout
if [ "$oracle_ready" = "false" ]; then
  echo "AVISO: Oracle nao ficou pronto apos ${MAX_WAIT}s, mas continuando..."
fi
if [ "$mongo_ready" = "false" ]; then
  echo "AVISO: MongoDB nao ficou pronto apos ${MAX_WAIT}s, mas continuando..."
fi

echo "Iniciando aplicacao..."

