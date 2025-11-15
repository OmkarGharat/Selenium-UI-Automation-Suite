package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;

/**
 * Test Case 25: Verify Scroll Up using 'Arrow' button and Scroll Down functionality
 */
public class TC25_VerifyScrollUpWithArrowTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC25_VerifyScrollUpWithArrowTest.class);

	@Test
	public void testScrollUpWithArrow() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			// Scroll down to footer
			homePage.scrollToFooter();
			homePage.verifySubscriptionLabel();

			// Click the scroll up arrow
			homePage.clickScrollUpArrow();
			Thread.sleep(1500); // Wait for scroll animation to complete

			// Verify header text is visible on screen
			homePage.verifyHeaderTextVisible();

			log.info("TC25 - Scroll Up using Arrow button test completed successfully.");
		} catch (Exception e) {
			log.error("TC25 - Scroll Up with arrow failed: ", e);
			Assert.fail("Scroll Up with arrow failed: " + e.getMessage());
		}
	}
}
