package rsa.abstractcomponents;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import rsa.pageobjects.CartPage;
import rsa.pageobjects.OrderPage;

public class AbstractComponent {
	WebDriver driver;
	
	public AbstractComponent(WebDriver driver) {
		this.driver=driver;
	}
	@FindBy(xpath="//button[contains(@routerlink,'cart')]")
	WebElement cartHeader;
	
	@FindBy(xpath="//button[contains(@routerlink,'myorders')]")
	WebElement orderHeader;
	public void waitForElementToAppear(By findBy) {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
	}
	public void waitForElementToDisappear(WebElement ele) {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOf(ele));
	}
	public void waitForElementToDisappear(By findBy) {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(findBy));
	}
	public CartPage goToCartPage() {
		ButtonClick(cartHeader);
		// Make sure the cart page actually opened before reading it
		new WebDriverWait(driver,Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("cart"));
		waitForElementToAppear(By.cssSelector(".cartSection h3"));
		CartPage cartpage=new CartPage(driver);
		return cartpage;
	}
	public OrderPage goToOrdersPage() {
		ButtonClick(orderHeader);
		new WebDriverWait(driver,Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("myorders"));
		waitForElementToAppear(By.xpath("//tr/td[2]"));
		OrderPage orderpage=new OrderPage(driver);
		return orderpage;
	}
	public void ButtonClick(By button) {
		ButtonClick(driver.findElement(button));
	}
	public void ButtonClick(WebElement button) {
		JavascriptExecutor jse=(JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click();",button);
	}
}
