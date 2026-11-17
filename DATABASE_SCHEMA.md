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

This is the first persistence mapping, not a completed customer business feature. There are no repositories, relationships, or CRUD API endpoints.

## Schema lifecycle and migration status

The `CustomerPersistenceIntegrationTest` sets `spring.jpa.hibernate.ddl-auto=create-drop` on its test application context only. Hibernate creates the mapped `customers` table for the test and drops it when that test context closes. The test was verified against the configured PostgreSQL database. No `ddl-auto` value is configured for production in the application properties. No Flyway or other migration tool is present, and no business table is created by the application at production startup.
