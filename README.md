# spring-modulith-demo

Minimal [Spring Modulith](https://docs.spring.io/spring-modulith/reference/index.html) demo on Spring Boot 4 and Java 21.

## Requirements

- Java 21+
- Network access for Maven dependencies on first build

## Commands

```bash
./mvnw test
./mvnw spring-boot:run
curl http://localhost:8080/api/greetings
curl http://localhost:8080/actuator/health
```

The `greeting` package is an application module verified by `ModularityTest`.
