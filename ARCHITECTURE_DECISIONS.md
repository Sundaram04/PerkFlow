# Architecture Decisions

## Initial platform choices

- Java 21 is the configured Java language level.
- Spring Boot 3.5.16 is used because it is the latest stable 3.x release compatible with Java 21 and the requested JUnit 5 test stack. Spring Boot 4.x requires JUnit 6 through its Spring Framework 7 test integration.
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
