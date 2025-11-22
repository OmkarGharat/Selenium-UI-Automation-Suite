package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import utility.WaitHelper;

/**
 * Page object for the AutomationExercise home page.
 * Handles home page verification, footer/subscription, and common navigation.
 */
public class HomePage {

	private WebDriver driver;
	private WaitHelper waitHelper;
	private static final Logger log = LoggerFactory.getLogger(HomePage.class);

	public HomePage(WebDriver driver, WaitHelper waitHelper) {
		this.driver = driver;
		this.waitHelper = waitHelper;
		PageFactory.initElements(driver, this);
	}

	// ===== Home Page Verification =====

	/**
	 * Verifies the home page has loaded by checking the active carousel slide.
	 */
	public void verifyHomePageLoaded() {
		var activeSlideWait = waitHelper.waitForSeconds(30);
		var activeSlide = activeSlideWait.until(
				d -> d.findElement(By.cssSelector("div.item.active")));

		String slideText = activeSlide.findElement(By.cssSelector("h1"))
				.getText().replaceAll("\\s", "");

		Assert.assertEquals(slideText, "AutomationExercise", "Active slide text mismatch");
		log.info("Home page loaded successfully.");
	}

	// ===== Footer / Subscription =====

	@FindBy(id = "footer")
	private WebElement footerSection;

	@FindBy(css = "div.single-widget > h2")
	private WebElement subscriptionLabel;

	@FindBy(id = "susbscribe_email")
	private WebElement subscriptionEmailField;

	@FindBy(id = "subscribe")
	private WebElement subscribeBtn;

	@FindBy(id = "success-subscribe")
	private WebElement successSubscribeMessage;

	/**
	 * Scrolls the page down to the footer section.
	 */
	public void scrollToFooter() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", footerSection);
		log.info("Scrolled to footer section.");
	}

	/**
	 * Verifies the "SUBSCRIPTION" label is visible in the footer.
	 */
	public void verifySubscriptionLabel() {
		Assert.assertEquals(subscriptionLabel.getText(), "SUBSCRIPTION", "Subscription text mismatch.");
		log.info("Subscription label verified.");
	}

	/**
	 * Enters an email address into the subscription field.
	 */
	public void enterSubscriptionEmail(String email) {
		subscriptionEmailField.sendKeys(email);
	}

	/**
	 * Clicks the subscribe button.
	 */
	public void clickSubscribe() {
		subscribeBtn.click();
		log.info("Clicked subscribe button.");
	}

	/**
	 * Verifies the success message after subscribing.
	 */
	public void verifySubscriptionSuccess() {
		Assert.assertTrue(successSubscribeMessage.isDisplayed(), "Success subscribe message is not visible.");
		log.info("Subscription success message verified.");
	}

	// ===== Navigation Header Links =====

	@FindBy(css = "a[href='/test_cases']")
	private WebElement testCasesLink;

	@FindBy(css = "a[href='/logout']")
	private WebElement logoutLink;

	/**
	 * Click the 'Test Cases' link in the navigation header.
	 */
	public void clickTestCases() {
		waitHelper.waitForClickability(testCasesLink);
		testCasesLink.click();
		log.info("Clicked Test Cases link.");
	}

	/**
	 * Click the 'Logout' link in the navigation header.
	 */
	public void clickLogout() {
		waitHelper.waitForClickability(logoutLink);
		logoutLink.click();
		log.info("Clicked Logout link.");
	}

	// ===== Recommended Items =====

	@FindBy(xpath = "//h2[normalize-space()='recommended items']")
	private WebElement recommendedItemsHeading;

	@FindBy(css = "div#recommended-item-carousel div.item.active p")
	private WebElement activeRecommendedProductName;

	@FindBy(css = "div#recommended-item-carousel div.item.active a.add-to-cart")
	private WebElement activeRecommendedAddToCartBtn;

	/**
	 * Scrolls to the Recommended Items section.
	 */
	public void scrollToRecommendedItems() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", recommendedItemsHeading);
		log.info("Scrolled to Recommended Items section.");
	}

	/**
	 * Verify recommended items heading is visible.
	 */
	public void verifyRecommendedItemsVisible() {
		Assert.assertTrue(recommendedItemsHeading.isDisplayed(), "Recommended Items heading is not visible.");
		log.info("Recommended Items heading verified visible.");
	}

	/**
	 * Adds the active recommended product to the cart and returns its name.
	 */
	public String addRecommendedProductToCart() {
		String productName = activeRecommendedProductName.getText();
		waitHelper.waitForClickability(activeRecommendedAddToCartBtn);
		activeRecommendedAddToCartBtn.click();
		log.info("Added recommended product to cart: {}", productName);
		return productName;
	}

	// ===== Scroll Functionality =====

	@FindBy(id = "scrollUp")
	private WebElement scrollUpArrow;

	@FindBy(xpath = "//h2[contains(text(),'Full-Fledged practice website')]")
	private WebElement headerText;

	/**
	 * Click the scroll up arrow.
	 */
	public void clickScrollUpArrow() {
		waitHelper.waitForClickability(scrollUpArrow);
		scrollUpArrow.click();
		log.info("Clicked Scroll Up arrow.");
	}

	/**
	 * Scroll manually to the top of the page.
	 */
	public void scrollToTop() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");
		log.info("Scrolled to top of the page manually.");
	}

	/**
	 * Verify that the top header text is visible on the screen.
	 */
	public void verifyHeaderTextVisible() {
		waitHelper.waitForVisibility(headerText);
		Assert.assertTrue(headerText.isDisplayed(), "Header text is not visible on the screen.");
		log.info("Header text verified visible.");
	}
}
