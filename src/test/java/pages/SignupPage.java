package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import utility.WaitHelper;

/**
 * Page object for the Signup page (https://www.automationexercise.com/signup).
 * Handles the full registration flow: basic signup, account details, and address.
 */
public class SignupPage {

	private WaitHelper waitHelper;
	private static final Logger log = LoggerFactory.getLogger(SignupPage.class);

	public SignupPage(WebDriver driver, WaitHelper waitHelper) {
		this.waitHelper = waitHelper;
		PageFactory.initElements(driver, this);
	}

	// ===== Navigation =====

	@FindBy(xpath = "//a[normalize-space()='Signup / Login']")
	private WebElement signupLoginLink;

	/**
	 * Clicks the 'Signup / Login' link in the navigation bar.
	 */
	public void clickSignupLogin() {
		waitHelper.waitForPageToLoad();
		signupLoginLink.click();
		log.info("Clicked Signup / Login link.");
	}

	// ===== Basic Signup Form =====

	@FindBy(css = "input[placeholder='Name']")
	private WebElement nameField;

	@FindBy(css = "input[data-qa='signup-email']")
	private WebElement emailField;

	@FindBy(xpath = "//button[normalize-space()='Signup']")
	private WebElement signupBtn;

	@FindBy(xpath = "//b[normalize-space()='Enter Account Information']")
	private WebElement enterAccountInfoHeading;

	/**
	 * Fills the name and email, then clicks Signup.
	 */
	public void completeBasicSignup(String name, String email) {
		nameField.sendKeys(name);
		emailField.sendKeys(email);
		signupBtn.click();
		log.info("Basic signup completed for: {}", name);
	}

	/**
	 * Verifies the 'Enter Account Information' heading is shown after basic signup.
	 */
	public void verifyEnterAccountInfoVisible() {
		String text = enterAccountInfoHeading.getText();
		Assert.assertTrue(text.equalsIgnoreCase("Enter Account Information"),
				"Expected 'Enter Account Information' but got: " + text);
		log.info("Enter Account Information page verified.");
	}

	// ===== Account Details =====

	@FindBy(id = "id_gender1")
	private WebElement mrRadioBtn;

	@FindBy(id = "id_gender2")
	private WebElement mrsRadioBtn;

	@FindBy(id = "password")
	private WebElement passwordField;

	@FindBy(id = "days")
	private WebElement dayDropdown;

	@FindBy(id = "months")
	private WebElement monthDropdown;

	@FindBy(id = "years")
	private WebElement yearDropdown;

	/**
	 * Fills account details: gender, password, and date of birth.
	 */
	public void fillAccountDetails(String gender, String password, String day, String month, String year) {
		// Gender
		if (gender.equalsIgnoreCase("Male")) {
			mrRadioBtn.click();
		} else {
			mrsRadioBtn.click();
		}

		// Password
		waitHelper.waitForVisibility(passwordField);
		passwordField.sendKeys(password);

		// Date of birth
		new Select(dayDropdown).selectByValue(day);
		new Select(monthDropdown).selectByValue(month);
		new Select(yearDropdown).selectByValue(year);

		log.info("Account details filled.");
	}

	// ===== Address Details =====

	@FindBy(id = "first_name")
	private WebElement firstNameField;

	@FindBy(id = "last_name")
	private WebElement lastNameField;

	@FindBy(id = "company")
	private WebElement companyField;

	@FindBy(id = "address1")
	private WebElement addressField;

	@FindBy(id = "city")
	private WebElement cityField;

	@FindBy(id = "state")
	private WebElement stateField;

	@FindBy(id = "zipcode")
	private WebElement zipcodeField;

	@FindBy(id = "mobile_number")
	private WebElement mobileNumberField;

	/**
	 * Fills the address section of the registration form.
	 */
	public void fillAddressDetails(String firstName, String lastName, String company,
			String address, String city, String state, String zipcode, String mobile) {
		firstNameField.sendKeys(firstName);
		lastNameField.sendKeys(lastName);
		companyField.sendKeys(company);
		addressField.sendKeys(address);
		cityField.sendKeys(city);
		stateField.sendKeys(state);
		zipcodeField.sendKeys(zipcode);
		mobileNumberField.sendKeys(mobile);
		log.info("Address details filled.");
	}

	// ===== Create Account =====

	@FindBy(xpath = "//button[normalize-space()='Create Account']")
	private WebElement createAccountBtn;

	@FindBy(css = "h2[data-qa='account-created']")
	private WebElement accountCreatedHeading;

	/**
	 * Clicks the 'Create Account' button.
	 */
	public void clickCreateAccount() {
		createAccountBtn.click();
		log.info("Clicked Create Account.");
	}

	/**
	 * Verifies the 'ACCOUNT CREATED!' confirmation message.
	 */
	public void verifyAccountCreated() {
		String text = accountCreatedHeading.getText();
		Assert.assertTrue(text.equalsIgnoreCase("Account Created!"),
				"Expected 'Account Created!' but got: " + text);
		log.info("Account created successfully.");
	}

	// ===== Login Form & Error Validations =====

	@FindBy(css = "input[data-qa='login-email']")
	private WebElement loginEmailField;

	@FindBy(css = "input[data-qa='login-password']")
	private WebElement loginPasswordField;

	@FindBy(css = "button[data-qa='login-button']")
	private WebElement loginBtn;

	@FindBy(xpath = "//h2[normalize-space()='Login to your account']")
	private WebElement loginHeading;

	@FindBy(xpath = "//p[contains(text(),'Your email or password is incorrect!')]")
	private WebElement loginErrorMsg;

	@FindBy(xpath = "//p[contains(text(),'Email Address already exist!')]")
	private WebElement signupErrorMsg;

	/**
	 * Log in with the specified email and password.
	 */
	public void login(String email, String password) {
		loginEmailField.sendKeys(email);
		loginPasswordField.sendKeys(password);
		loginBtn.click();
		log.info("Attempted login with email: {}", email);
	}

	/**
	 * Verify that the 'Login to your account' header is visible.
	 */
	public void verifyLoginHeadingVisible() {
		Assert.assertTrue(loginHeading.isDisplayed(), "Login to your account header is not visible.");
		log.info("Login heading verified visible.");
	}

	/**
	 * Verify that the login incorrect error message is displayed.
	 */
	public void verifyLoginErrorVisible() {
		Assert.assertTrue(loginErrorMsg.isDisplayed(), "Login incorrect error message is not visible.");
		log.info("Login error message verified.");
	}

	/**
	 * Verify that the signup existing email error message is displayed.
	 */
	public void verifySignupErrorVisible() {
		Assert.assertTrue(signupErrorMsg.isDisplayed(), "Email Address already exist error message is not visible.");
		log.info("Signup error message verified.");
	}
}
