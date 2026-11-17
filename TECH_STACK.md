# Tech Stack

Confirmed initial choices:

- Java 21
- Spring Boot 3.5.16
- Maven with Maven Wrapper
- Spring Boot Web starter
- Spring Boot Starter Test with Spring Boot managed JUnit 5
- `application.properties` for configuration
- Spring profiles: `default`, `dev`, `test`, and `prod`
- `@ConfigurationProperties` for the typed `perkflow.*` settings group; `@Value` for a single injected property
- `@RestControllerAdvice` and `@ExceptionHandler` for centralized API error responses
- `ApiErrorResponse` record for consistent error payloads
- SLF4J API with Logback, supplied by the Spring Boot Web starter, for application logging
- Spring Boot property-based logging levels with profile-specific settings; standard console output and no file logging setup

No database, security, cache, or messaging technology has been added in this task.
