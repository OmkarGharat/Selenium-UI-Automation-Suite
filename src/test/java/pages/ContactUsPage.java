package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Page object for the Contact Us page (https://www.automationexercise.com/contact_us).
 */
public class ContactUsPage {

	private static final Logger log = LoggerFactory.getLogger(ContactUsPage.class);

	public ContactUsPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// ===== Navigation =====

	@FindBy(xpath = "//a[normalize-space()='Contact us']")
	private WebElement contactUsLink;

	public void clickContactUs() {
		contactUsLink.click();
		log.info("Clicked Contact Us link.");
	}

	// ===== Form Fields =====

	@FindBy(css = "input[placeholder='Name']")
	private WebElement nameField;

	@FindBy(css = "input[placeholder='Email']")
	private WebElement emailField;

	@FindBy(css = "input[placeholder='Subject']")
	private WebElement subjectField;

	@FindBy(id = "message")
	private WebElement messageField;

	@FindBy(css = "input[name='upload_file']")
	private WebElement fileInput;

	@FindBy(css = "input[name='submit']")
	private WebElement submitButton;

	@FindBy(css = "div.status.alert.alert-success")
	private WebElement successMessage;

	public void enterName(String name) {
		nameField.sendKeys(name);
	}

	public void enterEmail(String email) {
		emailField.sendKeys(email);
	}

	public void enterSubject(String subject) {
		subjectField.sendKeys(subject);
	}

	public void enterMessage(String message) {
		messageField.sendKeys(message);
	}

	/**
	 * Uploads a file using the file input element.
	 * @param absoluteFilePath absolute path to the file to upload
	 */
	public void uploadFile(String absoluteFilePath) {
		fileInput.sendKeys(absoluteFilePath);
		log.info("File uploaded: {}", absoluteFilePath);
	}

	public void clickSubmit() {
		submitButton.click();
		log.info("Clicked Submit button.");
	}

	/**
	 * Verifies the success message after form submission.
	 */
	public void verifySuccessMessage() {
		Assert.assertTrue(successMessage.isDisplayed(), "Success message not visible.");
		Assert.assertEquals(successMessage.getText().trim(),
				"Success! Your details have been submitted successfully.",
				"Success message text mismatch.");
		log.info("Contact Us success message verified.");
	}
}
