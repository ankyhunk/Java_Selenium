# Selenium Framework

## Commands
- Build: `mvn -q -f SeleniumFrameworkDes/pom.xml compile`
- All tests: `mvn -f SeleniumFrameworkDes/pom.xml clean test`
- One class: `mvn -f SeleniumFrameworkDes/pom.xml test -Dtest=SubmitOrderTest`

## Conventions
- Page Object Model: one class per page in `src/main/java/rsa/pageobjects`, extending `AbstractComponent`
- Use the explicit waits in `AbstractComponent`; never `Thread.sleep`
- Tests extend `BaseTest`, which opens and quits the browser per test
- Test data lives in `rsa/data/*.json`, never hard-coded in tests

## Don't
- Don't add JUnit dependencies (Surefire would then skip the TestNG tests)
- Don't commit real credentials in PurchaseOrder.json