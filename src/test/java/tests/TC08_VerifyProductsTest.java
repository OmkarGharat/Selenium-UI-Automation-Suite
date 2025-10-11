package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.ProductsPage;

/**
 * Test Case 8: Verify All Products and product detail page
 */
public class TC08_VerifyProductsTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC08_VerifyProductsTest.class);

	@Test
	public void testVerifyProductDetails() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			productsPage.navigateToProducts();
			waitHelper.waitForPageToLoad();

			productsPage.verifyAllProductsPageVisible();

			productsPage.openFirstProductDetails();
			waitHelper.waitForPageToLoad();

			productsPage.verifyProductDetails();

			log.info("TC08 - Product details verified successfully.");
		} catch (Exception e) {
			log.error("TC08 - Verify products test failed: ", e);
			Assert.fail("Verify products test failed: " + e.getMessage());
		}
	}
}
