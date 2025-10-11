package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import models.UserData;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.SignupPage;
import utility.RandomUtility;

/**
 * Test Case 23: Verify address details in checkout page
 * Registers a user, adds products to cart, then verifies delivery
 * and billing addresses match the registration data.
 */
public class TC23_VerifyAddressInCheckoutTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC23_VerifyAddressInCheckoutTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testRegistration() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);

			waitHelper.waitForPageToLoad();
			homePage.verifyHomePageLoaded();

			user = RandomUtility.getRandomUserFromExcel();

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

			log.info("TC23 - Registration completed.");
		} catch (Exception e) {
			log.error("TC23 - Registration failed: ", e);
			Assert.fail("Registration failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testRegistration" })
	public void testAddToCart() {
		try {
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);

			checkoutPage.clickContinue();
			waitHelper.waitForPageToLoad();
			checkoutPage.verifyLoggedInAsUser();

			cartPage.clickProducts();
			waitHelper.waitForPageToLoad();
			cartPage.addFirstTwoProductsToCart();

			checkoutPage.verifyCartPageDisplayed();

			log.info("TC23 - Products added to cart.");
		} catch (Exception e) {
			log.error("TC23 - Add to cart failed: ", e);
			Assert.fail("Add to cart failed: " + e.getMessage());
		}
	}

	@Test(priority = 3, dependsOnMethods = { "testAddToCart" })
	public void testAddressVerification() {
		try {
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);

			checkoutPage.clickProceedToCheckout();
			checkoutPage.verifyCheckoutPageVisible();

			// Verify delivery address
			checkoutPage.verifyAddressLine(user.getAddress(),
					checkoutPage.getDeliveryAddressLine1());
			checkoutPage.verifyCityStateZip(user.getCity(), user.getState(),
					user.getZipcode(), checkoutPage.getDeliveryAddressCityStateZip());

			// Verify billing address
			checkoutPage.verifyAddressLine(user.getAddress(),
					checkoutPage.getBillingAddressLine1());
			checkoutPage.verifyCityStateZip(user.getCity(), user.getState(),
					user.getZipcode(), checkoutPage.getBillingAddressCityStateZip());

			// Delete account
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC23 - Address verification completed.");
		} catch (Exception e) {
			log.error("TC23 - Address verification failed: ", e);
			Assert.fail("Address verification failed: " + e.getMessage());
		}
	}
}
