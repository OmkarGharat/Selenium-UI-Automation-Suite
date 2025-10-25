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
import pages.ProductsPage;
import pages.SignupPage;
import utility.RandomUtility;

/**
 * Test Case 20: Search Products and Verify Cart After Login
 */
public class TC20_SearchProductsVerifyCartAfterLoginTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC20_SearchProductsVerifyCartAfterLoginTest.class);
	private UserData user;

	@Test(priority = 1)
	public void testRegisterUser() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();
			user = RandomUtility.getRandomUserFromExcel();

			// Register a user first to have valid credentials
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

			log.info("TC20 - Setup user registered and logged out.");
		} catch (Exception e) {
			log.error("TC20 - Setup failed: ", e);
			Assert.fail("Setup failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testRegisterUser" })
	public void testSearchAndVerifyCartAfterLogin() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);
			SignupPage signupPage = new SignupPage(driver, waitHelper);
			CheckoutPage checkoutPage = new CheckoutPage(driver, waitHelper);

			productsPage.navigateToProducts();
			waitHelper.waitForPageToLoad();
			productsPage.verifyAllProductsPageVisible();

			// Search product
			String searchTerm = "Tshirt";
			productsPage.searchProduct(searchTerm);
			productsPage.verifySearchResults(searchTerm);

			// Add search products to cart
			cartPage.addFirstTwoProductsToCart(); // Adds first two products of current list (which is the search results!)
			cartPage.verifyCartContents();

			// Log in
			signupPage.clickSignupLogin();
			waitHelper.waitForPageToLoad();
			signupPage.login(user.getEmail(), user.getPassword());
			waitHelper.waitForPageToLoad();
			checkoutPage.verifyLoggedInAsUser();

			// Navigate back to cart page and verify products are still there
			driver.get("https://www.automationexercise.com/view_cart");
			waitHelper.waitForPageToLoad();
			cartPage.verifyCartContents();

			// Clean up
			checkoutPage.clickDeleteAccount();
			checkoutPage.verifyAccountDeleted();

			log.info("TC20 - Search Products and Verify Cart After Login test completed successfully.");
		} catch (Exception e) {
			log.error("TC20 - Cart persistence verification failed: ", e);
			Assert.fail("Cart persistence verification failed: " + e.getMessage());
		}
	}
}
