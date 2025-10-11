package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.ProductsPage;

/**
 * Test Case 18: View Category Products
 * Verifies categories are visible and navigating to a sub-category works.
 */
public class TC18_ViewCategoryProductsTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC18_ViewCategoryProductsTest.class);

	@Test
	public void testViewCategoryProducts() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			// Verify categories are visible on the left sidebar
			productsPage.verifyCategoriesVisible();

			// Click Women > Dress
			productsPage.clickWomenCategory();
			productsPage.clickDressSubCategory();
			waitHelper.waitForPageToLoad();

			// Verify category page is displayed
			productsPage.verifyCategoryPageDisplayed();

			log.info("TC18 - View Category Products test completed.");
		} catch (Exception e) {
			log.error("TC18 - View Category Products failed: ", e);
			Assert.fail("View Category Products failed: " + e.getMessage());
		}
	}
}
