# ===============================================
# BUILD STAGE
# ===============================================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia apenas o pom.xml para cache de dependências
COPY pom.xml .

# Baixa dependências (cache eficiente)
RUN mvn dependency:go-offline -q

# Copia código-fonte
COPY src ./src

# Build sem testes (testes já rodam no pipeline CI/CD)
RUN mvn clean package -DskipTests -q \
    -Dmaven.compiler.source=17 \
    -Dmaven.compiler.target=17

# ===============================================
# RUNTIME STAGE (IMAGEM LEVE)
# ===============================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o JAR gerado
COPY --from=builder /app/target/skillmatch-ai-1.0.0.jar app.jar

# Instala netcat para health checks e cria diretório de logs
RUN apk add --no-cache netcat-openbsd && \
    mkdir -p /app/logs && \
    addgroup -g 1001 appuser && \
    adduser -D -u 1001 -G appuser appuser && \
    chown -R appuser:appuser /app

# Copia script de health check
COPY dockerfiles/wait-for-db.sh /app/wait-for-db.sh
RUN chmod +x /app/wait-for-db.sh && \
    chown appuser:appuser /app/wait-for-db.sh

USER appuser

EXPOSE 8080

# Variáveis de ambiente padrão (produção)
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC" \
    LOGGING_FILE_NAME="" \
    SERVER_PORT=8080

# Health check inteligente: aguarda bancos ficarem prontos antes de iniciar
# Substitui sleep fixo por verificações reais de conectividade
ENTRYPOINT ["sh", "-c", "/app/wait-for-db.sh && java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

