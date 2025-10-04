package utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Captures and saves browser screenshots on demand.
 */
public class ScreenshotUtil {

	private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);

	/**
	 * Takes a screenshot and saves it to the screenshots/ directory.
	 */
	public static void takeScreenshot(WebDriver driver, String testName) {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File srcFile = ts.getScreenshotAs(OutputType.FILE);

		String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		String destPath = "./screenshots/" + testName + "_" + timestamp + ".png";

		try {
			FileUtils.copyFile(srcFile, new File(destPath));
			log.info("Screenshot saved: {}", destPath);
		} catch (IOException e) {
			log.error("Failed to save screenshot: {}", e.getMessage());
		}
	}
}
