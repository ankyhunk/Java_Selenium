package rsa.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import rsa.abstractcomponents.AbstractComponent;



public class ProductCatalog extends AbstractComponent{
	WebDriver driver;
	public ProductCatalog(WebDriver driver) {
		// TODO Auto-generated constructor stub
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}
	
	@FindBy(css=".mb-3")
	List<WebElement> products;
	
	By productsBy = By.cssSelector(".mb-3");
	// Searched inside one product card, so it picks that card's "Add To Cart" button
	By addToCart = By.cssSelector(".card-body button:last-of-type");
	By toastMessage=By.cssSelector("#toast-container");
	@FindBy(css=".ng-animating")
	WebElement spinner;
	
	public List<WebElement> getProductList() {
		waitForElementToAppear(productsBy);
		return products;
	}
	
	public WebElement getProductByName(String productName) {
		WebElement prod=getProductList().stream().filter(product -> 
		product.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(productName)).findFirst().orElse(null);
		return prod;
	}
	
	public void addProductToCart(String productName) throws InterruptedException {
		WebElement prod=getProductByName(productName);
		if (prod==null) {
			throw new IllegalArgumentException("Product not found on page: "+productName);
		}
		super.ButtonClick(prod.findElement(addToCart));
		try {
			waitForElementToAppear(toastMessage);
		} catch (TimeoutException e) {
			// The "added to cart" pop-up can vanish before we see it; that's fine
		}
		waitForElementToDisappear(toastMessage);
	}
}
