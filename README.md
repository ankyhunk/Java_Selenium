# Selenium Framework

A Java test automation framework for an e-commerce web app, built with **Selenium WebDriver**, **TestNG** and **Cucumber**, using the **Page Object Model**.

The tests run against the store at <https://rahulshettyacademy.com/client/>: they log in, add a product to the cart, check out and verify the order, plus negative tests for wrong logins and products.

## Tech stack

| Tool | Version | Used for |
|---|---|---|
| Java | 17+ | Language |
| Maven | 3.9+ | Build and dependencies |
| Selenium WebDriver | 4.36.0 | Browser automation |
| TestNG | 7.11.0 | Test runner, data providers, groups, retries |
| Cucumber | 7.30.0 | BDD feature files (Gherkin) |
| Extent Reports | 5.1.2 | HTML test report with screenshots |
| Jackson | 3.0.0 | Reading JSON test data |

## Project structure

```
SeleniumFrameworkDes/
├── pom.xml
├── TestSuite/
│   ├── testng.xml            # Full suite (all tests + Extent listener)
│   ├── Purchase.xml          # Only tests in the "Purchase" group
│   └── ErrorValid.xml        # Only tests in the "Error Handling" group
└── src/
    ├── main/java/rsa/
    │   ├── abstractcomponents/
    │   │   └── AbstractComponent.java   # Shared waits, header navigation, JS clicks
    │   ├── pageobjects/                 # One class per page
    │   │   ├── LandingPage.java
    │   │   ├── ProductCatalog.java
    │   │   ├── CartPage.java
    │   │   ├── CheckoutPage.java
    │   │   ├── ConfirmationPage.java
    │   │   └── OrderPage.java
    │   └── resources/
    │       ├── GlobalData.properties    # browser=chrome
    │       └── ExtentReporterNG.java    # Extent report setup
    └── test/java/
        ├── rsa/tests/
        │   ├── SubmitOrderTest.java       # Place an order, then check order history
        │   └── ErrorValidationsTest.java  # Wrong login / wrong product checks
        ├── rsa/testcomponents/
        │   ├── BaseTest.java     # Opens and quits the browser for each test
        │   ├── Listeners.java    # Extent report + screenshot on failure
        │   └── Retry.java        # Retries a flaky test once
        ├── rsa/data/
        │   ├── DataReader.java
        │   └── PurchaseOrder.json   # Test data (login + product per row)
        ├── rsa/stepDefinitions/
        │   └── StepDefinitionImpl.java
        └── cucumber/
            ├── SubmitOrder.feature
            ├── ErrorValidation.feature
            └── TestNGTestRunner.java
```

## Prerequisites

- JDK 17 or newer
- Maven 3.9 or newer
- Google Chrome (Selenium Manager downloads the matching ChromeDriver automatically)

## Running the tests

Set your test login first (see [Test data](#test-data)), then clone the repository and go to the project folder:

```bash
git clone https://github.com/ankyhunk/Selenium_Framework.git
cd Selenium_Framework/SeleniumFrameworkDes
```

Run the full TestNG suite (`TestSuite/testng.xml`):

```bash
mvn clean test
```

Run a single test class:

```bash
mvn test -Dtest=SubmitOrderTest
```

Run only one group (`Purchase.xml` or `ErrorValid.xml`): right-click the file in Eclipse, then **Run As → TestNG Suite**. `pom.xml` always points Maven at `testng.xml`.

Run the Cucumber scenarios:

```bash
mvn test -Dtest=TestNGTestRunner
```
### In Eclipse

1. **File → Import → Maven → Existing Maven Projects**, then choose the `SeleniumFrameworkDes` folder.
2. Right-click the project, then **Maven → Update Project**.
3. Right-click `TestSuite/testng.xml`, then **Run As → TestNG Suite** (needs the TestNG plugin from the Eclipse Marketplace).

## Test data

The tests log in to the store with your own account, which is never stored in the repository. Create a free account at <https://rahulshettyacademy.com/client/>, then set two environment variables before running the tests:

```bash
export TEST_EMAIL="you@example.com"
export TEST_PASSWORD="your-password"
```

On Windows (PowerShell), for the current session:

```powershell
$env:TEST_EMAIL = "you@example.com"; $env:TEST_PASSWORD = "your-password"
```

Or pass them to Maven: `mvn clean test -Dtest.email=you@example.com -Dtest.password=your-password`. To run from Eclipse, set them as Windows user environment variables and restart Eclipse.

`src/test/java/rsa/data/PurchaseOrder.json` holds one row per order to place. The login fields are placeholders that `TestCredentials` fills in:

```json
[
  { "email": "${TEST_EMAIL}", "password": "${TEST_PASSWORD}", "product": "ZARA COAT 3" }
]
```

`submitOrder` runs once for each row through a TestNG data provider. The Cucumber feature files use the same placeholders.

## Claude Code setup

This repo is configured for [Claude Code](https://docs.claude.com/en/docs/claude-code/overview), so it follows the framework's conventions out of the box:

| File | Purpose |
|---|---|
| `CLAUDE.md` | Build/test commands, Page Object conventions and known pitfalls |
| `.claude/settings.json` | Permissions (no access to credentials) and a hook that compiles after every edit |
| `.claude/skills/new-page-object/` | `/new-page-object` skill: adds a Page Object and a matching test |
| `.claude/agents/test-reviewer.md` | Read-only subagent that reviews tests for flakiness and missing cleanup |
| `.github/workflows/claude.yml` | Comment `@claude` on an issue or PR to have Claude do the work in CI |

See [docs/claude-code-setup.md](docs/claude-code-setup.md) for how each piece works and why it's designed that way.

## Reports

| Report | Location |
|---|---|
| Extent report (with failure screenshots) | `reports/index.html` |
| TestNG report | `target/surefire-reports/index.html` |
| Cucumber report | `target/Cucumber.html` |

