package pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import utility.WaitHelper;

public class CartPage {

	private WebDriver driver;
	private WaitHelper waitHelper;
	private static final Logger log = LoggerFactory.getLogger(CartPage.class);
	private List<Map<String, String>> addedProducts = new ArrayList<>();

	public CartPage(WebDriver driver, WaitHelper waitHelper) {
		this.driver = driver;
		this.waitHelper = waitHelper;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[@href='/products']")
	private WebElement productsLink;

	@FindBy(xpath = "//div[@class='single-products']")
	private List<WebElement> products;

	@FindBy(xpath = "//button[text()='Continue Shopping']")
	private WebElement continueShoppingButton;

	@FindBy(xpath = "//u[normalize-space()='View Cart']")
	private WebElement viewCartButton;

	public void clickProducts() {
		productsLink.click();
	}

	public void addFirstTwoProductsToCart() {
		int count = 0;
		for (WebElement product : products) {
			if (count > 1) break;
			WebElement name = product.findElement(By.tagName("p"));
			WebElement price = product.findElement(By.tagName("h2"));
			WebElement addBtn = product.findElement(By.tagName("a"));

			Map<String, String> data = new HashMap<>();
			data.put("name", name.getText());
			data.put("price", price.getText());
			data.put("quantity", "1");
			addedProducts.add(data);

			addBtn.click();
			count++;
			waitHelper.waitForClickability(continueShoppingButton);
			if (count == 1) {
				continueShoppingButton.click();
			} else {
				viewCartButton.click();
			}
		}
	}

	public List<Map<String, String>> getAddedProducts() {
		return addedProducts;
	}

	@FindBy(xpath = "//tbody/tr")
	private List<WebElement> cartRows;

	public void verifyCartContents() {
		String expectedUrl = "https://www.automationexercise.com/view_cart";
		Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Not on cart page.");
		Assert.assertEquals(addedProducts.size(), cartRows.size(), "Cart count mismatch.");

		for (int i = 0; i < cartRows.size(); i++) {
			Map<String, String> expected = addedProducts.get(i);
			WebElement row = cartRows.get(i);
			String cartName = row.findElement(By.xpath("./td[@class='cart_description']/h4")).getText();
			String cartPrice = row.findElement(By.xpath("./td[@class='cart_price']")).getText();
			String cartQty = row.findElement(By.xpath("./td[@class='cart_quantity']")).getText();

			Assert.assertEquals(cartName, expected.get("name"), "Name mismatch row " + i);
			Assert.assertEquals(cartPrice, expected.get("price"), "Price mismatch row " + i);
			Assert.assertEquals(cartQty, expected.get("quantity"), "Qty mismatch row " + i);
		}
		log.info("Cart contents verified.");
	}

	@FindBy(xpath = "//td[@class='cart_delete']/a")
	private List<WebElement> cartDeleteButtons;

	@FindBy(xpath = "//p[@class='text-center']/b")
	private WebElement emptyCartMessage;

	public void removeAllProducts() {
		for (WebElement btn : cartDeleteButtons) {
			waitHelper.waitForVisibility(btn);
			waitHelper.waitForClickability(btn);
			btn.click();
		}
	}

	public void verifyCartIsEmpty() {
		waitHelper.waitForVisibleText(emptyCartMessage);
		Assert.assertTrue(emptyCartMessage.isDisplayed(), "Empty cart msg not visible.");
		Assert.assertEquals(emptyCartMessage.getText(), "Cart is empty!", "Empty cart msg mismatch.");
		log.info("Cart verified empty.");
	}

	// ===== Checkout & Popup Actions =====

	@FindBy(xpath = "//a[normalize-space()='Proceed To Checkout']")
	private WebElement proceedToCheckoutBtn;

	@FindBy(xpath = "//u[normalize-space()='Register / Login']")
	private WebElement registerLoginPopupLink;

	/**
	 * Click the 'Proceed To Checkout' button.
	 */
	public void clickProceedToCheckout() {
		waitHelper.waitForClickability(proceedToCheckoutBtn);
		proceedToCheckoutBtn.click();
		log.info("Clicked Proceed To Checkout.");
	}

	/**
	 * Click the 'Register / Login' link in the modal popup.
	 */
	public void clickRegisterLoginPopup() {
		waitHelper.waitForClickability(registerLoginPopupLink);
		registerLoginPopupLink.click();
		log.info("Clicked Register / Login link on checkout popup.");
	}

	/**
	 * Verify that a specific product in the cart has the expected quantity.
	 */
	public void verifyProductQuantity(String productName, String expectedQty) {
		waitHelper.waitForPageToLoad();
		for (WebElement row : cartRows) {
			String name = row.findElement(By.xpath("./td[@class='cart_description']/h4")).getText();
			if (name.trim().equalsIgnoreCase(productName.trim())) {
				String qty = row.findElement(By.xpath("./td[@class='cart_quantity']")).getText();
				Assert.assertEquals(qty.trim(), expectedQty.trim(), "Quantity mismatch for product: " + productName);
				log.info("Verified product '{}' has quantity '{}' in cart.", productName, expectedQty);
				return;
			}
		}
		throw new AssertionError("Product not found in cart: " + productName);
	}
}
