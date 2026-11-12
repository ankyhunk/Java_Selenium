package rsa.stepDefinitions;

import rsa.pageobjects.CartPage;
import rsa.pageobjects.CheckoutPage;
import rsa.pageobjects.ConfirmationPage;
import rsa.pageobjects.LandingPage;
import rsa.pageobjects.ProductCatalog;
import rsa.testcomponents.BaseTest;
import rsa.testcomponents.TestCredentials;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.AssertJUnit;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitionImpl extends BaseTest {
	public LandingPage landingPage;
	public ProductCatalog productCatalog;
	public ConfirmationPage confirmationPage;
	@Given("I landed on Ecommerce Page")
	public void I_landed_on_Ecommerce_Page() throws IOException {
		landingPage = launchApplication();
	}
	
	@Given("^Logged in with username (.+) and password (.+)$")
	public void logged_in_username_and_password(String username,String password) {
		// ${TEST_EMAIL} / ${TEST_PASSWORD} in the feature file come from the environment
		productCatalog=landingPage.loginApplication(TestCredentials.resolve(username),TestCredentials.resolve(password));
	}
	
	@When("^I add product (.+) to Cart$")
	public void I_add_product_to_Cart(String productName) throws InterruptedException {
		List<WebElement>products=productCatalog.getProductList();
		productCatalog.addProductToCart(productName);
	}
	
	@When("^Checkout (.+) and submit the order$")
	public void Checkout_and_submit_the_order(String productName) throws InterruptedException {
		CartPage cartPage=productCatalog.goToCartPage();
		
		Boolean match =cartPage.VerifyProductDisplay(productName);
		Assert.assertTrue(match);
		CheckoutPage checkoutPage=cartPage.goToCheckout();
		checkoutPage.selectCountry("India");
		confirmationPage=checkoutPage.submitOrder();
	}
	
	@Then("{string} message is displayed on ConfirmationPage")
	public void message_displayed_ConfirmationPage(String string) {
		String confirmMsg = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(confirmMsg.equalsIgnoreCase(string));
	}

	@Then("{string} message is displayed")
	public void message_is_displayed(String expectedMessage) {
		Assert.assertEquals(expectedMessage,landingPage.getErrorMessage());
	}

	// Runs after every scenario, passed or failed, so no browser is left open
	@After
	public void closeBrowser() {
		if (driver!=null) {
			driver.quit();
		}
	}
}
