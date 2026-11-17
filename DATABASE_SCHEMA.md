# Database Schema

## Database connectivity setup

The local development PostgreSQL database is provisioned manually per the project setup:

- Database: `perkflow_db`
- Application role: `perkflow_app`
- Host and port: `localhost:5432`

The password is not stored in this repository. Set `SPRING_DATASOURCE_PASSWORD` in the process environment. The connection URL and username are configurable with `SPRING_DATASOURCE_URL` and `SPRING_DATASOURCE_USERNAME`.

## Schema status

No business tables, entities, repositories, or migrations have been created. `JdbcTemplate` is used only by the connectivity integration test to execute `SELECT 1`. The application does not create the database or tables. Actual connection verification is tracked in `PROGRESS.md`; a configured database and password are required to run it.
