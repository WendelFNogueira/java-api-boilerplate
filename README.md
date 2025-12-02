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
├── docker-compose.yml                  # Local environment with Docker (API + MySQL)
├── Dockerfile                          # Containerization
├── Jenkinsfile                         # CI/CD pipeline
├── mvnw                                # Maven wrapper
├── mvnw.cmd                            # Maven wrapper for Windows
├── pom.xml                             # Maven configuration
├── README.md                           # This documentation
├── helm/                               # Helm Charts per environment
│   ├── dev/
│   ├── hml/
│   └── prod/
├── logs/                               # Application logs
├── performance/                        # JMeter scripts (users-performance.jmx)
├── src/
│   ├── main/
│   │   ├── java/br/com/wendelnogueira/javaapiboilerplate/
│   │   │   ├── api/                 # Generated OpenAPI interfaces
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business logic (AuthService, UsersService)
│   │   │   ├── repository/          # JPA data access
│   │   │   ├── model/               # JPA entities
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── mapper/              # MapStruct mappings
│   │   │   ├── exception/           # Custom exceptions and GlobalExceptionHandler
│   │   │   ├── security/            # JWT security configurations
│   │   │   ├── config/              # General configurations
│   │   │   └── util/                # Utilities (JwtUtil, MessageExceptionFormatter)
│   │   └── resources/
│   │       ├── application.properties          # Common configs
│   │       ├── application-local.properties    # Local overrides (MySQL, disable Datadog)
│   │       ├── application-deploy.properties   # Deploy overrides (env vars, enable Datadog)
│   │       ├── java-api-boilerplate.yaml       # OpenAPI 3.0 spec
│   │       ├── messages_en.properties          # Error messages
│   │       └── docs/README.md                  # This documentation
│   └── test/
│       ├── java/br/com/wendelnogueira/javaapiboilerplate/
│       │   ├── bdd/                        # BDD tests with Cucumber (UserManagementSteps, CucumberTest)
│       │   ├── integration/                # Integration tests with Testcontainers
│       │   └── unit/                       # Unit tests (AuthControllerTest, UsersServiceTest)
│       └── resources/
│           ├── application.properties      # Test configs
│           └── features/                   # Cucumber feature files
├── target/                             # Build output
└── ...
```

## Technologies

- **Java 21**
- **Spring Boot 3.5+**
- **Spring Security** (JWT)
- **Spring Data JPA** (MySQL)
- **OpenAPI Generator** (Swagger)
- **MapStruct** (Mappings)
- **Lombok** (Reduce boilerplate)
- **Resilience4j** (Retry)
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
- Apache JMeter (for performance tests)

## How to Run Locally

### 1. Clone the Repository
```bash
git clone https://github.com/WendelFNogueira/java-api-boilerplate.git
cd java-api-boilerplate
```

### 2. Configure Database
The application uses MySQL. You can run it locally or via Docker.

Option 1: Local MySQL
- Install MySQL and create database `javaapiboilerplate`.

Option 2: Via Docker Compose (recommended)
```bash
docker-compose up -d mysql
```
This starts a MySQL container on port 3306.

### 3. Configure Environment Variables
The `application-local.properties` is already configured for local development with MySQL container settings. If using local MySQL, adjust the URL.

### 4. Run the Application
```bash
# Install dependencies and compile
mvn clean install

