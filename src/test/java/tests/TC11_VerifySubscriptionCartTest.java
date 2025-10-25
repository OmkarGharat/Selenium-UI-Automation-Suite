package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.HomePage;

/**
 * Test Case 11: Verify Subscription in Cart page
 */
public class TC11_VerifySubscriptionCartTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC11_VerifySubscriptionCartTest.class);

	@Test
	public void testVerifySubscriptionInCart() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);
			CartPage cartPage = new CartPage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			// Navigate to Cart page
			cartPage.clickProducts(); // Click products and then cart, or click directly from header.
			// Let's click view cart directly. Wait, CartPage has viewCartButton or we can use navigation.
			// Let's see: HomePage doesn't have a direct Cart link element but we can find it by xpath: //a[contains(@href, '/view_cart')]
			driver.get("https://www.automationexercise.com/view_cart");
			waitHelper.waitForPageToLoad();

			homePage.scrollToFooter();
			homePage.verifySubscriptionLabel();
			homePage.enterSubscriptionEmail("carttestuser@gmail.com");
			homePage.clickSubscribe();
			homePage.verifySubscriptionSuccess();

			log.info("TC11 - Subscription in Cart page verified successfully.");
		} catch (Exception e) {
			log.error("TC11 - Subscription in Cart page verification failed: ", e);
			Assert.fail("Subscription in Cart page verification failed: " + e.getMessage());
		}
	}
}
