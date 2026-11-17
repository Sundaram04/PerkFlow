# Architecture Decisions

## Initial platform choices

- Java 21 is the configured Java language level.
- Spring Boot 3.5.16 is used because it is the latest stable 3.x release compatible with Java 21 and the requested JUnit 5 test stack. Spring Boot 4.x requires JUnit 6 through its Spring Framework 7 test integration.
- Maven and the Maven Wrapper provide the build entry point.
- `application.properties` is the sole application configuration format.

These choices establish the requested project foundation. No domain or distributed-systems architecture decisions have been made yet.
