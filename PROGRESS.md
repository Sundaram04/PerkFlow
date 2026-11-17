# Progress

## Group 1: IN PROGRESS

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

### Task 3: Global Exception Handling — IN PROGRESS

- [ ] Standard API error response and custom exceptions
- [ ] Central exception advice and status mapping
- [ ] MVC tests for response shape, status, message safety, and request path
- [ ] Documentation updated and clean test suite verified
