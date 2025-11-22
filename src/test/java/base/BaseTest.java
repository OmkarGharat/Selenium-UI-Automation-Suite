package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import utility.WaitHelper;

/**
 * Base test class for all AutomationExercise test cases.
 * Handles browser setup, navigation, and teardown.
 */
public class BaseTest {

	protected WebDriver driver;
	protected WaitHelper waitHelper;
	protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

	private static final String BASE_URL = "https://www.automationexercise.com";

	@BeforeClass
	@Parameters({ "browser" })
	public void setup(@Optional("chrome") String browser) {

		log.info("Setting up browser: {}", browser);

		switch (browser.toLowerCase()) {
		case "chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			System.setProperty("webdriver.chrome.driver",
					"C:\\Apps\\CR_618FA.tmp\\CHROME.PACKED\\chrome\\Chrome-bin\\chromedriver.exe");
			chromeOptions.setBinary(
					"C:\\Apps\\CR_618FA.tmp\\CHROME.PACKED\\chrome\\Chrome-bin\\sigma.exe");
			chromeOptions.addArguments("--user-data-dir=C:\\Users\\Omkar\\AppData\\Local\\Sigma\\User Data");
			chromeOptions.addArguments("--profile-directory=Default");
			chromeOptions.addArguments("--disable-notifications");
			chromeOptions.addArguments("--no-sandbox");
			chromeOptions.addArguments("--disable-dev-shm-usage");
			driver = new ChromeDriver(chromeOptions);
			break;
		case "edge":
			driver = new EdgeDriver();
			break;
		case "firefox":
			driver = new FirefoxDriver();
			break;
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		waitHelper = new WaitHelper(driver);

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get(BASE_URL);

		log.info("Browser launched and navigated to {}", BASE_URL);
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			log.info("Browser closed.");
		}
	}

	public WebDriver getDriver() {
		return driver;
	}
}
