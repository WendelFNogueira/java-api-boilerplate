# Java API Boilerplate

## Visão Geral

Este é um boilerplate corporativo para APIs Java utilizando Spring Boot 3.5+, projetado para ser reutilizável em qualquer projeto de API REST empresarial. Inclui arquitetura hexagonal, DDD, segurança JWT, testes abrangentes, observabilidade, resiliência e infraestrutura para deploy em produção.

## Arquitetura

### Padrões Adotados
- **Arquitetura Hexagonal + Camadas**: Separação clara entre domínio, aplicação e infraestrutura.
- **DDD (Domain-Driven Design)**: Foco no domínio de negócio.
- **Clean Architecture**: Dependências apontam para dentro.
- **MVC**: Controllers, Services, Repositories.

### Estrutura de Diretórios
```
java-api-boilerplate/
├── src/main/java/br/com/wendelnogueira/javaapiboilerplate/
│   ├── api/                 # Interfaces OpenAPI geradas
│   ├── controller/          # Controllers REST
│   ├── service/             # Lógica de negócio
│   ├── repository/          # Acesso a dados JPA
│   ├── model/               # Entidades JPA
│   ├── dto/                 # Data Transfer Objects
│   ├── mapper/              # Mapeamentos MapStruct
│   ├── exception/           # Exceções customizadas
│   ├── security/            # Configurações de segurança JWT
│   ├── config/              # Configurações gerais
│   └── util/                # Utilitários
├── src/test/java/           # Testes
│   ├── unit/                # Testes unitários
│   ├── integration/         # Testes de integração com Testcontainers
│   └── bdd/                 # Testes BDD com Cucumber
├── performance/             # Scripts JMeter
├── helm/                    # Helm Charts por ambiente
│   ├── dev/
│   ├── hml/
│   └── prod/
├── docker-compose.yml       # Ambiente local com Docker
├── Dockerfile               # Containerização
├── Jenkinsfile              # CI/CD
└── README.md                # Esta documentação
```

## Tecnologias

- **Java 21**
- **Spring Boot 3.5+**
- **Spring Security** (JWT)
- **Spring Data JPA** (MySQL)
- **OpenAPI Generator** (Swagger)
- **MapStruct** (Mapeamentos)
- **Lombok** (Redução de boilerplate)
- **Resilience4j** (Circuit Breaker, Retry, etc.)
- **OTEL + Datadog** (Observabilidade)
- **Testcontainers** (Testes de integração)
- **Cucumber** (BDD)
- **JMeter** (Performance)
- **Docker + Docker Compose**
- **Helm** (Kubernetes)
- **Jenkins** (CI/CD)

## Pré-requisitos

- Java 21
- Maven 3.9+
- Docker + Docker Compose
- MySQL (local ou via Docker)
- Kubernetes + Helm (para deploy)

## Como Rodar Local

### 1. Clonar o Repositório
```bash
git clone https://github.com/WendelFNogueira/java-api-boilerplate.git
cd java-api-boilerplate
```

### 2. Configurar Banco de Dados
Opção 1: MySQL local
- Instalar MySQL e criar banco `javaapiboilerplate`.

Opção 2: Via Docker Compose
```bash
docker-compose up -d mysql
```

### 3. Configurar Variáveis de Ambiente
Criar arquivo `application-local.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/javaapiboilerplate
spring.datasource.username=user
spring.datasource.password=password
jwt.secret=mySecretKey
jwt.expiration=86400000
```

### 4. Executar a Aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

### 5. Acessar Documentação
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

## Como Rodar Testes

### Testes Unitários
```bash
mvn test -Dtest="*Test"
```

### Testes de Integração (com Testcontainers)
```bash
mvn test -Dtest="*ControllerTest"
```

### Testes BDD (Cucumber)
```bash
mvn test -Dtest="*CucumberTest"
```

### Todos os Testes
```bash
mvn test
```

## Como Rodar JMeter (Performance)

### 1. Instalar JMeter
Baixar e instalar Apache JMeter.

### 2. Executar Scripts
```bash
jmeter -n -t performance/users-performance.jmx -l results.jtl
```

### 3. Ver Relatórios
Abrir `results.jtl` no JMeter ou gerar relatório HTML.

## Deploy

### Desenvolvimento Local
```bash
docker-compose up
```

### Kubernetes via Helm
```bash
# Dev
helm install java-api-boilerplate ./helm/dev --namespace dev

# HML
helm install java-api-boilerplate ./helm/hml --namespace hml

# Prod
helm install java-api-boilerplate ./helm/prod --namespace prod
```

### CI/CD via Jenkins
O `Jenkinsfile` automatiza build, testes, Docker e deploy baseado na branch.

## Segurança

- **JWT Authentication**: Tokens Bearer para endpoints protegidos.
- **Roles**: ADMIN e USER.
- **Endpoints Livres**: `/actuator/health`, `/auth/login`, `/auth/register`.

## Observabilidade

- **OTEL**: Tracing distribuído.
- **Datadog**: Métricas e logs.
- **Actuator**: Health checks.

## Padrões e Convenções

### Branches
- `main`: Produção
- `develop`: Desenvolvimento
- `feature/*`: Novas funcionalidades
- `release/*`: Releases
- `hotfix/*`: Correções urgentes

### Commits
- `feat:` Novas funcionalidades
- `fix:` Correções
- `docs:` Documentação
- `refactor:` Refatoração
- `test:` Testes

### Código
- Java 21 features
- Lombok para reduzir boilerplate
- MapStruct para mapeamentos
- Exceções customizadas com MessageExceptionFormatter
- Validações dentro das funções

## Contribuição

1. Fork o projeto
2. Crie uma branch feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'feat: nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto é propriedade corporativa. Consulte os termos internos.
