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
 * Test Case 14: Place Order: Register while Checkout
 */
public class TC14_PlaceOrderRegisterWhileCheckoutTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC14_PlaceOrderRegisterWhileCheckoutTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testAddProductsAndCheckoutPopup() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			// Add products to cart
			cartPage.clickProducts();
			waitHelper.waitForPageToLoad();
			cartPage.addFirstTwoProductsToCart();

			// Click Proceed To Checkout and redirect to signup
			cartPage.clickProceedToCheckout();
			cartPage.clickRegisterLoginPopup();
			waitHelper.waitForPageToLoad();

			log.info("TC14 - Products added and checkout popup redirect completed.");
		} catch (Exception e) {
			log.error("TC14 - Checkout redirect failed: ", e);
			Assert.fail("Checkout redirect failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testAddProductsAndCheckoutPopup" })
	public void testRegisterAndConfirmOrder() {
		try {
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);
			PaymentPage paymentPage = new PaymentPage(driver, waitHelper);

			user = RandomUtility.getRandomUserFromExcel();

			// Register account
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

			// Navigate back to checkout
			checkoutPage.clickContinue();
			waitHelper.waitForPageToLoad();
			checkoutPage.verifyLoggedInAsUser();

			driver.get("https://www.automationexercise.com/view_cart");
			waitHelper.waitForPageToLoad();
			cartPage.clickProceedToCheckout();
			waitHelper.waitForPageToLoad();

			// Verify address details
			checkoutPage.verifyAddressLine(user.getAddress(), checkoutPage.getDeliveryAddressLine1());
			checkoutPage.verifyAddressLine(user.getAddress(), checkoutPage.getBillingAddressLine1());

			// Place order
			checkoutPage.enterComment("Please deliver quickly. Thank you.");
			checkoutPage.clickPlaceOrder();
			waitHelper.waitForPageToLoad();

			// Pay and confirm
			paymentPage.fillPaymentDetails(user.getName(), "4111222233334444", "123", "11", "2029");
			paymentPage.clickPayAndConfirm();
			waitHelper.waitForPageToLoad();
			paymentPage.verifyOrderPlaced();

			// Clean up: delete account
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC14 - Place Order: Register while Checkout test completed successfully.");
		} catch (Exception e) {
			log.error("TC14 - Order placement failed: ", e);
			Assert.fail("Order placement failed: " + e.getMessage());
		}
	}
}
