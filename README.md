# PerkFlow

PerkFlow is a Java/Spring learning project for a rewards platform. The long-term product vision includes accounts, simulated transactions, points, rewards, promotions, and notifications. Those business capabilities are not implemented yet.

## Current status

Group 1, Project Foundation & Environment, is complete. The repository currently contains a Spring Boot application foundation, profile-based configuration, typed configuration binding, centralized API error handling, profile-aware logging, and tests. There are no production business endpoints, database integrations, authentication components, or messaging systems. Group 2, Database & Persistence, has not started.

## Current technologies

- Java release target 21
- Spring Boot 3.5.16
- Maven through the Maven Wrapper (configured distribution 3.9.12)
- Spring Boot Web and Spring Boot Starter Test
- `application.properties` and `dev`, `test`, `prod` profiles

See [TECH_STACK.md](TECH_STACK.md) for implemented, planned, and supporting technologies.

## Build, test, and run on Windows

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

The default server port is 8081. There is no production endpoint, so the root URL is not an implemented application API.

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
