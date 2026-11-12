---
name: new-page-object
description: Create a new Page Object and a matching TestNG test in this framework's style. Use when adding a page to the framework.
---
1. Create `src/main/java/rsa/pageobjects/<Name>.java` extending `AbstractComponent`,
   with `@FindBy` locators and `PageFactory.initElements` in the constructor.
2. Add the navigation method on the page that leads to it.
3. Create a test in `src/test/java/rsa/tests/` extending `BaseTest`. Put its test data in a
   JSON file in `src/test/java/rsa/data/` and feed it through a `@DataProvider` that calls
   `getJsonDataToMap`. Log in with `${TEST_EMAIL}` / `${TEST_PASSWORD}` in the data, or
   `TestCredentials.email()` / `TestCredentials.password()` in code, never a real login.
4. Run `mvn -q -f SeleniumFrameworkDes/pom.xml test-compile`, then the new test with
   `mvn -f SeleniumFrameworkDes/pom.xml test -Dtest=<TestClass>` (needs `TEST_EMAIL` and
   `TEST_PASSWORD` set). If the live site isn't reachable, stop after the build and say so.
   Fix anything that fails.