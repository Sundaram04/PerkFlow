# Architecture

## Current architecture

PerkFlow is one Spring Boot web application. The main class, `com.perkflow.PerkFlowApplication`, is at the package root and enables component scanning for `com.perkflow` and its subpackages. It also scans `@ConfigurationProperties` classes.

```text
com.perkflow
├── PerkFlowApplication.java       # Spring Boot entry point and startup logs
├── PerkFlowProperties.java        # typed perkflow.* settings
├── customer
│   ├── entity
│   │   └── Customer.java          # JPA entity mapping
│   └── repository
│       └── CustomerRepository.java # Spring Data JPA persistence interface
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

## JPA persistence and migrations (Group 2)

Spring Data JPA brings Hibernate as the JPA provider. Hibernate maps `Customer` to the `customers` table. `CustomerRepository` extends `JpaRepository<Customer, Long>` and declares derived queries by email and name and an email existence check. Flyway applies versioned scripts from `classpath:db/migration` before Hibernate initializes. `V1__create_customers_table.sql` creates the initial schema; Hibernate uses `ddl-auto=validate` to check the mapping without changing the schema. Flyway clean is disabled. Repository integration tests use transactions that roll back customer rows.

`Customer` is the only domain entity, so there is no entity relationship yet. Relationship design is deferred until a concrete related entity and its ownership/lifecycle are defined. No speculative associations or cascade rules are added.

## Not implemented

No production CRUD endpoints, business service layer, authentication, microservice boundary, REST client, or asynchronous messaging are implemented. The repository is a persistence interface only. PerkFlow does not create the PostgreSQL database itself; Flyway manages the application schema through versioned migrations.
