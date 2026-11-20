# Architecture Decisions

## Confirmed Group 1 decisions

- **One application and root package:** The current implementation is a single Spring Boot web application. `PerkFlowApplication` is at `com.perkflow`, so component/configuration-property scanning covers application subpackages without extra scan configuration.
- **Java and framework:** `pom.xml` sets Java release 21 and uses the Spring Boot 3.5.16 parent. The available verification runtime recorded in the project is Java 26.0.1; the configured release target was not changed.
- **Build:** Maven Wrapper is committed and configured to use Maven 3.9.12. This avoids requiring a separately installed Maven version for normal builds.
- **Group 1 dependencies:** Spring Boot Web and Spring Boot Starter Test established the web/test foundation. Spring JDBC, PostgreSQL JDBC, and later Spring Data JPA were added in Group 2. Logging remains supplied transitively by Spring Boot.
- **Configuration:** `.properties` is the only application configuration format. Common settings are in `application.properties`; `dev`, `test`, and `prod` contain profile overrides. Environment variables provide selected overrides. No credentials or secrets are checked in.
- **Typed settings:** `PerkFlowProperties` binds related `perkflow.*` values as a Java record; `@Value` appears only as a single-property demonstration in a test.
- **Error responses:** `GlobalExceptionHandler` maps `ResourceNotFoundException` to 404, `BadRequestException` to 400, and other `Exception` instances to a generic 500. All mappings use `ApiErrorResponse`. The test controller exists only in test sources.
- **Logging:** Application code uses SLF4J with Boot's Logback implementation. Log levels are profile properties. Startup is INFO, application DEBUG is enabled in dev, and exception advice emits fixed WARN/ERROR messages without logging exception content. No file logging infrastructure is configured.
- **Testing:** Spring Boot Test/JUnit 5 checks application context and property binding; MockMvc tests the exception advice; JDBC/JPA integration tests use the configured PostgreSQL service. No extra test-only dependencies were added.

## Group 2, Task 1: PostgreSQL connectivity

- Add Spring Boot's `spring-boot-starter-jdbc` and the PostgreSQL JDBC driver with versions inherited from the Spring Boot parent. This enables JDBC and Boot's `DataSource` auto-configuration without bringing in JPA/Hibernate.
- Keep the common URL and username defaults in `application.properties`; use the standard `SPRING_DATASOURCE_*` environment variables for overrides and require the password from the environment. Do not include credentials in tracked files.
- Use `JdbcTemplate` for a minimal `SELECT 1` integration test. The test needs a real provisioned local PostgreSQL database and must not be mistaken for a business/API feature.
- No database-specific profile values are needed because all current profiles use the same externally supplied local connection settings. Operators can override URL/username/password per process using standard Spring Boot property environment variables.

## Group 2, Task 2: JPA and Hibernate

- Add Spring Boot's `spring-boot-starter-data-jpa`; let Spring Boot manage Hibernate and its compatible versions. Do not add repository abstractions, entities beyond the first `Customer`, or migration tooling in this task.
- Map `Customer` with Jakarta Persistence annotations and a protected no-argument constructor. Use a database-generated identity key and required, bounded name/email columns; email is unique.
- Demonstrate persistence through `EntityManager`, so the persistence context and lifecycle are visible without adding a production service/repository or REST API.
- Scope Hibernate `create-drop` to the integration test's `@SpringBootTest` properties. Do not configure schema generation in application/profile properties; production must not create or drop tables as a side effect of this task.

## Group 2, Task 3: repositories and relationships

- Define `CustomerRepository` as a Spring Data `JpaRepository` interface and use derived method names for simple lookups; do not hand-write standard CRUD or custom SQL for these operations.
- Keep repository coverage at the PostgreSQL integration layer, using unique email values and rollback transactions to avoid persistent test data.
- Do not add a relationship while `Customer` is the only domain entity. When a real related entity is introduced, decide foreign-key ownership and cascade from its domain lifecycle; use lazy fetching by default and avoid speculative cascades.
- Keep relationship work deferred until the related entity exists. The proposed default for a future customer-owned row is an owning many-to-one association from that row to `Customer`, with the foreign key on the related table, lazy fetching, and no cascade unless aggregate ownership justifies one.

## Deliberately not decided or implemented

PostgreSQL and Hibernate/JPA are selected for local connectivity and the first entity mapping. The project has one Spring Data repository interface but no production CRUD routes, business service layer, schema migrations, authentication, microservices, caching, or messaging. Group 2 is in progress; later tasks have not started. Kafka and RabbitMQ appear as future learning interests in the initial brief, not as technology decisions. The repository does not contain the detailed original 16-group roadmap or a Definition of Done; the tracker notes this rather than reconstructing either from assumptions.
