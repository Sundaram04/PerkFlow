# PerkFlow

PerkFlow is a Java/Spring learning project for a rewards platform. The long-term product vision includes accounts, simulated transactions, points, rewards, promotions, and notifications. Those business capabilities are not implemented yet.

## Current status

Group 1, Project Foundation & Environment, is complete. Group 2, Database & Persistence, is in progress. PostgreSQL JDBC connectivity, the PostgreSQL-backed `Customer` JPA lifecycle test, and a `CustomerRepository` persistence interface with derived queries are implemented. Task 3 repository tests are awaiting PostgreSQL verification. There is no customer service, CRUD API, or production schema-generation configuration. There are no production business endpoints, authentication components, or messaging systems.

## Current technologies

- Java release target 21
- Spring Boot 3.5.16
- Maven through the Maven Wrapper (configured distribution 3.9.12)
- Spring Boot Web and Spring Boot Starter Test
- Spring Boot JDBC and the PostgreSQL JDBC driver
- Spring Data JPA starter, which supplies Hibernate as the JPA provider
- `application.properties` and `dev`, `test`, `prod` profiles

See [TECH_STACK.md](TECH_STACK.md) for implemented, planned, and supporting technologies.

## Build, test, and run on Windows

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

The default server port is 8081. There is no production endpoint, so the root URL is not an implemented application API.
The full application test suite now needs the local PostgreSQL setup and `SPRING_DATASOURCE_PASSWORD` described below. The JPA test uses test-scoped `create-drop` schema generation for the `customers` mapping; no production schema-generation setting is enabled.

## Local PostgreSQL setup

The application expects a local PostgreSQL server at `localhost:5432`, database `perkflow_db`, and username `perkflow_app`. Create the database and application user using your local PostgreSQL administration process. PerkFlow does not create the database or tables. Supply the password through `SPRING_DATASOURCE_PASSWORD`; do not put it in a tracked properties file.

PowerShell example (replace the placeholder locally):

```powershell
$env:SPRING_DATASOURCE_PASSWORD = "<local PostgreSQL password>"
.\mvnw.cmd spring-boot:run
Remove-Item Env:SPRING_DATASOURCE_PASSWORD
```

The common datasource URL and username defaults are `jdbc:postgresql://localhost:5432/perkflow_db` and `perkflow_app`. `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` override them. Run the integration tests after setting the password variable:

```powershell
.\mvnw.cmd -Dtest=PostgresConnectivityIntegrationTest test
.\mvnw.cmd -Dtest=CustomerPersistenceIntegrationTest test
.\mvnw.cmd clean test
```

The connectivity test executes `SELECT 1` using `JdbcTemplate`; the JPA test persists, flushes, clears, reloads, detaches, and removes a `Customer` using `EntityManager`.

Activate an environment profile in PowerShell for the current session:

```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw.cmd spring-boot:run
Remove-Item Env:SPRING_PROFILES_ACTIVE
```

Use `test` or `prod` in place of `dev`. The example ports are 8082, 8083, and 8080 respectively; `SERVER_PORT` can override the selected port. Configuration details are in [LEARNING_NOTES.md](LEARNING_NOTES.md).

## Project documents

- [PROJECT_OVERVIEW.md](PROJECT_OVERVIEW.md) — purpose and current scope
- [ARCHITECTURE.md](ARCHITECTURE.md) — implemented architecture
- [API_DOCUMENTATION.md](API_DOCUMENTATION.md) — current API status
- [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) — current persistence status
- [PROGRESS.md](PROGRESS.md) — group/task tracker and verification record
