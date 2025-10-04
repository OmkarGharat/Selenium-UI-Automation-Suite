package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import models.UserData;
import pages.SignupPage;
import utility.RandomUtility;

/**
 * Test Case 1: Register User
 * Verifies end-to-end user registration on automationexercise.com.
 */
public class TC01_RegisterUserTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC01_RegisterUserTest.class);

	@Test(priority = 1)
	public void testBasicSignup() {
		try {
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			UserData user = RandomUtility.getRandomUserFromExcel();

			signupPage.clickSignupLogin();
			waitHelper.waitForPageToLoad();
			signupPage.completeBasicSignup(user.getName(), user.getEmail());
			waitHelper.waitForPageToLoad();
			signupPage.verifyEnterAccountInfoVisible();

			log.info("TC01 - Basic signup completed successfully.");
		} catch (Exception e) {
			log.error("TC01 - Basic signup failed: ", e);
			Assert.fail("Basic signup failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testBasicSignup" })
	public void testFullRegistration() {
		try {
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			UserData user = RandomUtility.getRandomUserFromExcel();

			waitHelper.waitForPageToLoad();

			signupPage.fillAccountDetails(user.getGender(), user.getPassword(),
					user.getDay(), user.getMonth(), user.getYear());

			signupPage.fillAddressDetails(user.getFirstName(), user.getLastName(),
					user.getCompany(), user.getAddress(), user.getCity(),
					user.getState(), user.getZipcode(), user.getMobile());

			signupPage.clickCreateAccount();
			signupPage.verifyAccountCreated();

			log.info("TC01 - Full registration completed successfully.");
		} catch (Exception e) {
			log.error("TC01 - Registration failed: ", e);
			Assert.fail("Registration failed: " + e.getMessage());
		}
	}
}
