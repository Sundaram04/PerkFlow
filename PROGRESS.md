# Progress

## Group 1: COMPLETE

### Task 1: Project Initialization & Foundation

- [x] Initial Spring Boot application structure
- [x] Java 21 and Maven configuration
- [x] `application.properties`
- [x] Starter documentation
- [x] Maven Wrapper generation and build verification
- [x] Confirm application startup on the configured port

Verification was run with Java 26.0.1 because Java 21 is not installed in the current environment. The project targets Java 21 in Maven configuration.

### Task 2: Configuration Management

- [x] Common and profile-specific properties files
- [x] Environment-variable overrides and typed configuration binding
- [x] Default, dev, test, prod, and override verification
- [x] Update configuration learning notes

Verification used Java 26.0.1 because Java 21 is not installed in the current environment; compilation targets Java 21. The default-profile app also started on port 8081 and returned HTTP 404 at `/`, as no endpoint is defined.

### Task 3: Global Exception Handling — COMPLETE

- [x] Standard API error response and custom exceptions
- [x] Central exception advice and status mapping
- [x] MVC tests for response shape, status, message safety, and request path
- [x] Documentation updated and clean test suite verified

Verification: `mvnw.cmd clean test` passed with 4 tests, 0 failures, and 0 errors. The test-only MVC controller exercises all three error mappings without adding production endpoints.

### Task 4: Logging — COMPLETE

- [x] Confirm existing Spring Boot logging infrastructure (SLF4J and Logback via the web starter)
- [x] Configure common and profile-specific logging levels
- [x] Add startup and exception-handling logs without sensitive details
- [x] Update logging documentation
- [x] Run clean tests and verify default-profile startup

Verification: `mvnw.cmd clean test` passed with 4 tests, 0 failures, and 0 errors. The existing test suite also passed under `dev`, `test`, and `prod`. The default-profile application started successfully on port 8081 and emitted the configured INFO startup event. Maven ran on Java 26.0.1 in this environment while compiling for the configured Java 21 release.

### Task 5: Testing, Documentation & Final Verification — COMPLETE

- [x] Reviewed configuration, exception handling, logging, tests, Maven setup, and Group 1 documentation
- [x] Asserted profile-specific logging levels in the application-context test
- [x] Verified clean tests, each profile, environment-variable overrides, and final diff
- [x] Finalized documentation and confirmed Group 1 scope

Final verification: `mvnw.cmd clean test` passed with 4 tests, 0 failures, and 0 errors. The suite also passed under `dev`, `test`, and `prod`, and with `SERVER_PORT`, `PERKFLOW_ENVIRONMENT`, and `PERKFLOW_DISPLAY_NAME` overridden by environment variables. Default-profile startup was previously verified on port 8081. Java 26.0.1 was used for verification; Maven compiles with the configured release target 21.
