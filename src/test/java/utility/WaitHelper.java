package utility;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Helper class for Selenium waits.
 * Provides reusable methods for waiting on page loads, element visibility, and clickability.
 */
public class WaitHelper {

	private WebDriver driver;
	private WebDriverWait wait;

	public WaitHelper(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	/**
	 * Creates a WebDriverWait with a custom timeout (clamped between 1-60s).
	 */
	public WebDriverWait waitForSeconds(int seconds) {
		int timeout = Math.max(1, Math.min(seconds, 60));
		return new WebDriverWait(driver, Duration.ofSeconds(timeout));
	}

	/**
	 * Waits until document.readyState is "complete".
	 */
	public void waitForPageToLoad() {
		wait.until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));
	}

	/**
	 * Waits until the given element is visible on the page.
	 */
	public void waitForVisibility(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Waits until the given element is displayed and has non-empty text.
	 */
	public void waitForVisibleText(WebElement element) {
		wait.until(d -> {
			try {
				return element.isDisplayed() && !element.getText().isEmpty();
			} catch (Exception e) {
				return false;
			}
		});
	}

	/**
	 * Waits until the given element is clickable.
	 */
	public void waitForClickability(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
}
