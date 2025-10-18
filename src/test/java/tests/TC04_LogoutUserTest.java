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
 * Test Case 4: Logout User
 */
public class TC04_LogoutUserTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC04_LogoutUserTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testRegisterAndLogin() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();
			user = RandomUtility.getRandomUserFromExcel();

			// Register
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

			// Logout
			homePage.clickLogout();
			waitHelper.waitForPageToLoad();

			log.info("TC04 - User registered and logged out for test preparation.");
		} catch (Exception e) {
			log.error("TC04 - Setup failed: ", e);
			Assert.fail("Setup failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testRegisterAndLogin" })
	public void testLogoutRedirection() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);

			// Login
			signupPage.login(user.getEmail(), user.getPassword());
			waitHelper.waitForPageToLoad();
			checkoutPage.verifyLoggedInAsUser();

			// Logout and verify redirected to login page
			homePage.clickLogout();
			waitHelper.waitForPageToLoad();
			signupPage.verifyLoginHeadingVisible();

			// Clean up: Login again and delete the account
			signupPage.login(user.getEmail(), user.getPassword());
			waitHelper.waitForPageToLoad();
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC04 - Logout user test completed successfully.");
		} catch (Exception e) {
			log.error("TC04 - Logout verification failed: ", e);
			Assert.fail("Logout verification failed: " + e.getMessage());
		}
	}
}
