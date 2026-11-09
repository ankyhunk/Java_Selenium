package rsa.tests;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.TakesScreenshot;

import io.github.bonigarcia.wdm.WebDriverManager;
import rsa.pageobjects.*;
import rsa.testcomponents.BaseTest;

public class SubmitOrderTest extends BaseTest{
	String productName="ZARA COAT 3";
	
//		WebDriverManager.chromedriver().setup();
	@Test(dataProvider="getData",groups={"Purchase"})
	public void submitOrder(HashMap<String,String> input) throws IOException, InterruptedException{
		
		// Browser is already opened by @BeforeMethod launchApplication() in BaseTest
		ProductCatalog productCatalog=landingPage.loginApplication(input.get("email"),input.get("password"));
				
		List<WebElement>products=productCatalog.getProductList();
		productCatalog.addProductToCart(input.get("product"));
		CartPage cartPage=productCatalog.goToCartPage();
		
		Boolean match =cartPage.VerifyProductDisplay(input.get("product"));
		Assert.assertTrue(match);
		CheckoutPage checkoutPage=cartPage.goToCheckout();
		checkoutPage.selectCountry("India");
		ConfirmationPage confirmationPage=checkoutPage.submitOrder();
		
		String confirmMsg = confirmationPage.getConfirmationMessage();
		AssertJUnit.assertTrue(confirmMsg.equalsIgnoreCase("Thankyou for the order."));
//		Thread.sleep(2000);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='ng-star-inserted']")));
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating")));
		
		
//		List<WebElement> cartproducts=driver.findElements(By.xpath("//div[@class='cartSection']/h3"));
//		Boolean match=cartproducts.stream().anyMatch(cartproduct -> cartproduct.getText().equalsIgnoreCase(productName));
//		Assert.assertTrue(match);
//		WebElement chkOut=driver.findElement(By.xpath("//button[text()='Checkout']"));
//		JavascriptExecutor jse=(JavascriptExecutor)driver; 
//		jse.executeScript("arguments[0].click();",chkOut);
//		
//		//driver.findElement(By.xpath("//input[@placeholder='Select Country']")).sendKeys("India");
//		Actions act=new Actions(driver);
//		act.sendKeys(driver.findElement(By.xpath("//input[@placeholder='Select Country']")),"India").build().perform();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[contains(@class,'ta-results')]")));
//		WebElement ddInd=driver.findElement(By.xpath("(//button[contains(@class,'ng-star-inserted')])[2]"));
//		jse.executeScript("arguments[0].click();",ddInd);
//		WebElement placeOrder=driver.findElement(By.xpath("//a[contains(@class,'submit ')]"));
//		jse.executeScript("arguments[0].click();",placeOrder);
//		
//		String confirmMsg = driver.findElement(By.xpath("//h1[@class='hero-primary']")).getText();
//		Assert.assertTrue(confirmMsg.equalsIgnoreCase("Thankyou for the order."));
		
	}
	
	// Runs after submitOrder; ankyhunk@gmail.com orders ADIDAS ORIGINAL in PurchaseOrder.json
	@Test(dependsOnMethods={"submitOrder"})
	public void OrderHistoryTest() {
		ProductCatalog productCatalog=landingPage.loginApplication("ankyhunk@gmail.com","Ank123ur");
		OrderPage ordersPage=productCatalog.goToOrdersPage();
		Assert.assertTrue(ordersPage.VerifyOrderDisplay("ADIDAS ORIGINAL"));
	}
	
	@DataProvider
	public Object[][] getData() throws IOException{
//		HashMap<String,String>map = new HashMap<String,String>();
//		map.put("email","anshika@gmail.com");
//		map.put("password", "Iamking@000");
//		map.put("product", "ZARA COAT 3");
//		HashMap<String,String>map1 = new HashMap<String,String>();
//		map1.put("email","rahulshetty@gmail.com");
//		map1.put("password", "Iamking@000");
//		map1.put("product", "ADIDAS ORIGINAL");
		List<HashMap<String,String>> data= getJsonDataToMap(System.getProperty("user.dir")+"\\src\\test\\java\\rsa\\data\\PurchaseOrder.json");
		return new Object[][] {{data.get(0)},{data.get(1)}};
	}

}
