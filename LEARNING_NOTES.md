# Learning Notes

## Configuration management (Group 1, Task 2)

Spring profiles select environment-specific settings while keeping the application code the same. With no active profile, Spring uses the `default` profile and loads the common `application.properties`. Setting `SPRING_PROFILES_ACTIVE` to `dev`, `test`, or `prod` loads the corresponding `application-{profile}.properties` alongside the common file. Profile-specific values take precedence over common file values.

The profile can be selected in PowerShell for the current terminal session:

```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw.cmd spring-boot:run
Remove-Item Env:SPRING_PROFILES_ACTIVE
```

Use `test` or `prod` in place of `dev` to select those profiles. Production deployment should set `SPRING_PROFILES_ACTIVE=prod` outside the application rather than hard-coding it in the common properties file.

The example ports are 8081 for the default profile, 8082 for `dev`, 8083 for `test`, and 8080 for `prod`. `SERVER_PORT` can replace any of these values.

Important settings have environment-variable defaults. For example, `server.port=${SERVER_PORT:8081}` means Spring uses `SERVER_PORT` when provided and falls back to `8081` otherwise. Profile files provide their own fallback ports. `PERKFLOW_ENVIRONMENT` similarly overrides the environment label. Environment variables are higher-precedence property sources than application files; command-line arguments can override them. Spring Boot also supports Java system properties and other external configuration sources.

`@Value` injects one property into a Spring bean, which is convenient for a small number of isolated values. `@ConfigurationProperties` binds related values under a prefix into a typed object; `PerkFlowProperties` collects the `perkflow.*` settings. It is easier to organize, validate, and test as a configuration group.

No passwords, API keys, or other secrets are stored in these files. Production secrets should be supplied through a secured deployment configuration source.

## Global exception handling (Group 1, Task 3)

Global exception handling keeps controllers from repeating error-to-HTTP-response conversion. `@RestControllerAdvice` applies shared controller advice across the application and writes returned objects as response bodies. `@ExceptionHandler` maps a specific exception type to a method that chooses its status and response.

PerkFlow uses `ResourceNotFoundException` for a missing resource (404) and `BadRequestException` for invalid client input (400). These types make the intent explicit and let the handler choose a status without guessing from message text. Other `Exception` instances receive a generic 500 response; their exception message and internal details are not returned.

All handled errors use `ApiErrorResponse`: an ISO-8601 `Instant` timestamp, numeric HTTP status, standard HTTP reason phrase, message, and request URI path. Client errors retain their useful, safe exception message. Unexpected errors use a fixed generic message. The handler does not log exceptions; logging is reserved for a later task.
