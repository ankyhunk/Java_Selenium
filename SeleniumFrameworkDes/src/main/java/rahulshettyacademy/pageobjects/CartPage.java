package rahulshettyacademy.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import rahulshettyacademy.abstractcomponents.AbstractComponent;


public class CartPage extends AbstractComponent{
	
	WebDriver driver;
	@FindBy(xpath="//div[@class='cartSection']/h3")
	private List<WebElement> cartProducts;
	
	By checkoutEle = By.cssSelector(".totalRow button");
		
	public CartPage(WebDriver driver) {
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	public Boolean VerifyProductDisplay(String productName) {
		Boolean match=cartProducts.stream().anyMatch(cartProduct->cartProduct.getText().equalsIgnoreCase(productName));
		return match;
	}
	
	public CheckoutPage goToCheckout() {
		ButtonClick(checkoutEle);
		CheckoutPage checkoutpage=new CheckoutPage(driver);
		return checkoutpage;
	}
}
