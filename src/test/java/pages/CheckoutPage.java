package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import utility.CustomAssert;
import utility.WaitHelper;

public class CheckoutPage {

	private WebDriver driver;
	private WaitHelper waitHelper;
	private static final Logger log = LoggerFactory.getLogger(CheckoutPage.class);

	public CheckoutPage(WebDriver driver, WaitHelper waitHelper) {
		this.driver = driver;
		this.waitHelper = waitHelper;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[text()='Continue']")
	private WebElement continueLink;

	@FindBy(css = "li:nth-child(10) a:nth-child(1)")
	private WebElement loggedInAsUserLink;

	@FindBy(xpath = "//a[normalize-space()='Proceed To Checkout']")
	private WebElement proceedToCheckoutLink;

	@FindBy(xpath = "//h2[normalize-space()='Address Details']")
	private WebElement addressDetailsHeading;

	@FindBy(css = "ul#address_delivery > li:nth-child(4)")
	private WebElement deliveryAddressLine1;

	@FindBy(css = "ul#address_delivery > li:nth-child(6)")
	private WebElement deliveryAddressCityStateZip;

	@FindBy(css = "ul#address_invoice > li")
	private List<WebElement> billingAddressItems;

	@FindBy(xpath = "//a[normalize-space()='Delete Account']")
	private WebElement deleteAccountLink;

	@FindBy(css = "h2[data-qa='account-deleted'] > b")
	private WebElement accountDeletedHeading;

	public void clickContinue() {
		continueLink.click();
	}

	public void verifyLoggedInAsUser() {
		Assert.assertTrue(loggedInAsUserLink.isDisplayed(), "User not logged in.");
		log.info("Verified: Logged in as user.");
	}

	public void verifyCartPageDisplayed() {
		WebElement cart = driver.findElement(By.id("cart_info_table"));
		waitHelper.waitForVisibility(cart);
		Assert.assertTrue(cart.isDisplayed(), "Cart is empty.");
		log.info("Cart page displayed with products.");
	}

	public void clickProceedToCheckout() {
		proceedToCheckoutLink.click();
	}

	public void verifyCheckoutPageVisible() {
		waitHelper.waitForVisibleText(addressDetailsHeading);
		Assert.assertTrue(addressDetailsHeading.isDisplayed(), "Address Details not visible.");
		log.info("Checkout page verified.");
	}

	// ===== Address Verification =====

	public String getDeliveryAddressLine1() {
		return deliveryAddressLine1.getText();
	}

	public String getDeliveryAddressCityStateZip() {
		return deliveryAddressCityStateZip.getText();
	}

	public String getBillingAddressLine1() {
		return billingAddressItems.get(3).getText();
	}

	public String getBillingAddressCityStateZip() {
		return billingAddressItems.get(5).getText();
	}

	public void verifyAddressLine(String expected, String actual) {
		Assert.assertEquals(actual, expected, "Address mismatch.");
		log.info("Address line verified: {}", expected);
	}

	public void verifyCityStateZip(String city, String state, String zipcode, String addressText) {
		List<String> parsed = parseAddress(addressText);
		Assert.assertEquals(parsed.get(0), city, "City mismatch.");
		Assert.assertEquals(parsed.get(1), state, "State mismatch.");
		Assert.assertEquals(parsed.get(2), zipcode, "Zipcode mismatch.");
		log.info("City/State/Zip verified.");
	}

	private List<String> parseAddress(String address) {
		String regex = "(.+?)\\s+([A-Za-z\\s]+?)\\s+(\\d{5,6})";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(address);
		List<String> result = new ArrayList<>();
		if (matcher.find()) {
			result.add(matcher.group(1));
			result.add(matcher.group(2));
			result.add(matcher.group(3));
		} else {
			throw new AssertionError("Could not parse address: " + address);
		}
		return result;
	}

	// ===== Delete Account =====

	public void clickDeleteAccount() {
		deleteAccountLink.click();
	}

	public void verifyAccountDeleted() {
		waitHelper.waitForPageToLoad();
		waitHelper.waitForVisibleText(accountDeletedHeading);
		Assert.assertTrue(accountDeletedHeading.isDisplayed(), "Account deleted msg not visible.");
		CustomAssert.assertEqualsIgnoreCase(accountDeletedHeading.getText(),
				"Account Deleted!", "Account deleted msg mismatch.");
		log.info("Account deleted verified.");
	}

	// ===== Order Comments & Place Order =====

	@FindBy(css = "textarea[name='message']")
	private WebElement commentTextArea;

	@FindBy(css = "a[href='/payment']")
	private WebElement placeOrderBtn;

	/**
	 * Enter a description/comment for the order.
	 */
	public void enterComment(String comment) {
		waitHelper.waitForVisibility(commentTextArea);
		commentTextArea.sendKeys(comment);
		log.info("Entered order comment: {}", comment);
	}

	/**
	 * Click the 'Place Order' button.
	 */
	public void clickPlaceOrder() {
		waitHelper.waitForClickability(placeOrderBtn);
		placeOrderBtn.click();
		log.info("Clicked Place Order.");
	}
}
