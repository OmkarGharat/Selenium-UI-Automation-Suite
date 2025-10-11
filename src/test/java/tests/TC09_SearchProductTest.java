package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.ProductsPage;

/**
 * Test Case 9: Search Product
 */
public class TC09_SearchProductTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC09_SearchProductTest.class);

	@Test
	public void testSearchProduct() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			productsPage.navigateToProducts();
			waitHelper.waitForPageToLoad();

			productsPage.verifyAllProductsPageVisible();

			String searchTerm = "Tshirt";
			productsPage.searchProduct(searchTerm);
			productsPage.verifySearchResults(searchTerm);

			log.info("TC09 - Search product test completed.");
		} catch (Exception e) {
			log.error("TC09 - Search product test failed: ", e);
			Assert.fail("Search product test failed: " + e.getMessage());
		}
	}
}
