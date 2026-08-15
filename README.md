# Order Management System - API Tests

API test automation for order management, using RestAssured + TestNG.

Tests hit https://fakestoreapi.com (public fake API) for auth, products and orders.

## What's in here

- `src/main/java/org/example/pages` - API clients (login, product, order)
- `src/main/java/org/example/models` - request/response objects
- `src/main/java/org/example/db` - simple in-memory "database" used to fake DB checks
- `src/test/java/org/example/tests` - the actual tests
- `testng.xml` - test suite config, runs everything in parallel

## Setup

- Java 17
- Maven

```
git clone <repo-url>
cd order_management_system
mvn clean compile
```

## Running tests

Run everything:

```
mvn test
```

Run one class:

```
mvn test -Dtest=OrderApiTest
```

Reports go to `target/surefire-reports`.

Tests run in parallel (4 threads, one per class) - see `testng.xml`.

## CI

Added two sample pipelines just to show how this would run in CI:

- `Jenkinsfile` (Jenkins)

 just run `mvn test` and save the reports.



## Results
<img width="1599" height="899" alt="WhatsApp Image 2026-08-14 at 09 52 48" src="https://github.com/user-attachments/assets/5f27df68-f0b2-4450-8c5b-12ff8c003e60" />
