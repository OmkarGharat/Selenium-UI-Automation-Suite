package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;

/**
 * Test Case 7: Verify Test Cases Page
 */
public class TC07_VerifyTestCasesPageTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(TC07_VerifyTestCasesPageTest.class);

	@Test
	public void testVerifyTestCasesPage() {
		try {
			HomePage homePage = new HomePage(driver, waitHelper);

			homePage.verifyHomePageLoaded();

			homePage.clickTestCases();
			waitHelper.waitForPageToLoad();

			String currentUrl = driver.getCurrentUrl();
			Assert.assertTrue(currentUrl.contains("/test_cases"), "URL does not contain /test_cases. Current URL: " + currentUrl);

			log.info("TC07 - Test Cases page navigation verified successfully.");
		} catch (Exception e) {
			log.error("TC07 - Test Cases page verification failed: ", e);
			Assert.fail("Test Cases page verification failed: " + e.getMessage());
		}
	}
}
