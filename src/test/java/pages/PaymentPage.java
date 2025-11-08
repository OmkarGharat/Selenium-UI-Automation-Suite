package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import utility.WaitHelper;

/**
 * Page Object for the Payment page (https://www.automationexercise.com/payment).
 * Handles payment info submission, order validation, and invoice downloading.
 */
public class PaymentPage {

	private WaitHelper waitHelper;
	private static final Logger log = LoggerFactory.getLogger(PaymentPage.class);

	public PaymentPage(WebDriver driver, WaitHelper waitHelper) {
		this.waitHelper = waitHelper;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@name='name_on_card']")
	private WebElement nameOnCardField;

	@FindBy(xpath = "//input[@name='card_number']")
	private WebElement cardNumberField;

	@FindBy(xpath = "//input[@name='cvc']")
	private WebElement cvcField;

	@FindBy(xpath = "//input[@name='expiry_month']")
	private WebElement expiryMonthField;

	@FindBy(xpath = "//input[@name='expiry_year']")
	private WebElement expiryYearField;

	@FindBy(xpath = "//button[@id='submit']")
	private WebElement payConfirmBtn;

	@FindBy(xpath = "//*[contains(text(),'successfully')]")
	private WebElement orderSuccessText;

	@FindBy(xpath = "//a[normalize-space()='Download Invoice']")
	private WebElement downloadInvoiceBtn;

	/**
	 * Enter payment credentials.
	 */
	public void fillPaymentDetails(String name, String cardNumber, String cvc, String expiryMonth, String expiryYear) {
		waitHelper.waitForVisibility(nameOnCardField);
		nameOnCardField.sendKeys(name);
		cardNumberField.sendKeys(cardNumber);
		cvcField.sendKeys(cvc);
		expiryMonthField.sendKeys(expiryMonth);
		expiryYearField.sendKeys(expiryYear);
		log.info("Payment details filled for: {}", name);
	}

	/**
	 * Click 'Pay and Confirm Order' button.
	 */
	public void clickPayAndConfirm() {
		waitHelper.waitForClickability(payConfirmBtn);
		payConfirmBtn.click();
		log.info("Clicked Pay and Confirm Order button.");
	}

	/**
	 * Verify order is successfully placed.
	 */
	public void verifyOrderPlaced() {
		waitHelper.waitForVisibility(orderSuccessText);
		Assert.assertTrue(orderSuccessText.isDisplayed(), "Order placed success message not visible.");
		log.info("Order success message verified.");
	}

	/**
	 * Click 'Download Invoice' button.
	 */
	public void clickDownloadInvoice() {
		waitHelper.waitForClickability(downloadInvoiceBtn);
		downloadInvoiceBtn.click();
		log.info("Clicked Download Invoice button.");
	}
}
