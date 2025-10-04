package tests;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import models.UserData;
import pages.ContactUsPage;
import utility.RandomUtility;

/**
 * Test Case 6: Contact Us Form
 * Verifies the Contact Us form submission and success message.
 */
public class TC06_ContactUsTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC06_ContactUsTest.class);

	@Test
	public void testContactUsForm() {
		try {
			ContactUsPage contactUs = new ContactUsPage(driver);
			UserData user = RandomUtility.getRandomUserFromExcel();

			contactUs.clickContactUs();
			waitHelper.waitForPageToLoad();

			contactUs.enterName(user.getName());
			contactUs.enterEmail(user.getEmail());
			contactUs.enterSubject("Feedback");
			contactUs.enterMessage("I just love your service.");

			String filePath = new File("src/test/resources/picture.jpeg").getAbsolutePath();
			contactUs.uploadFile(filePath);

			contactUs.clickSubmit();

			// Handle JavaScript alert
			driver.switchTo().alert().accept();

			contactUs.verifySuccessMessage();

			log.info("TC06 - Contact Us form submitted successfully.");
		} catch (Exception e) {
			log.error("TC06 - Contact Us test failed: ", e);
			Assert.fail("Contact Us test failed: " + e.getMessage());
		}
	}
}
