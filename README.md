# java-api-boilerplate

**Version:** 0.0.1-SNAPSHOT  
**Group ID:** br.com.wendelnogueira

## Overview

This is a corporate boilerplate for Java APIs using Spring Boot 3.5+, designed to be reusable in any enterprise REST API project. It includes hexagonal architecture, DDD, JWT security, comprehensive testing, observability, resilience, and infrastructure for production deployment.

## Architecture

### Adopted Patterns
- **Hexagonal Architecture + Layers**: Clear separation between domain, application, and infrastructure.
- **DDD (Domain-Driven Design)**: Focus on business domain.
- **Clean Architecture**: Dependencies point inward.
- **MVC**: Controllers, Services, Repositories.

### Directory Structure
```
java-api-boilerplate/
├── src/main/java/br/com/wendelnogueira/javaapiboilerplate/
│   ├── api/                 # Generated OpenAPI interfaces
│   ├── controller/          # REST Controllers
│   ├── service/             # Business logic
│   ├── repository/          # JPA data access
│   ├── model/               # JPA entities
│   ├── dto/                 # Data Transfer Objects
│   ├── mapper/              # MapStruct mappings
│   ├── exception/           # Custom exceptions
│   ├── security/            # JWT security configurations
│   ├── config/              # General configurations
│   └── util/                # Utilities
├── src/test/java/           # Tests
│   ├── unit/                # Unit tests
│   ├── integration/         # Integration tests with Testcontainers
│   └── bdd/                 # BDD tests with Cucumber
├── performance/             # JMeter scripts
├── helm/                    # Helm Charts per environment
│   ├── dev/
│   ├── hml/
│   └── prod/
├── docker-compose.yml       # Local environment with Docker
├── Dockerfile               # Containerization
├── Jenkinsfile              # CI/CD
└── README.md                # This documentation
```

## Technologies

- **Java 21**
- **Spring Boot 3.5+**
- **Spring Security** (JWT)
- **Spring Data JPA** (MySQL)
- **OpenAPI Generator** (Swagger)
- **MapStruct** (Mappings)
- **Lombok** (Reduce boilerplate)
- **Resilience4j** (Circuit Breaker, Retry, etc.)
- **OTEL + Datadog** (Observability)
- **Testcontainers** (Integration tests)
- **Cucumber** (BDD)
- **JMeter** (Performance)
- **Docker + Docker Compose**
- **Helm** (Kubernetes)
- **Jenkins** (CI/CD)

## Prerequisites

- Java 21
- Maven 3.9+
- Docker + Docker Compose
- MySQL (local or via Docker)
- Kubernetes + Helm (for deployment)

## How to Run Locally

### 1. Clone the Repository
```bash
git clone https://github.com/WendelFNogueira/java-api-boilerplate.git
cd java-api-boilerplate
```

### 2. Configure Database
Option 1: Local MySQL
- Install MySQL and create database `javaapiboilerplate`.

Option 2: Via Docker Compose
```bash
docker-compose up -d mysql
```

### 3. Configure Environment Variables
Create file `application-local.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/javaapiboilerplate
spring.datasource.username=user
spring.datasource.password=password
jwt.secret=mySecretKey
jwt.expiration=86400000
```

### 4. Run the Application
```bash
# Default (H2 database)
mvn spring-boot:run

# With local profile (MySQL via Docker)
# Linux/Mac
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run

# Windows (PowerShell)
$env:SPRING_PROFILES_ACTIVE = 'local'; .\mvnw.cmd spring-boot:run
```

The API will be available at `http://localhost:8080`.

### 5. Access Documentation
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

## How to Run Tests

### Unit Tests
```bash
mvn test -Dtest="*Test"
```

### Integration Tests (with Testcontainers)
```bash
mvn test -Dtest="*ControllerTest"
```

### BDD Tests (Cucumber)
```bash
mvn test -Dtest="*CucumberTest"
```

### All Tests
```bash
mvn test
```

## How to Run JMeter (Performance)

### 1. Install JMeter
Download and install Apache JMeter.

### 2. Run Scripts
```bash
jmeter -n -t performance/users-performance.jmx -l results.jtl
```

### 3. View Reports
Open `results.jtl` in JMeter or generate HTML report.

## Deployment

### Local Development
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
The `Jenkinsfile` automates build, tests, Docker, and deployment based on branch.

## Security

- **JWT Authentication**: Bearer tokens for protected endpoints.
- **Roles**: ADMIN and USER.
- **Free Endpoints**: `/actuator/health`, `/auth/login`, `/auth/register`, `/swagger-ui/**`, `/swagger-ui.html`,`/v3/api-docs/**`.

## Observability

- **OTEL**: Distributed tracing.
- **Datadog**: Metrics and logs.
- **Actuator**: Health checks.

## Standards and Conventions

### Branches
- `main`: Production
- `develop`: Development
- `feature/*`: New features
- `release/*`: Releases
- `hotfix/*`: Urgent fixes

### Commits
- `feat:` New features
- `fix:` Fixes
- `docs:` Documentation
- `refactor:` Refactoring
- `test:` Tests

### Code
- Java 21 features
- Lombok to reduce boilerplate
- MapStruct for mappings
- Custom exceptions with MessageExceptionFormatter
- Validations within functions

## Contribution

1. Fork the project
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'feat: new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

## License

This project is corporate property. Consult internal terms.
