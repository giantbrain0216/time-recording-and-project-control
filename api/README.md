# Spring Boot RESTful API

> This is the SoPra WS20/21 skeleton project for a RESTful API with Spring Boot.
> It exposes REST resources under http://localhost:8080/api/v1/.
> See the a list of resources in the Swagger UI (http://localhost:8080/api/v1/swagger-ui) or OpenAPI documentation as JSON (http://localhost:8080/api/v1/api-docs).

## Prerequisites

- Install a JDK in at least version 11.
- Make sure that `JAVA_HOME` is set correctly to the root directory of your JDK. You can check with this command: `echo %JAVA_HOME%` (or `echo $JAVA_HOME` on Linux / Git Bash)
- Make sure that the JDK `bin` folder is added to your `PATH`.
- You can also install Maven yourself, but if you use the Maven Wrapper scripts (`mvnw` or `mvnw.cmd`), this is not necessary, since Maven will be downloaded in this case.

## Usage

```bash
# build and package executable --> appears in target/sopraws20-api.jar
./mvnw clean install

# execute tests only
./mvnw clean test

# generate test coverage report (execute tests first) --> appears in target/site/jacoco/index.html
./mvnw jacoco:report

# build and package executable without running tests
./mvnw clean install -DskipTests

# run the created JAR file --> http://localhost:8080/api/v1/
java -jar ./target/sopraws20-api.jar

# for development: build and run in live-reload mode
./mvnw spring-boot:run
```

## Documentation

- General references: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle
- controllers.Application properties: https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html
