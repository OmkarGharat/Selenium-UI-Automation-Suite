package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.HomePage;

/**
 * Test Case 17: Remove Products From Cart
 */
public class TC17_RemoveProductsFromCartTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC17_RemoveProductsFromCartTest.class);

	@Test(priority = 1)
	public void testAddProductsToCart() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			cartPage.clickProducts();
			waitHelper.waitForPageToLoad();

			cartPage.addFirstTwoProductsToCart();

			log.info("TC17 - Products added to cart.");
		} catch (Exception e) {
			log.error("TC17 - Adding products failed: ", e);
			Assert.fail("Adding products failed: " + e.getMessage());
		}
	}

	@Test(priority = 2, dependsOnMethods = { "testAddProductsToCart" })
	public void testRemoveProductsFromCart() {
		try {
			CartPage cartPage = new CartPage(driver, waitHelper);

			cartPage.removeAllProducts();
			cartPage.verifyCartIsEmpty();

			log.info("TC17 - Products removed from cart successfully.");
		} catch (Exception e) {
			log.error("TC17 - Remove products failed: ", e);
			Assert.fail("Remove products failed: " + e.getMessage());
		}
	}
}
