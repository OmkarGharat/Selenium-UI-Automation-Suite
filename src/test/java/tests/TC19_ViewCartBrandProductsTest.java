package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.ProductsPage;

/**
 * Test Case 19: View & Cart Brand Products
 */
public class TC19_ViewCartBrandProductsTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC19_ViewCartBrandProductsTest.class);

	@Test
	public void testViewBrandProducts() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			productsPage.navigateToProducts();
			waitHelper.waitForPageToLoad();

			// Verify brands sidebar is visible
			productsPage.verifyBrandsVisible();

			// Click first brand (e.g. Polo)
			String firstBrand = "Polo";
			productsPage.clickBrand(firstBrand);
			waitHelper.waitForPageToLoad();
			productsPage.verifyBrandPageVisible(firstBrand);

			// Click another brand (e.g. H&M or Madame)
			String secondBrand = "Madame";
			productsPage.clickBrand(secondBrand);
			waitHelper.waitForPageToLoad();
			productsPage.verifyBrandPageVisible(secondBrand);

			log.info("TC19 - Brand products view test completed successfully.");
		} catch (Exception e) {
			log.error("TC19 - Brand products verification failed: ", e);
			Assert.fail("Brand products verification failed: " + e.getMessage());
		}
	}
}
