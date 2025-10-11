package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.HomePage;

/**
 * Test Case 12: Add Products in Cart
 */
public class TC12_AddProductsToCartTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC12_AddProductsToCartTest.class);

	@Test
	public void testAddProductsToCart() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			cartPage.clickProducts();
			waitHelper.waitForPageToLoad();

			cartPage.addFirstTwoProductsToCart();
			cartPage.verifyCartContents();

			log.info("TC12 - Add products to cart test completed.");
		} catch (Exception e) {
			log.error("TC12 - Add to cart test failed: ", e);
			Assert.fail("Add to cart test failed: " + e.getMessage());
		}
	}
}
