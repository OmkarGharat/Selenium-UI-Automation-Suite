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
 * Test Case 13: Verify Product quantity in Cart
 */
public class TC13_VerifyProductQuantityInCartTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC13_VerifyProductQuantityInCartTest.class);

	@Test
	public void testProductQuantityInCart() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			productsPage.navigateToProducts();
			waitHelper.waitForPageToLoad();

			productsPage.openFirstProductDetails();
			waitHelper.waitForPageToLoad();

			String productName = productsPage.getProductName();

			// Increase quantity to 4
			productsPage.setQuantity("4");

			// Click Add to cart
			productsPage.clickAddToCart();

			// Click View Cart
			productsPage.clickDetailsViewCart();
			waitHelper.waitForPageToLoad();

			// Verify quantity
			cartPage.verifyProductQuantity(productName, "4");

			log.info("TC13 - Product quantity in cart verified successfully.");
		} catch (Exception e) {
			log.error("TC13 - Product quantity verification failed: ", e);
			Assert.fail("Product quantity verification failed: " + e.getMessage());
		}
	}
}
