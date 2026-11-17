# Interview Questions and Answers

These answers describe concepts implemented in the current repository. Database integration verification status is recorded in `PROGRESS.md`.

### Why is the Spring Boot main class in `com.perkflow`?

`@SpringBootApplication` enables component scanning from its package downward. Placing it at the root allows Spring to find application components in `com.perkflow` subpackages.

### What is the role of `SpringApplication.run()`?

It bootstraps Spring Boot, creates the application context, applies auto-configuration, and starts the embedded web server.

### How does a Spring profile affect configuration?

The default profile loads `application.properties`. Activating `dev`, `test`, or `prod` also loads the matching `application-{profile}.properties`, whose values override shared configuration. `SPRING_PROFILES_ACTIVE` selects the profile.

### When would you use `@Value` versus `@ConfigurationProperties`?

`@Value` is suitable for injecting an isolated property. `@ConfigurationProperties` binds related settings under one prefix into a typed object, as `PerkFlowProperties` does for `perkflow.*`.

### How are environment-variable overrides configured?

Spring property placeholders provide environment variables and fallback values, for example `${SERVER_PORT:8081}`. Spring Boot's environment property sources also apply precedence rules among files, environment variables, system properties, and command-line arguments.

### Why use `@RestControllerAdvice` and `@ExceptionHandler`?

They centralize exception handling across MVC controllers. The handler maps specific custom exception types to HTTP statuses and serializes a consistent error response.

### What does `ApiErrorResponse` contain?

It is a Java record containing an `Instant` timestamp, numeric status, standard HTTP reason phrase, message, and request URI path.

### How does PerkFlow avoid returning internal errors to clients?

Known client errors map to their expected 400 or 404 response. Other `Exception` instances return a fixed generic 500 message. Internal exception details are not placed in that response.

### What is the difference between SLF4J and Logback?

SLF4J is the logging API used by application code. Logback is the implementation used by Spring Boot to format and output log events.

### How are Spring MVC errors tested without production endpoints?

`@WebMvcTest` and MockMvc exercise a controller in `src/test` together with the global advice. That controller is not included in production sources.

### What does the Maven Wrapper provide?

It lets contributors invoke the project-configured Maven distribution using `mvnw.cmd` on Windows, without depending on a separately installed Maven version.

### Does the configured Java release prove the app was tested on Java 21?

No. `pom.xml` configures Java release 21, but recorded verification used Java 26.0.1 because that was the available runtime. A Java 21 runtime verification remains outstanding.

### What is the difference between JPA, Hibernate, and Spring Data JPA?

JPA is the Jakarta Persistence API specification. Hibernate is the ORM provider implementing that API. Spring Data JPA adds repository abstractions on top of JPA; the dependency is present, but this task does not define a repository.

### What is ORM?

Object-relational mapping maps Java objects and fields to relational tables and columns. PerkFlow's `Customer` entity maps to the `customers` table through Jakarta Persistence annotations.

### What is a persistence context?

It is the set of entities managed by an `EntityManager`. Hibernate tracks their identity and changes there and uses it as a first-level cache. `clear()` detaches all managed entities; after that, `find()` reloads them from the database.

### What does `EntityManager.flush()` do?

It synchronizes pending changes in the persistence context to the database by issuing SQL. It does not commit the surrounding transaction.

### What are the four common JPA entity states?

Transient entities are new and untracked; managed entities belong to a persistence context; detached entities have an identity but are not currently tracked; removed entities are marked for deletion. The integration test demonstrates these states with `persist`, `find`, `clear`, `detach`, `remove`, and `flush`.

### Why does the JPA integration test use `create-drop`?

The test applies Hibernate's `create-drop` schema strategy only to its own Spring test context, so it does not require a manually created `customers` table. Production properties do not enable schema generation, and the test uses the configured PostgreSQL database rather than an in-memory substitute.
