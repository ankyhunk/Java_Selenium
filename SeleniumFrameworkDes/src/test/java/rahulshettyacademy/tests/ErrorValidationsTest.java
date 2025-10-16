package rahulshettyacademy.tests;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import rahulshettyacademy.pageobjects.CartPage;
import rahulshettyacademy.pageobjects.CheckoutPage;
import rahulshettyacademy.pageobjects.ConfirmationPage;
import rahulshettyacademy.pageobjects.LandingPage;
import rahulshettyacademy.pageobjects.ProductCatalog;
import rahulshettyacademy.testcomponents.BaseTest;
import rahulshettyacademy.testcomponents.Retry;

public class ErrorValidationsTest extends BaseTest{

	@Test(groups={"Error Handling"},retryAnalyzer=Retry.class)
//		WebDriverManager.chromedriver().setup();
	public void LoginErrorValidation() throws IOException, InterruptedException{
		//String productName="ZARA COAT 3";
		
		landingPage.loginApplication("ankyhunk@gmail.com","Ankur");
		Assert.assertEquals("Incorrect email or password.", landingPage.getErrorMessage());
	}
	@Test
	public void ProductErrorValidation() throws IOException, InterruptedException{
		String productName="ZARA COAT 3";
		
		ProductCatalog productCatalog=landingPage.loginApplication("ankyhunk@gmail.com","Ank123ur");
		
		List<WebElement>products=productCatalog.getProductList();
		productCatalog.addProductToCart(productName);
		CartPage cartPage=productCatalog.goToCartPage();
		
		Boolean match =cartPage.VerifyProductDisplay("ZARA COAT 33");
		Assert.assertFalse(match);
	}
	
}