# Run with local profile (uses application-local.properties)
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Or via Docker Compose (full containerized):
```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

### Default Users
The application automatically creates the following default users on startup:

- **Admin User**:
  - Email: `admin@example.com`
  - Password: `admin123`
  - Role: ADMIN

- **Regular User**:
  - Email: `user@example.com`
  - Password: `user123`
  - Role: USER

You can use these credentials to log in via `/auth/login` and obtain a JWT token for testing authenticated endpoints.

### 5. Access Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

To test authenticated endpoints in Swagger UI:
1. Register a user or use default credentials.
2. Login via `/auth/login` to get a JWT token.
3. Click the "Authorize" button in Swagger UI.
4. Enter `Bearer <your-token>` in the value field.
5. Click "Authorize" to set the token for requests.

### Updating OpenAPI Specification
If you modify the `java-api-boilerplate.yaml` file, regenerate the OpenAPI code:

```bash
mvn clean generate-resources
mvn clean compile
```

## How to Run Tests

### Unit Tests
```bash
mvn test -Dtest="*Test" -Dspring.profiles.active=test
```

### Integration Tests (with Testcontainers)
```bash
mvn test -Dtest="*ControllerTest" -Dspring.profiles.active=test
```

### BDD Tests (Cucumber)
Note: Cucumber tests are currently not fully configured due to Testcontainers integration issues. The test runner exists, but may fail in some environments.
```bash
mvn test -Dtest="*CucumberTest" -Dspring.profiles.active=test
```

### All Tests
```bash
mvn test -Dspring.profiles.active=test
```

## How to Run JMeter (Performance)

### 1. Install JMeter
Download and install Apache JMeter from https://jmeter.apache.org/.

### 2. Update Script for Authentication
The `users-performance.jmx` script includes a login sampler, but to properly test authenticated endpoints, you need to:
- Add a JSON Extractor to extract the JWT token from the login response.
- Add an HTTP Header Manager with `Authorization: Bearer ${token}` for subsequent requests.

Open the script in JMeter GUI and configure accordingly.

### 3. Run Scripts
```bash
jmeter -n -t performance/users-performance.jmx -l results.jtl
```

### 4. View Reports
Open `results.jtl` in JMeter or generate HTML report:
```bash
jmeter -g results.jtl -o report/
```

## Resilience

The application uses Resilience4j for fault tolerance:
- **Retry**: Configured for external calls with exponential backoff.
- Settings in `application.properties`: max attempts, wait duration, etc.

## Security

- **JWT Authentication**: Bearer tokens for protected endpoints.
- **Roles**: ADMIN and USER.
- **Free Endpoints**: `/actuator/health`, `/auth/login`, `/auth/register`, `/swagger-ui/**`, `/v3/api-docs/**`.

## Observability

- **OTEL**: Distributed tracing (enabled in deploy profile).
- **Datadog**: Metrics and logs (enabled in deploy profile via `DATADOG_API_KEY` and `DATADOG_APP_KEY`).
- **Actuator**: Health checks, Prometheus metrics exposed at `/actuator/**`.

## Error Handling

The API uses standardized error responses with codes and messages:

- 01: Error with authorization (JWT issues)
- 02: Unexpected error with authorization
- 03: Authentication error
- 04: Invalid credentials
- 05: User not found
- 06: Access denied
- 07: Unexpected error
- 08: Invalid request body
- 09: User already exists

## Deployment

### Local Development
```bash
docker-compose up --build
```

### Kubernetes via Helm
Ensure you have the required secrets (e.g., MySQL credentials, Datadog keys) in the namespace.

```bash
# Dev
helm upgrade --install java-api-boilerplate-dev ./helm/dev --namespace dev

# HML
helm upgrade --install java-api-boilerplate-hml ./helm/hml --namespace hml

# Prod
helm upgrade --install java-api-boilerplate-prod ./helm/prod --namespace prod
```

### CI/CD via Jenkins
The `Jenkinsfile` automates:
- Checkout
- Build (mvn clean compile)
- Test (mvn test)
- Package (mvn package -DskipTests)
- Build Docker Image
- Push to ECR
- Deploy to Dev/HML/Prod based on branch (develop/release/master)

## Troubleshooting

- If tests fail, ensure Docker is running for Testcontainers.
- For local MySQL, set `spring.datasource.url` in `application-local.properties`.
- Check logs in `logs/application.log` for errors.

## API Versioning

The API is versioned via URL paths (e.g., `/v1/auth/login`), but currently at v1.

## Contribution

1. Fork the project
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'feat: new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

### Code Standards
- Java 21 features
- Lombok to reduce boilerplate
- MapStruct for mappings
- Custom exceptions with MessageExceptionFormatter
- Validations within functions
- Follow hexagonal architecture

## License

This project is corporate property. Consult internal terms.
