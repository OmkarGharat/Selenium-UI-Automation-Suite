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
 * Test Case 2: Login User with correct email and password
 */
public class TC02_LoginUserCorrectTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC02_LoginUserCorrectTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testRegisterAndLogout() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();
			user = RandomUtility.getRandomUserFromExcel();

			// Register the user dynamically
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

			// Log out to prepare for login verification
			homePage.clickLogout();
			waitHelper.waitForPageToLoad();
			signupPage.verifyLoginHeadingVisible();

			log.info("TC02 - User registered and logged out successfully.");
		} catch (Exception e) {
			log.error("TC02 - Registration/Logout failed: ", e);
			Assert.fail("Registration/Logout failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testRegisterAndLogout" })
	public void testLoginAndDeleteAccount() {
		try {
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);

			signupPage.verifyLoginHeadingVisible();
			signupPage.login(user.getEmail(), user.getPassword());
			waitHelper.waitForPageToLoad();

			checkoutPage.verifyLoggedInAsUser();

			// Delete Account
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC02 - Login user with correct credentials test completed.");
		} catch (Exception e) {
			log.error("TC02 - Login/Delete Account failed: ", e);
			Assert.fail("Login/Delete Account failed: " + e.getMessage());
		}
	}
}
