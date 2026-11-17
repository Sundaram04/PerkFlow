# Architecture

## Current architecture

PerkFlow is one Spring Boot web application. The main class, `com.perkflow.PerkFlowApplication`, is at the package root and enables component scanning for `com.perkflow` and its subpackages. It also scans `@ConfigurationProperties` classes.

```text
com.perkflow
├── PerkFlowApplication.java       # Spring Boot entry point and startup logs
├── PerkFlowProperties.java        # typed perkflow.* settings
├── customer
│   └── entity
│       └── Customer.java          # JPA entity mapping
└── exception
    ├── ApiErrorResponse.java      # common API error payload
    ├── BadRequestException.java   # client error type (400)
    ├── ResourceNotFoundException.java # missing resource type (404)
    └── GlobalExceptionHandler.java # controller advice and status mapping
```

Configuration is in `src/main/resources/application.properties` plus `application-dev.properties`, `application-test.properties`, and `application-prod.properties`. Environment variables can override selected values. There are no production controllers yet. `src/test/java/com/perkflow/exception/TestExceptionController.java` exists only to exercise exception advice with MockMvc and is not included in the production artifact.

When an exception reaches Spring MVC, `GlobalExceptionHandler` creates an `ApiErrorResponse` with timestamp, status, reason phrase, message, and request path. `ResourceNotFoundException` maps to 404, `BadRequestException` to 400, and other `Exception` instances receive a generic 500 response. Unexpected failures are logged without returning internal details to the client.

Application code uses SLF4J; Spring Boot's logging starter supplies Logback. Common and profile-specific properties set logging levels. Output uses Spring Boot's default console configuration; there is no custom file appender.

## Database connectivity (Group 2, Task 1)

The application uses Spring Boot JDBC auto-configuration to create a `DataSource` from shared `spring.datasource.*` properties. The PostgreSQL JDBC driver is available at runtime, and `JdbcTemplate` is used for direct SQL. Task 1's Surefire report records a passing `SELECT 1` integration test. The defaults target the locally provisioned `perkflow_db` database and `perkflow_app` user; the password is required from `SPRING_DATASOURCE_PASSWORD`.

## JPA mapping (Group 2, Task 2)

Spring Data JPA brings Hibernate as the JPA provider. Hibernate maps `Customer` to the `customers` table. Entity persistence is demonstrated through `EntityManager`; no Spring Data repository is defined. The PostgreSQL-backed lifecycle integration test passes. It applies `spring.jpa.hibernate.ddl-auto=create-drop` only to its own test context, so the test creates and drops the mapped table without enabling schema generation in application properties.

## Not implemented

No production CRUD endpoints, repository layer, schema migrations, authentication, microservice boundary, REST client, or asynchronous messaging are implemented. The `Customer` table is test-generated only; PerkFlow does not create the local database or manage a production schema.
