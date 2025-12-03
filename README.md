# SkillMarabach AI

Sistema de matching de habilidades que conecta trabalhadores a vagas de emprego baseado em análise de competências.

## Integrantes

- Marcelo Siqueira Bonfim - RM558254
- Antonio Caue - RM558891
- Felipe Gomes Costa Orikasa - RM557435

## Sobre o Projeto

A SkillMatch AI é uma API REST desenvolvida em Java que faz o match entre trabalhadores e vagas de emprego através de análise de habilidades. O sistema também oferece recomendações de trilhas de aprendizado para desenvolvimento profissional.

## Tecnologias

- Java 17
- Spring Boot 3.1.0
- Azure SQL Database
- Azure Web App
- Azure DevOps (CI/CD)
- Maven
- JUnit

## Como Funciona

A aplicação está hospedada no Azure Web App e utiliza Azure SQL Database para armazenar os dados. O deploy é feito automaticamente através do Azure DevOps quando há merge na branch main.

## API

A API está disponível em: `https://skillmatch-app.azurewebsites.net/api`

### Autenticação

Usa Basic Authentication:
- Username: `admin`
- Password: `admin123`

### Principais Endpoints

**Trabalhadores:**
- GET `/api/v1/trabalhadores` - Lista todos os trabalhadores
- GET `/api/v1/trabalhadores/{id}` - Busca trabalhador por ID
- POST `/api/v1/trabalhadores` - Cria novo trabalhador
- PUT `/api/v1/trabalhadores/{id}` - Atualiza trabalhador
- DELETE `/api/v1/trabalhadores/{id}` - Remove trabalhador

**Vagas:**
- GET `/api/v1/vagas` - Lista todas as vagas
- GET `/api/v1/vagas/{id}` - Busca vaga por ID
- POST `/api/v1/vagas` - Cria nova vaga
- PUT `/api/v1/vagas/{id}` - Atualiza vaga
- DELETE `/api/v1/vagas/{id}` - Remove vaga

**Match:**
- GET `/api/v1/match/compatibilidade/{idTrabalhador}/{idVaga}` - Calcula compatibilidade
- GET `/api/v1/match/vagas-compativeis/{idTrabalhador}` - Lista vagas compatíveis

## Operações CRUD

### CRUD - Trabalhadores

#### CREATE - Criar Trabalhador

**Requisição:**
```http
POST https://skillmatch-app.azurewebsites.net/api/v1/trabalhadores
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Body:**
```json
{
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "cpf": "12345678900",
  "statusConta": "ATIVA",
  "habilidadesIds": [1, 2]
}
```

**Resposta (201 Created):**
```json
{
  "idTrabalhador": 1,
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "cpf": "12345678900",
  "dtCadastro": "2024-01-15",
  "statusConta": "ATIVA",
  "habilidadesIds": [1, 2]
}
```

#### READ - Listar Todos os Trabalhadores

**Requisição:**
```http
GET https://skillmatch-app.azurewebsites.net/api/v1/trabalhadores
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Resposta (200 OK):**
```json
[
  {
    "idTrabalhador": 1,
    "nome": "João Silva",
    "email": "joao.silva@email.com",
    "cpf": "12345678900",
    "dtCadastro": "2024-01-15",
    "statusConta": "ATIVA",
    "habilidadesIds": [1, 2]
  },
  {
    "idTrabalhador": 2,
    "nome": "Maria Santos",
    "email": "maria.santos@email.com",
    "cpf": "98765432100",
    "dtCadastro": "2024-01-16",
    "statusConta": "ATIVA",
    "habilidadesIds": [2, 3]
  }
]
```

#### READ - Buscar Trabalhador por ID

**Requisição:**
```http
GET https://skillmatch-app.azurewebsites.net/api/v1/trabalhadores/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Resposta (200 OK):**
```json
{
  "idTrabalhador": 1,
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "cpf": "12345678900",
  "dtCadastro": "2024-01-15",
  "statusConta": "ATIVA",
  "habilidadesIds": [1, 2]
}
```

#### UPDATE - Atualizar Trabalhador

**Requisição:**
```http
PUT https://skillmatch-app.azurewebsites.net/api/v1/trabalhadores/1
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Body:**
```json
{
  "nome": "João Silva Atualizado",
  "email": "joao.atualizado@email.com",
  "cpf": "12345678900",
  "statusConta": "ATIVA",
  "habilidadesIds": [1, 2, 3]
}
```

**Resposta (200 OK):**
```json
{
  "idTrabalhador": 1,
  "nome": "João Silva Atualizado",
  "email": "joao.atualizado@email.com",
  "cpf": "12345678900",
  "dtCadastro": "2024-01-15",
  "statusConta": "ATIVA",
  "habilidadesIds": [1, 2, 3]
}
```

