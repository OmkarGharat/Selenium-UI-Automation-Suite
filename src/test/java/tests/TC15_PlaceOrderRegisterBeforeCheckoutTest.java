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
 * Test Case 15: Place Order: Register before Checkout
 */
public class TC15_PlaceOrderRegisterBeforeCheckoutTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC15_PlaceOrderRegisterBeforeCheckoutTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testRegisterAccount() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();
			user = RandomUtility.getRandomUserFromExcel();

			// Register before checkout
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

			log.info("TC15 - User registered before checkout successfully.");
		} catch (Exception e) {
			log.error("TC15 - Registration failed: ", e);
			Assert.fail("Registration failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testRegisterAccount" })
	public void testCheckoutAndPay() {
		try {
			CartPage cartPage = new CartPage(driver, waitHelper);
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);
			PaymentPage paymentPage = new PaymentPage(driver, waitHelper);

			checkoutPage.clickContinue();
			waitHelper.waitForPageToLoad();
			checkoutPage.verifyLoggedInAsUser();

			// Add products to cart
			cartPage.clickProducts();
			waitHelper.waitForPageToLoad();
			cartPage.addFirstTwoProductsToCart();

			// Go to Checkout
			checkoutPage.clickProceedToCheckout();
			waitHelper.waitForPageToLoad();

			// Verify address
			checkoutPage.verifyAddressLine(user.getAddress(), checkoutPage.getDeliveryAddressLine1());
			checkoutPage.verifyAddressLine(user.getAddress(), checkoutPage.getBillingAddressLine1());

			// Comments
			checkoutPage.enterComment("Leave at door.");
			checkoutPage.clickPlaceOrder();
			waitHelper.waitForPageToLoad();

			// Pay
			paymentPage.fillPaymentDetails(user.getName(), "4222333344445555", "321", "09", "2030");
			paymentPage.clickPayAndConfirm();
			waitHelper.waitForPageToLoad();
			paymentPage.verifyOrderPlaced();

			// Clean up
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC15 - Place Order: Register before Checkout test completed successfully.");
		} catch (Exception e) {
			log.error("TC15 - Checkout/Order failed: ", e);
			Assert.fail("Checkout/Order failed: " + e.getMessage());
		}
	}
}
