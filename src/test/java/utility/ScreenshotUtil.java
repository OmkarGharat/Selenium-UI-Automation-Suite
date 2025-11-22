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
	 * @return absolute path of saved screenshot, or null on failure
	 */
	public static String takeScreenshot(WebDriver driver, String testName) {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File srcFile = ts.getScreenshotAs(OutputType.FILE);

		String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		String destPath = "./screenshots/" + testName + "_" + timestamp + ".png";
		File destFile = new File(destPath);

		try {
			FileUtils.copyFile(srcFile, destFile);
			log.info("Screenshot saved: {}", destFile.getAbsolutePath());
			return destFile.getAbsolutePath();
		} catch (IOException e) {
			log.error("Failed to save screenshot: {}", e.getMessage());
			return null;
		}
	}
}
