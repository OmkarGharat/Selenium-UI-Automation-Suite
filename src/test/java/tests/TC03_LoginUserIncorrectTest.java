package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.SignupPage;

/**
 * Test Case 3: Login User with incorrect email and password
 */
public class TC03_LoginUserIncorrectTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC03_LoginUserIncorrectTest.class);

	@Test
	public void testLoginWithIncorrectCredentials() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			signupPage.clickSignupLogin();
			waitHelper.waitForPageToLoad();
			signupPage.verifyLoginHeadingVisible();

			// Login with incorrect credentials
			signupPage.login("incorrect_user_123456@gmail.com", "invalidPassword");
			signupPage.verifyLoginErrorVisible();

			log.info("TC03 - Login user with incorrect credentials test completed.");
		} catch (Exception e) {
			log.error("TC03 - Login incorrect credentials failed: ", e);
			Assert.fail("Login incorrect credentials failed: " + e.getMessage());
		}
	}
}
