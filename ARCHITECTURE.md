# Architecture

The project currently consists of one Spring Boot application. It does not yet implement domain components, persistence, service-to-service communication, or messaging.

The main application class is in the `com.perkflow` root package. Spring Boot component scanning therefore includes this package and its future subpackages by default.
