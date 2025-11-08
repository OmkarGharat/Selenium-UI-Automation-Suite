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
import pages.PaymentPage;
import pages.SignupPage;
import utility.RandomUtility;

/**
 * Test Case 16: Place Order: Login before Checkout
 */
public class TC16_PlaceOrderLoginBeforeCheckoutTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC16_PlaceOrderLoginBeforeCheckoutTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testRegisterAndLogout() {
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

			log.info("TC16 - Setup user registered and logged out.");
		} catch (Exception e) {
			log.error("TC16 - Setup failed: ", e);
			Assert.fail("Setup failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testRegisterAndLogout" })
	public void testLoginAndOrderCheckout() {
		try {
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);
			PaymentPage paymentPage = new PaymentPage(driver, waitHelper);

			// Login
			signupPage.login(user.getEmail(), user.getPassword());
			waitHelper.waitForPageToLoad();
			checkoutPage.verifyLoggedInAsUser();

			// Add products to cart
			cartPage.clickProducts();
			waitHelper.waitForPageToLoad();
			cartPage.addFirstTwoProductsToCart();

			// Proceed to checkout
			checkoutPage.clickProceedToCheckout();
			waitHelper.waitForPageToLoad();

			// Verify address
			checkoutPage.verifyAddressLine(user.getAddress(), checkoutPage.getDeliveryAddressLine1());
			checkoutPage.verifyAddressLine(user.getAddress(), checkoutPage.getBillingAddressLine1());

			// Comments
			checkoutPage.enterComment("Delivering before 6 PM.");
			checkoutPage.clickPlaceOrder();
			waitHelper.waitForPageToLoad();

			// Payment
			paymentPage.fillPaymentDetails(user.getName(), "5333444455556666", "456", "05", "2031");
			paymentPage.clickPayAndConfirm();
			waitHelper.waitForPageToLoad();
			paymentPage.verifyOrderPlaced();

			// Clean up
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC16 - Place Order: Login before Checkout test completed successfully.");
		} catch (Exception e) {
			log.error("TC16 - Checkout failed: ", e);
			Assert.fail("Checkout failed: " + e.getMessage());
		}
	}
}
