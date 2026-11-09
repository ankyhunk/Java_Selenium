package rsa.pageobjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import rsa.abstractcomponents.AbstractComponent;



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
	
	By countryInput=By.cssSelector("[placeholder='Select Country']");
	By results=By.cssSelector(".ta-results");

	public void selectCountry(String countryName) throws InterruptedException {
		waitForElementToAppear(countryInput);
		// The page sometimes drops text typed while it is still loading, so retry until suggestions show
		for (int attempt=1; ; attempt++) {
			country.clear();
			country.sendKeys(countryName);
			try {
				new WebDriverWait(driver,Duration.ofSeconds(15)).until(ExpectedConditions.visibilityOfElementLocated(results));
				break;
			} catch (TimeoutException e) {
				if (attempt==3) throw e;
			}
		}
		// Pick the suggestion whose text is exactly the country (e.g. "India", not "British Indian Ocean Territory")
		By option=By.xpath("//button[contains(@class,'ta-item')][normalize-space()='"+countryName+"']");
		waitForElementToAppear(option);
		ButtonClick(option);
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
