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

- `.github/workflows/ci.yml` (GitHub Actions)
- `Jenkinsfile` (Jenkins)

Both just run `mvn test` and save the reports.

## Assumptions

- FakeStoreAPI doesn't really save anything, so create/update/delete tests just check the response, not real persistence.
- `DatabaseValidationTest` uses a fake in-memory DB (`OrderDatabase`) since there's no real DB to connect to. It's there to show how DB validation would be written.
- No login token is used in other tests - the API doesn't need one.
- Test data (user id, product id, etc.) is hardcoded since the fake API's data doesn't change.
- No UI/Selenium tests, this is API only.
