package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;

/**
 * Test Case 26: Verify Scroll Up without 'Arrow' button and Scroll Down functionality
 */
public class TC26_VerifyScrollUpWithoutArrowTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC26_VerifyScrollUpWithoutArrowTest.class);

	@Test
	public void testScrollUpWithoutArrow() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			// Scroll down to footer
			homePage.scrollToFooter();
			homePage.verifySubscriptionLabel();

			// Scroll up manually to top
			homePage.scrollToTop();
			Thread.sleep(1500); // Wait for scroll animation to complete

			// Verify header text is visible on screen
			homePage.verifyHeaderTextVisible();

			log.info("TC26 - Scroll Up without Arrow button test completed successfully.");
		} catch (Exception e) {
			log.error("TC26 - Scroll Up without arrow failed: ", e);
			Assert.fail("Scroll Up without arrow failed: " + e.getMessage());
		}
	}
}
