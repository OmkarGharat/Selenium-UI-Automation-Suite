package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.ProductsPage;

/**
 * Test Case 21: Add review on product
 */
public class TC21_AddReviewOnProductTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC21_AddReviewOnProductTest.class);

	@Test
	public void testAddProductReview() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			ProductsPage productsPage = new ProductsPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			productsPage.navigateToProducts();
			waitHelper.waitForPageToLoad();
			productsPage.verifyAllProductsPageVisible();

			productsPage.openFirstProductDetails();
			waitHelper.waitForPageToLoad();

			productsPage.verifyReviewTabVisible();

			// Fill in and submit review
			productsPage.submitReview("Omkar", "omkarg@gmail.com", "The fabric of this item is very comfortable and fits perfectly.");
			productsPage.verifyReviewSuccess();

			log.info("TC21 - Add review on product test completed successfully.");
		} catch (Exception e) {
			log.error("TC21 - Add review failed: ", e);
			Assert.fail("Add review failed: " + e.getMessage());
		}
	}
}
