# Database Schema

## PostgreSQL setup

The local development PostgreSQL database is provisioned outside PerkFlow:

- Database: `perkflow_db`
- Application role: `perkflow_app`
- Host and port: `localhost:5432`

The password is supplied through `SPRING_DATASOURCE_PASSWORD` and is not stored in this repository.

## Current entity mapping

`com.perkflow.customer.entity.Customer` maps to table `customers`:

| Java field | Mapping | Constraints |
| --- | --- | --- |
| `id` (`Long`) | Generated primary key | `GenerationType.IDENTITY` |
| `name` (`String`) | Column `name` | Required, maximum length 100 |
| `email` (`String`) | Column `email` | Required, unique, maximum length 254 |

This is the first persistence mapping, not a completed customer business feature. `CustomerRepository` provides persistence operations using Spring Data JPA, but there are no business services or CRUD API endpoints.

## Repository and relationship status

`com.perkflow.customer.repository.CustomerRepository` extends `JpaRepository<Customer, Long>`. It adds derived lookups by email and name plus an email existence check; the standard CRUD operations come from `JpaRepository`. Repository integration tests exercise these operations against PostgreSQL and roll back their test transactions.

`Customer` is currently the only domain entity, so no entity relationship is mapped. Relationship implementation is deferred until a real related domain entity is introduced. At that point, the foreign-key owner, fetch behavior (lazy by default), and any cascade behavior should follow the actual domain ownership and lifecycle. No cascade is assumed in advance.

## Schema lifecycle and migration status

The `CustomerPersistenceIntegrationTest` sets `spring.jpa.hibernate.ddl-auto=create-drop` on its test application context only. Hibernate creates the mapped `customers` table for the test and drops it when that test context closes. The test was verified against the configured PostgreSQL database. No `ddl-auto` value is configured for production in the application properties. No Flyway or other migration tool is present, and no business table is created by the application at production startup.
