package rahulshettyacademy.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import rahulshettyacademy.abstractcomponents.AbstractComponent;



public class CheckoutPage extends AbstractComponent{
	WebDriver driver;
	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);	
	}
	
	By submit=By.cssSelector(".action__submit");
	
	@FindBy(css="[placeholder='Select Country']")
	WebElement country;
	
	By selectCountry= By.xpath("(//button[contains(@class,'ta-item')])[2]");
		
	By results=By.cssSelector(".ta-results");
	
	public void selectCountry(String countryName) throws InterruptedException {
		Actions a=new Actions(driver);
		System.out.println("Cntry:"+countryName);
		Thread.sleep(1000);
		a.sendKeys(country, countryName).build().perform();
		
//		waitForElementToAppear(results);
		ButtonClick(selectCountry);
	}
	
	public ConfirmationPage submitOrder() {
//		JavascriptExecutor jse=(JavascriptExecutor)driver;
//		jse.executeScript("window.scrollBy(0,500)");
		waitForElementToAppear(submit);
//		driver.findElement(submit).click();
		ButtonClick(submit);
		ConfirmationPage confirmpage=new ConfirmationPage(driver);
		return confirmpage;
	}
}
