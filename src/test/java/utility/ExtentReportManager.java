package utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Manages ExtentReports lifecycle — creation, test tracking, and report generation.
 */
public class ExtentReportManager {

	private static ExtentReports extent;
	private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	public ExtentReportManager() {
		if (extent == null) {
			extent = new ExtentReports();
			ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
			htmlReporter.config().setDocumentTitle("AutomationExercise Test Report");
			htmlReporter.config().setReportName("Regression Suite");
			htmlReporter.config().setTheme(Theme.DARK);
			extent.attachReporter(htmlReporter);
		}
	}

	public ExtentTest createTest(String testName) {
		ExtentTest test = extent.createTest(testName);
		extentTest.set(test);
		return test;
	}

	public ExtentTest getTest() {
		return extentTest.get();
	}

	public void removeTest() {
		extentTest.remove();
	}

	public void flushReport() {
		if (extent != null) {
			extent.flush();
		}
	}
}
