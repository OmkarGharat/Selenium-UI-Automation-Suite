package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import models.UserData;
import pages.CheckoutPage;
import pages.HomePage;
import pages.SignupPage;
import utility.RandomUtility;

/**
 * Test Case 5: Register User with existing email
 */
public class TC05_RegisterUserExistingEmailTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC05_RegisterUserExistingEmailTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testRegisterInitialUser() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();
			user = RandomUtility.getRandomUserFromExcel();

			// Register the first time
			signupPage.clickSignupLogin();
			waitHelper.waitForPageToLoad();
			signupPage.completeBasicSignup(user.getName(), user.getEmail());
			waitHelper.waitForPageToLoad();
			signupPage.verifyEnterAccountInfoVisible();

			signupPage.fillAccountDetails(user.getGender(), user.getPassword(),
					user.getDay(), user.getMonth(), user.getYear());
			signupPage.fillAddressDetails(user.getFirstName(), user.getLastName(),
					user.getCompany(), user.getAddress(), user.getCity(),
					user.getState(), user.getZipcode(), user.getMobile());
			signupPage.clickCreateAccount();
			signupPage.verifyAccountCreated();

			// Log out to try duplicate registration
			homePage.clickLogout();
			waitHelper.waitForPageToLoad();

			log.info("TC05 - Initial user registered and logged out.");
		} catch (Exception e) {
			log.error("TC05 - Initial registration failed: ", e);
			Assert.fail("Initial registration failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testRegisterInitialUser" })
	public void testRegisterWithExistingEmail() {
		try {
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);

			// Try to register with same email
			signupPage.completeBasicSignup(user.getName(), user.getEmail());
			signupPage.verifySignupErrorVisible();

			// Clean up: Login and delete the initial account
			signupPage.login(user.getEmail(), user.getPassword());
			waitHelper.waitForPageToLoad();
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC05 - Register with existing email test completed.");
		} catch (Exception e) {
			log.error("TC05 - Duplicate registration verification failed: ", e);
			Assert.fail("Duplicate registration verification failed: " + e.getMessage());
		}
	}
}
