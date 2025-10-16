package rahulshettyacademy.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import rahulshettyacademy.abstractcomponents.AbstractComponent;

public class OrderPage extends AbstractComponent{

	WebDriver driver;
	@FindBy(xpath="//tr/td[2]")
	private List<WebElement> product;
	
	@FindBy(css=".totalRow button")
	WebElement checkoutEle;
	
	public OrderPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	public Boolean VerifyOrderDisplay(String productName) {
		Boolean match=product.stream().anyMatch(product->product.getText().equalsIgnoreCase(productName));
		return match;
	}
		
}
