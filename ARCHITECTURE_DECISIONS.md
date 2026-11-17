# Architecture Decisions

## Confirmed Group 1 decisions

- **One application and root package:** The current implementation is a single Spring Boot web application. `PerkFlowApplication` is at `com.perkflow`, so component/configuration-property scanning covers application subpackages without extra scan configuration.
- **Java and framework:** `pom.xml` sets Java release 21 and uses the Spring Boot 3.5.16 parent. The available verification runtime recorded in the project is Java 26.0.1; the configured release target was not changed.
- **Build:** Maven Wrapper is committed and configured to use Maven 3.9.12. This avoids requiring a separately installed Maven version for normal builds.
- **Dependencies:** Only Spring Boot Web and Spring Boot Starter Test are declared. Logging is supplied transitively by Spring Boot; no standalone logging dependency was introduced.
- **Configuration:** `.properties` is the only application configuration format. Common settings are in `application.properties`; `dev`, `test`, and `prod` contain profile overrides. Environment variables provide selected overrides. No credentials or secrets are checked in.
- **Typed settings:** `PerkFlowProperties` binds related `perkflow.*` values as a Java record; `@Value` appears only as a single-property demonstration in a test.
- **Error responses:** `GlobalExceptionHandler` maps `ResourceNotFoundException` to 404, `BadRequestException` to 400, and other `Exception` instances to a generic 500. All mappings use `ApiErrorResponse`. The test controller exists only in test sources.
- **Logging:** Application code uses SLF4J with Boot's Logback implementation. Log levels are profile properties. Startup is INFO, application DEBUG is enabled in dev, and exception advice emits fixed WARN/ERROR messages without logging exception content. No file logging infrastructure is configured.
- **Testing:** Spring Boot Test/JUnit 5 checks application context and property binding; MockMvc tests the exception advice. There are no extra test dependencies.

## Deliberately not decided or implemented

The project has not selected a database or ORM, defined a schema, created production API/domain/service/repository components, or implemented authentication, microservices, caching, or messaging. Group 2 is titled Database & Persistence but has not started. PostgreSQL, Kafka, and RabbitMQ appear as future learning interests in the initial brief, not as technology decisions. The repository does not contain the detailed original 16-group roadmap or a Definition of Done; the tracker notes this rather than reconstructing either from assumptions.
