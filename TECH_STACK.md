# Technology Stack

## Currently implemented

- Java source and Maven compiler release target: 21 (`java.version` in `pom.xml`).
- Spring Boot: 3.5.16.
- Build: Maven Wrapper, configured to download/use Maven 3.9.12; wrapper scripts are checked in.
- Runtime framework: `spring-boot-starter-web`.
- Database connectivity: `spring-boot-starter-jdbc` 3.5.16 and PostgreSQL JDBC driver 42.7.11, resolved through Spring Boot dependency management (no explicit versions in `pom.xml`).
- Connection pool: HikariCP 6.3.3, brought in by the JDBC starter.
- JDBC access: Spring `JdbcTemplate`; Boot auto-configures the `DataSource` from `spring.datasource.*` properties.
- ORM/persistence: `spring-boot-starter-data-jpa` 3.5.16, with Hibernate ORM 6.6.53.Final supplied as its JPA provider; one `Customer` entity, `CustomerRepository`, and direct `EntityManager` use in integration tests.
- Schema migrations: Flyway core and PostgreSQL database module, version 11.7.2 managed by Spring Boot; versioned SQL migrations under `src/main/resources/db/migration`.
- Tests: `spring-boot-starter-test` (test scope), Spring Boot Test, JUnit 5, and MockMvc.
- Configuration: Java `.properties` files only; default plus `dev`, `test`, and `prod` profiles; `PerkFlowProperties` binds `perkflow.*` settings.
- Error handling: Spring MVC `@RestControllerAdvice`, `@ExceptionHandler`, and the `ApiErrorResponse` record.
- Logging: SLF4J API and Logback are available through Spring Boot's logging starter brought in by the Web starter; application and profile logging levels are properties-driven.
- Version control: Git repository on branch `main`, with the origin remote `https://github.com/Sundaram04/PerkFlow.git`.

## Planned or not selected

- Group 2 is titled Database & Persistence and is in progress. PostgreSQL/JDBC connectivity and the Task 1 connectivity test are implemented. Tasks 2 and 3 add `Customer`, `CustomerRepository`, and PostgreSQL integration tests; Task 3 verification remains pending. Task 4 adds Flyway V1 and changes Hibernate to `validate`, with clean disabled. Task 4 and full-suite database verification remain pending in this environment. There is no business API.
- The original project brief names PostgreSQL, Kafka, RabbitMQ, and distributed systems as longer-term learning interests. They are not dependencies or implemented architecture. The detailed mapping of these technologies to later groups is not recorded in the repository.
- Microservices, authentication/security, caching, business APIs, and notification delivery are future possibilities, not current features.

## Supporting tools

- Windows PowerShell commands use `mvnw.cmd`.
- `.gitignore` excludes Maven output, IDE files, local environment files, and common secret/key files.
- Git history contains five task-level commits for Group 1. Current Group 2 work remains uncommitted.

The PostgreSQL database and `perkflow_app` role are a local prerequisite supplied outside the application. Connection defaults are in `application.properties`; the password is externalized as `SPRING_DATASOURCE_PASSWORD`.
