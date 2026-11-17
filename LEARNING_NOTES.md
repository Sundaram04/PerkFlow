# Learning Notes

This document summarizes implemented Group 1 concepts. Product ideas and later-group technologies are labeled as future work; they are not described as existing features.

## Group 1 implementation summary

| Task | Objective and concepts | Implementation and decisions | Verification status |
| --- | --- | --- | --- |
| 1. Project Initialization & Foundation | Establish a minimal Java/Spring Boot application, Maven build, wrapper, properties, Git conventions, and initial docs. Concepts: `@SpringBootApplication`, `SpringApplication.run`, component-scan root, Maven starters, and application properties. | Java release 21 and Spring Boot 3.5.16 in `pom.xml`; Web and test starters only; app entry point in `com.perkflow`; Maven Wrapper set to Maven 3.9.12; default port was initially 8080. No business endpoints or persistence. | The committed progress record says the context test/build and startup on port 8080 were verified with Java 26.0.1. Exact original command output is not retained in Git. |
| 2. Configuration Management | Separate shared and environment-specific settings; understand profiles, property precedence, `@Value`, and typed binding. | `application.properties` plus `application-{dev,test,prod}.properties`; defaults/overrides for port and `perkflow.*`; `PerkFlowProperties` record, `@ConfigurationPropertiesScan`, and a test field using `@Value`. Profile ports are 8082/8083/8080; default is 8081. No credentials. | The committed progress record reports profile and override checks. The current context test asserts values and typed binding; this audit reran default, all profiles, and environment overrides successfully. |
| 3. Global Exception Handling | Centralize MVC exception-to-HTTP mapping and keep a consistent error contract. | `GlobalExceptionHandler` uses `@RestControllerAdvice` and `@ExceptionHandler`; custom runtime exceptions map to 404/400, other `Exception` types to a safe generic 500. `ApiErrorResponse` is a record. Test-only MVC routes avoid adding dummy production endpoints. | This audit's MockMvc tests passed for each mapping, required JSON fields, request path, and generic unexpected-error message. |
| 4. Logging | Add useful application events using Spring Boot's built-in logging stack. | SLF4J API with Logback supplied by the Web starter; root/application levels in properties, with profile overrides; startup, WARN client rejection, and ERROR unexpected-exception logs. No file appender or added logging dependency. | Task 4's committed progress says default startup on port 8081 and tests passed. Task 5 added profile logging assertions; this audit reran the suite under each profile and default startup successfully. |
| 5. Testing, Documentation & Final Verification | Review Group 1, check profile logging configuration, and finalize docs. | Context assertions cover logging levels; error tests continue to cover generic response safety; docs describe actual scope and future work. | Commit `ac624d4` records Task 5 and 4 passing tests. This audit repeated clean tests, all profile runs, overrides, default startup, and package verification; outcomes are in `PROGRESS.md`. |

### Commands and task-level verification evidence

- **Task 1:** The original brief prescribed `.\mvnw.cmd clean package` and `.\mvnw.cmd spring-boot:run`; the initial committed `PROGRESS.md` records build/start verification at port 8080, but not the exact invocations or full output. This audit reran `.\mvnw.cmd clean test` and `.\mvnw.cmd spring-boot:run`; a final package run is recorded in the audit tracker.
- **Task 2:** This audit ran `.\mvnw.cmd -q test` once for each `SPRING_PROFILES_ACTIVE` value (`dev`, `test`, `prod`) and with environment overrides set. All runs passed.
- **Task 3:** `.\mvnw.cmd clean test` passed; its three MockMvc tests exercised each exception mapping and payload.
- **Task 4:** The same clean test suite passed, including the profile logging assertions; default `.\mvnw.cmd spring-boot:run` started on port 8081.
- **Task 5:** The audit ran clean tests, each profile test run, the environment override test, default startup, and `git diff --check`. Outcomes are tracked in `PROGRESS.md`.

### Task 1 concepts

`@SpringBootApplication` combines configuration, auto-configuration, and component scanning. Because `PerkFlowApplication` is in `com.perkflow`, component scanning naturally covers that package and descendants. `SpringApplication.run()` creates and runs the application context. Maven starters collect compatible dependencies; the wrapper lets Windows use the repository's configured Maven version without requiring a separately installed Maven.

### Task 2 configuration and precedence

With no active profile, Spring uses the default profile and loads `application.properties`. Setting `SPRING_PROFILES_ACTIVE` to `dev`, `test`, or `prod` loads the matching `application-{profile}.properties`; profile-specific values override shared file values. Environment variables can override placeholders such as `server.port=${SERVER_PORT:8081}` and `perkflow.environment=${PERKFLOW_ENVIRONMENT:default}`. Spring's external property sources include environment variables and command-line arguments; command-line arguments have higher precedence than environment variables. No secret values are stored in repository properties.

