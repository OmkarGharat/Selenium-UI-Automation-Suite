package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;

import utility.ExtentReportManager;

/**
 * TestNG listener that integrates with ExtentReports for test reporting.
 */
public class TestListener implements ITestListener {

	private static final Logger log = LoggerFactory.getLogger(TestListener.class);
	private final ExtentReportManager reportManager;

	public TestListener() {
		reportManager = new ExtentReportManager();
	}

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentTest test = reportManager.createTest(testName);
		result.setAttribute("test", test);
		log.info("Test started: {}", testName);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentTest test = (ExtentTest) result.getAttribute("test");
		if (test != null) {
			test.pass("Test passed");
		}
		reportManager.removeTest();
		log.info("Test passed: {}", result.getName());
	}

	@Override
	public void onTestFailure(ITestResult result) {
		ExtentTest test = (ExtentTest) result.getAttribute("test");

		String screenshotPath = null;
		Object testClass = result.getInstance();
		if (testClass instanceof base.BaseTest) {
			org.openqa.selenium.WebDriver driver = ((base.BaseTest) testClass).getDriver();
			if (driver != null) {
				screenshotPath = utility.ScreenshotUtil.takeScreenshot(driver, result.getName());
			}
		}

		if (test != null) {
			if (screenshotPath != null) {
				test.fail("Test failed: " + result.getThrowable(),
						com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.fail("Test failed: " + result.getThrowable());
			}
		}
		reportManager.removeTest();
		log.error("Test failed: {}", result.getName());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentTest test = (ExtentTest) result.getAttribute("test");
		if (test != null) {
			test.skip("Test skipped");
		}
		reportManager.removeTest();
		log.warn("Test skipped: {}", result.getName());
	}

	@Override
	public void onFinish(ITestContext context) {
		reportManager.flushReport();
		log.info("All tests completed. ExtentReport generated.");
	}
}
