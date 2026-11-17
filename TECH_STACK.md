# Technology Stack

## Currently implemented

- Java source and Maven compiler release target: 21 (`java.version` in `pom.xml`).
- Spring Boot: 3.5.16.
- Build: Maven Wrapper, configured to download/use Maven 3.9.12; wrapper scripts are checked in.
- Runtime framework: `spring-boot-starter-web`.
- Tests: `spring-boot-starter-test` (test scope), Spring Boot Test, JUnit 5, and MockMvc.
- Configuration: Java `.properties` files only; default plus `dev`, `test`, and `prod` profiles; `PerkFlowProperties` binds `perkflow.*` settings.
- Error handling: Spring MVC `@RestControllerAdvice`, `@ExceptionHandler`, and the `ApiErrorResponse` record.
- Logging: SLF4J API and Logback are available through Spring Boot's logging starter brought in by the Web starter; application and profile logging levels are properties-driven.
- Version control: Git repository on branch `main`, with the origin remote `https://github.com/Sundaram04/PerkFlow.git`.

## Planned or not selected

- Group 2 is titled Database & Persistence and is not started. No database product, ORM, schema, or persistence library has been selected or added.
- The original project brief names PostgreSQL, Kafka, RabbitMQ, and distributed systems as longer-term learning interests. They are not dependencies or implemented architecture. The detailed mapping of these technologies to later groups is not recorded in the repository.
- Microservices, authentication/security, caching, business APIs, and notification delivery are future possibilities, not current features.

## Supporting tools

- Windows PowerShell commands use `mvnw.cmd`.
- `.gitignore` excludes Maven output, IDE files, local environment files, and common secret/key files.
- Git history contains five task-level commits for Group 1; no audit changes have been committed.
