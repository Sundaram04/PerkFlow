# Architecture Decisions

## Group 1 architecture overview

PerkFlow is currently a minimal Spring Boot web application. `PerkFlowApplication` starts the application and scans typed `perkflow.*` settings into the `PerkFlowProperties` record. Common settings live in `application.properties`; `dev`, `test`, and `prod` override environment-specific values. There are no business controllers or persistence components yet. When an application exception reaches the web layer, `GlobalExceptionHandler` maps it to the standard `ApiErrorResponse`. SLF4J logging calls use the Logback implementation supplied by Spring Boot.

### Platform decisions

- Java 21 is the configured Java language level.
- Spring Boot 3.5.16 is the selected version and is configured with Java 21 and Spring Boot's managed JUnit 5 test stack.
- Maven and the Maven Wrapper provide the build entry point.
- `application.properties` is the sole application configuration format.
- Common settings and environment-specific values are separated into `application.properties` and `application-{profile}.properties`. The profiles use example ports and environment labels only; they contain no credentials or secrets.
- `PerkFlowProperties` uses `@ConfigurationProperties` for the related `perkflow.*` values. `@Value` is demonstrated for a single property in the context test.
- Controller exceptions are converted centrally by `GlobalExceptionHandler` into the shared `ApiErrorResponse` shape. Missing resources map to 404, invalid client input to 400, and unexpected exceptions to a safe generic 500 response.
- Spring Boot's existing SLF4J and Logback stack is used for logging; no additional logging dependency or file appender is configured.
- Logging levels are configured in properties: application events use INFO by default, development enables application DEBUG, and production keeps application INFO while limiting framework output to WARN.
- The exception advice logs invalid requests as a fixed WARN event and unexpected failures as a fixed ERROR event with only the exception type. It does not log exception messages or expose internal details in the generic 500 response.
- Never log credentials, tokens, secrets, or sensitive user data.

These choices establish the requested project foundation. No domain or distributed-systems architecture decisions have been made yet.
