# Mistakes, Fixes, and Verification Caveats

## Java runtime differs from the configured release

The committed progress notes and Maven test output show that the available runtime in the verification environment was Java 26.0.1, while `pom.xml` configures `java.version` as 21. Maven's compiler plugin uses the Java 21 release target. No Java-version workaround or change to the target was made. For a Java 21 runtime check, install/select JDK 21 and rerun `java -version` and `.\mvnw.cmd clean test` in PowerShell.

## No other persisted project-specific failures

The audit test run emitted non-failing toolchain warnings: Mockito dynamically attached its inline-mock agent, and Maven's bootstrap reported reflective final-field mutation. All tests completed successfully. No dependencies or JVM flags were added to suppress these warnings; the application code and build configuration were left unchanged.

The current Git history contains the five completed task commits, and the committed progress records successful builds/tests. It does not contain a separate failure log or verified code defect/fix beyond the environment caveats above. No other incidents are inferred.
