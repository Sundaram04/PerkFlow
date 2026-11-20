# Project Overview

## Product vision

PerkFlow is a learning and portfolio project for a fintech-inspired rewards platform. The initial project brief describes a future platform where customers could manage accounts, simulate financial transactions, earn and redeem points, review reward and transaction history, explore promotions, and receive notifications. These are planned product capabilities, not implemented features.

The initial brief also names service-oriented architecture and synchronous REST/asynchronous messaging as long-term learning goals. The current repository is a single Spring Boot application and does not implement service-to-service communication or messaging.

## Current implementation

Group 1, Project Foundation & Environment, is complete. Group 2, Database & Persistence, is now in progress. Implemented capabilities include:

- Spring Boot startup in the `com.perkflow` root package.
- Common and `dev`, `test`, and `prod` properties files with environment-variable overrides.
- Typed binding of `perkflow.*` settings through `PerkFlowProperties`.
- Central exception advice with 400, 404, and generic 500 responses.
- SLF4J logging through the Logback infrastructure supplied by Spring Boot.
- Context/configuration tests and MockMvc tests using a test-only controller.
- PostgreSQL JDBC DataSource configuration and a `JdbcTemplate` connectivity integration test (`SELECT 1`); database verification status is recorded in [PROGRESS.md](PROGRESS.md).
- A `Customer` JPA mapping, verified PostgreSQL-backed `EntityManager` lifecycle integration test, and `CustomerRepository` with derived queries. Repository tests are implemented and awaiting PostgreSQL verification. This is persistence groundwork only; no customer service or production CRUD operations exist.

No production API routes, reward domain model, business tables, persistence repositories, authentication, or message broker currently exists. The `Customer` entity is the first persistence mapping. The application expects the local database and role to be provisioned outside the app. Exact titles for Groups 3–16 and the project Definition of Done were not present in the tracked baseline or the original Task 1 brief available in this repository context; see [PROGRESS.md](PROGRESS.md).

## Technology baseline

The project uses Spring Boot 3.5.16 and targets Java release 21. Maven Wrapper is configured for Maven 3.9.12. See [TECH_STACK.md](TECH_STACK.md) for the distinction between implemented and future technologies.
