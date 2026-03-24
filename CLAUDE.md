# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build
./gradlew build

# Run application (http://localhost:8080)
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.test08.domain.order.service.OrderServiceTest"

# Clean build
./gradlew clean build
```

## Architecture

This is a Spring Boot 3.4.3 / Java 21 e-commerce order management application using layered architecture (Controller → Service → Repository → Entity) with an in-memory H2 database.

### Domain Structure

Three business domains under `src/main/java/com/test08/domain/`:

- **product** — CRUD for products (name, price, category, imageUrl)
- **order** — Order lifecycle management; `OrderScheduler` runs daily at 14:00 (cron) to transition all `PENDING` orders to `SHIPPED`
- **orderitem** — Junction between Order and Product (quantity + price snapshot)

Cross-cutting concerns live in `com.test08.global/` (exception handling, config, util).

### Key Business Logic

**Order creation (`OrderService`):** Accepts an `OrderForm` (email, address, postCode, `Map<Integer,Integer>` of productId→quantity). If a `PENDING` order with the same email+address already exists, new items are merged into it (quantities added); otherwise a new order is created. All mutations are `@Transactional`.

**Order status:** `OrderStatus` enum — `PENDING` → `SHIPPED` (triggered by scheduler).

### Database

- H2 in-memory; `ddl-auto: create-drop` (schema recreated on every start)
- H2 console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:test08`)
- SQL logging enabled with formatting

### API Documentation

Springdoc OpenAPI is configured; Swagger UI is available at `http://localhost:8080/swagger-ui.html` after startup.

## Testing Conventions

- Unit tests use `@ExtendWith(MockitoExtension.class)` with Mockito mocks
- Integration/context tests use `@SpringBootTest`
- BDD-style: `given` / `when` / `then` structure with AssertJ assertions
- Test reports: `build/reports/tests/test/index.html`

## Notes

- `GlobalExceptionHandler` and `WebConfig` are currently empty stubs — add implementations there rather than inline
- `@EnableScheduling` is set on the main application class; be careful when adding new `@Scheduled` methods
