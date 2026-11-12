# Selenium Framework

## Commands
- Build (main + test code): `mvn -q -f SeleniumFrameworkDes/pom.xml test-compile`
- All tests: `mvn -f SeleniumFrameworkDes/pom.xml clean test`
- One class: `mvn -f SeleniumFrameworkDes/pom.xml test -Dtest=SubmitOrderTest`

## Conventions
- Page Object Model: one class per page in `src/main/java/rsa/pageobjects`, extending `AbstractComponent`
- Use the explicit waits in `AbstractComponent`; never `Thread.sleep`
- Tests extend `BaseTest`, which opens and quits the browser per test
- Test data lives in `rsa/data/*.json`, never hard-coded in tests
- The test login comes from the `TEST_EMAIL` / `TEST_PASSWORD` environment variables (see `TestCredentials`); data files use the placeholders `${TEST_EMAIL}` and `${TEST_PASSWORD}`

## Don't
- Don't add JUnit dependencies (Surefire would then skip the TestNG tests)
- Don't put a real email or password in any file; use the placeholders