`@Value` is convenient for one isolated setting. `@ConfigurationProperties` groups related settings under a prefix into a typed configuration object. `PerkFlowProperties` binds `perkflow.environment` and `perkflow.display-name`.

| Profile | Environment label default | Port default | Root log level | `com.perkflow` log level |
| --- | --- | ---: | --- | --- |
| default | `default` | 8081 | INFO | INFO |
| dev | `development` | 8082 | INFO | DEBUG |
| test | `test` | 8083 | WARN | INFO |
| prod | `production` | 8080 | WARN | INFO |

`SERVER_PORT` is available in each profile. `PERKFLOW_ENVIRONMENT` overrides the environment label. `PERKFLOW_DISPLAY_NAME` overrides the common display name. Development additionally sets `logging.level.org.springframework=WARN`.

### Task 3 exception handling

`@RestControllerAdvice` applies exception-handling methods across Spring MVC controllers. `@ExceptionHandler` selects a handler by exception type. Missing resources map to HTTP 404, invalid client requests to 400, and other `Exception` instances to 500. `ApiErrorResponse` has an `Instant` timestamp, numeric status, reason phrase, message, and request URI. Client-error messages come from their custom exceptions; unexpected failures get a fixed generic message. Stack traces and internal exception details are not returned in the body.

### Task 4 logging

SLF4J is the logging facade used by application code; Logback is the implementation. Spring Boot configures the stack through its starter and reads logger levels from properties. TRACE is very detailed, DEBUG helps diagnose, INFO records important events, WARN records unusual/recoverable conditions, and ERROR records unexpected failures. PerkFlow logs startup at INFO, active profiles at DEBUG, rejected invalid requests at WARN, and unexpected exceptions at ERROR. The error log includes only the exception type, not the exception message or stack trace. Never log secrets or unnecessary personal data.

## Windows commands

Run the default-profile suite and application:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Run the suite under a profile (replace `dev` with `test` or `prod`):

```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw.cmd test
Remove-Item Env:SPRING_PROFILES_ACTIVE
```

Exercise environment-variable overrides in the configuration test:

```powershell
$env:SERVER_PORT = "8188"
$env:PERKFLOW_ENVIRONMENT = "override-check"
$env:PERKFLOW_DISPLAY_NAME = "PerkFlow Override Check"
.\mvnw.cmd test
Remove-Item Env:SERVER_PORT, Env:PERKFLOW_ENVIRONMENT, Env:PERKFLOW_DISPLAY_NAME
```

The Group 1 tasks did not implement a database, API business routes, security, messaging, or microservices. Group 2 (Database & Persistence) is now in progress; its database connection test is described below.

## Group 2, Task 1: PostgreSQL Setup & Database Connectivity

### Concepts and implementation

PostgreSQL is a relational database. The PostgreSQL JDBC driver implements JDBC, the standard Java interface for opening connections and executing SQL against a database. `spring-boot-starter-jdbc` adds Spring JDBC and HikariCP connection-pool support; the `postgresql` driver is runtime-scoped. The resolved versions are Spring Boot JDBC 3.5.16, HikariCP 6.3.3, and PostgreSQL JDBC 42.7.11, all managed by the Spring Boot parent without explicit versions in `pom.xml`.

Spring Boot's datasource auto-configuration reads `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password`, creates a `DataSource`, and makes JDBC access available to the application. PerkFlow keeps local URL and username defaults in common `application.properties`; password has no default and must come from `SPRING_DATASOURCE_PASSWORD`. Spring's relaxed binding recognizes the standard `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` environment variables. Database connection settings are common to the current profiles, so no duplicate profile datasource values were added. The local database and role are provisioned outside the app.

`JdbcTemplate` wraps repetitive JDBC work such as obtaining a connection, preparing/executing SQL, mapping results, and releasing resources. `PostgresConnectivityIntegrationTest` injects it and runs `SELECT 1`, expecting integer `1`. The test is a real integration test: Spring creates its datasource and it needs the PostgreSQL server, database, role, and password to be available. No REST endpoint is needed. This task does not add JPA/Hibernate, entities, repositories, migrations, or business tables.

### Local setup and commands

Ensure PostgreSQL is running and the local database `perkflow_db` and role `perkflow_app` exist. Supply the password only in the local process environment. In PowerShell, replace the placeholder with the local password (never commit or share it):

```powershell
$env:SPRING_DATASOURCE_PASSWORD = "<local PostgreSQL password>"
.\mvnw.cmd -Dtest=PostgresConnectivityIntegrationTest test
.\mvnw.cmd clean test
Remove-Item Env:SPRING_DATASOURCE_PASSWORD
```

Optional URL/username overrides:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/perkflow_db"
$env:SPRING_DATASOURCE_USERNAME = "perkflow_app"
```

Verification status for this task remains pending until the real database integration test succeeds. See `PROGRESS.md` for the recorded outcome and any environment limitation.
