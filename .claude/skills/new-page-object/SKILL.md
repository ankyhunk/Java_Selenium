---
name: new-page-object
description: Create a new Page Object and a matching TestNG test in this framework's style. Use when adding a page to the framework.
---
1. Create `src/main/java/rsa/pageobjects/<Name>.java` extending `AbstractComponent`,
   with `@FindBy` locators and `PageFactory.initElements` in the constructor.
2. Add the navigation method on the page that leads to it.
3. Create a test in `src/test/java/rsa/tests/` extending `BaseTest`.
4. Run the build and the new test; fix anything that fails.