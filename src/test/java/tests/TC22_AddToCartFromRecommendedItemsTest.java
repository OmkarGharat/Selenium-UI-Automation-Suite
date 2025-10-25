package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.HomePage;
import pages.ProductsPage;

/**
 * Test Case 22: Add to cart from Recommended items
 */
public class TC22_AddToCartFromRecommendedItemsTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC22_AddToCartFromRecommendedItemsTest.class);

	@Test
	public void testAddToCartFromRecommended() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			// Scroll down and verify recommended items
			homePage.scrollToRecommendedItems();
			homePage.verifyRecommendedItemsVisible();

			// Add recommended product and get its name
			String productName = homePage.addRecommendedProductToCart();

			// Go to Cart
			productsPage.clickDetailsViewCart();
			waitHelper.waitForPageToLoad();

			// Verify product is displayed in cart
			// Since we want to make sure it is in cart, we can use verifyProductQuantity or similar method.
			// Let's use verifyProductQuantity which we added!
			cartPage.verifyProductQuantity(productName, "1");

			log.info("TC22 - Add to cart from Recommended items test completed successfully.");
		} catch (Exception e) {
			log.error("TC22 - Recommended items test failed: ", e);
			Assert.fail("Recommended items test failed: " + e.getMessage());
		}
	}
}