#### DELETE - Deletar Trabalhador

**Requisição:**
```http
DELETE https://skillmatch-app.azurewebsites.net/api/v1/trabalhadores/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Resposta (204 No Content):**
```
(sem corpo de resposta)
```

---

### CRUD - Vagas

#### CREATE - Criar Vaga

**Requisição:**
```http
POST https://skillmatch-app.azurewebsites.net/api/v1/vagas
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Body:**
```json
{
  "idEmpresa": 1,
  "titulo": "Desenvolvedor Java",
  "descricao": "Vaga para desenvolvedor Java com experiência em Spring Boot e Azure",
  "habilidadesExigidas": [1, 2, 3]
}
```

**Resposta (201 Created):**
```json
{
  "idVaga": 1,
  "idEmpresa": 1,
  "titulo": "Desenvolvedor Java",
  "descricao": "Vaga para desenvolvedor Java com experiência em Spring Boot e Azure",
  "dtPublicacao": "2024-01-15",
  "habilidadesExigidas": [1, 2, 3]
}
```

#### READ - Listar Todas as Vagas

**Requisição:**
```http
GET https://skillmatch-app.azurewebsites.net/api/v1/vagas
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Resposta (200 OK):**
```json
[
  {
    "idVaga": 1,
    "idEmpresa": 1,
    "titulo": "Desenvolvedor Java",
    "descricao": "Vaga para desenvolvedor Java com experiência em Spring Boot e Azure",
    "dtPublicacao": "2024-01-15",
    "habilidadesExigidas": [1, 2, 3]
  },
  {
    "idVaga": 2,
    "idEmpresa": 1,
    "titulo": "DevOps Engineer",
    "descricao": "Vaga para DevOps com experiência em Azure e CI/CD",
    "dtPublicacao": "2024-01-16",
    "habilidadesExigidas": [4, 5]
  }
]
```

#### READ - Buscar Vaga por ID

**Requisição:**
```http
GET https://skillmatch-app.azurewebsites.net/api/v1/vagas/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Resposta (200 OK):**
```json
{
  "idVaga": 1,
  "idEmpresa": 1,
  "titulo": "Desenvolvedor Java",
  "descricao": "Vaga para desenvolvedor Java com experiência em Spring Boot e Azure",
  "dtPublicacao": "2024-01-15",
  "habilidadesExigidas": [1, 2, 3]
}
```

#### UPDATE - Atualizar Vaga

**Requisição:**
```http
PUT https://skillmatch-app.azurewebsites.net/api/v1/vagas/1
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Body:**
```json
{
  "idEmpresa": 1,
  "titulo": "Desenvolvedor Java Sênior",
  "descricao": "Vaga atualizada para desenvolvedor Java Sênior com experiência em Spring Boot, Azure e microserviços",
  "habilidadesExigidas": [1, 2, 3, 4]
}
```

**Resposta (200 OK):**
```json
{
  "idVaga": 1,
  "idEmpresa": 1,
  "titulo": "Desenvolvedor Java Sênior",
  "descricao": "Vaga atualizada para desenvolvedor Java Sênior com experiência em Spring Boot, Azure e microserviços",
  "dtPublicacao": "2024-01-15",
  "habilidadesExigidas": [1, 2, 3, 4]
}
```

#### DELETE - Deletar Vaga

**Requisição:**
```http
DELETE https://skillmatch-app.azurewebsites.net/api/v1/vagas/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

**Resposta (204 No Content):**
```
(sem corpo de resposta)
```

## Banco de Dados

O banco de dados é Azure SQL Database. As tabelas principais são:
- TB_SKILL_USUARIO - Dados dos trabalhadores
- TB_SKILL_VAGA - Dados das vagas
- TB_SKILL_HABILIDADE - Catálogo de habilidades
- TB_SKILL_USUARIO_HABILIDADE - Relação trabalhador-habilidade
- TB_SKILL_VAGA_HABILIDADE - Relação vaga-habilidade

## CI/CD

O pipeline do Azure DevOps executa automaticamente:
1. Build do projeto com Maven
2. Execução de testes
3. Criação de infraestrutura no Azure (se necessário)
4. Deploy da aplicação no Azure Web App

## Executar Localmente

Pré-requisitos:
- Java 17
- Maven 3.9+
- Azure SQL Database configurado

```bash
mvn clean install
mvn spring-boot:run
```

## Testes

```bash
mvn test
```

Para ver cobertura de código:

```bash
mvn clean test jacoco:report
```

## Health Check

```http
GET /api/actuator/health
```
