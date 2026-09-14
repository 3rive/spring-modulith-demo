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

curl -s -X POST http://localhost:8080/api/payments \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"ord-1","amountMinor":1999,"currency":"USD","description":"Widget"}'

curl -s http://localhost:8080/api/payments
```

Application modules (verified by `ModularityTest`):

- `greeting` — sample HTTP greeting
- `payment` — initiate, complete, and fail payments; publishes `PaymentCompleted` / `PaymentFailed` for other modules to listen to via `PaymentApi` and application events

Amounts are stored in minor units (for example `1999` is $19.99). Completing or failing a payment is an explicit application action so card data never touches this service.
