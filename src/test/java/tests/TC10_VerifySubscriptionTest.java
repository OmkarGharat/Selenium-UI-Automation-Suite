package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;

/**
 * Test Case 10: Verify Subscription in home page
 */
public class TC10_VerifySubscriptionTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC10_VerifySubscriptionTest.class);

	@Test
	public void testVerifySubscription() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			homePage.scrollToFooter();
			homePage.verifySubscriptionLabel();
			homePage.enterSubscriptionEmail("testuser@gmail.com");
			homePage.clickSubscribe();
			homePage.verifySubscriptionSuccess();

			log.info("TC10 - Subscription test completed.");
		} catch (Exception e) {
			log.error("TC10 - Subscription test failed: ", e);
			Assert.fail("Subscription test failed: " + e.getMessage());
		}
	}
}
