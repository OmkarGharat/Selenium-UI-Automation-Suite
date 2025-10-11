package pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import utility.WaitHelper;

/**
 * Page object for the Products page (https://www.automationexercise.com/products).
 * Handles product listing, product detail view, search, and category navigation.
 */
public class ProductsPage {

	private WebDriver driver;
	private WaitHelper waitHelper;
	private static final Logger log = LoggerFactory.getLogger(ProductsPage.class);

	public ProductsPage(WebDriver driver, WaitHelper waitHelper) {
		this.driver = driver;
		this.waitHelper = waitHelper;
		PageFactory.initElements(driver, this);
	}

	// ===== Navigation to Products =====

	@FindBy(xpath = "//a[@href='/products']")
	private WebElement productsMenuLink;

	/**
	 * Clicks the Products link in the navigation bar.
	 */
	public void navigateToProducts() {
		productsMenuLink.click();
		log.info("Navigated to Products page.");
	}

	// ===== All Products Page =====

	@FindBy(xpath = "//h2[@class='title text-center']")
	private WebElement allProductsHeading;

	/**
	 * Verifies the 'ALL PRODUCTS' heading is visible.
	 */
	public void verifyAllProductsPageVisible() {
		Assert.assertTrue(allProductsHeading.isDisplayed(), "All Products heading not visible.");
		log.info("All Products page verified.");
	}

	// ===== Product Detail =====

	@FindBy(css = "a[href='/product_details/1']")
	private WebElement firstProductViewLink;

	@FindBy(xpath = "//div[@class='product-information']/h2")
	private WebElement productNameField;

	/**
	 * Gets the product name from the product details page.
	 */
	public String getProductName() {
		waitHelper.waitForVisibility(productNameField);
		return productNameField.getText();
	}

	@FindBy(xpath = "//div[@class='product-information']/p")
	private WebElement productCategoryField;

	@FindBy(xpath = "//div[@class='product-information']/span")
	private WebElement productPriceField;

	@FindBy(id = "quantity")
	private WebElement quantityField;

	@FindBy(xpath = "//div[@class='product-information']/p[2]")
	private WebElement availabilityLabel;

	@FindBy(xpath = "//div[@class='product-information']/p[3]")
	private WebElement conditionLabel;

	@FindBy(xpath = "//div[@class='product-information']/p[4]")
	private WebElement brandLabel;

	/**
	 * Clicks 'View Product' on the first product.
	 */
	public void openFirstProductDetails() {
		firstProductViewLink.click();
		log.info("Opened first product details.");
	}

	/**
	 * Verifies all product detail fields are visible and populated.
	 */
	public void verifyProductDetails() {
		Assert.assertTrue(productNameField.isDisplayed(), "Product Name should be visible");
		Assert.assertFalse(productNameField.getText().isEmpty(), "Product Name should not be empty");

		Assert.assertTrue(productCategoryField.isDisplayed(), "Product Category should be visible");
		Assert.assertFalse(productCategoryField.getText().isEmpty(), "Product Category should not be empty");

		Assert.assertTrue(productPriceField.isDisplayed(), "Product Price should be visible");

		String qty = quantityField.getDomAttribute("value");
		Assert.assertEquals(qty, "1", "Default Quantity should be 1");

		Assert.assertTrue(availabilityLabel.isDisplayed(), "Product Availability should be visible");
		Assert.assertTrue(conditionLabel.isDisplayed(), "Product Condition should be visible");
		Assert.assertTrue(brandLabel.isDisplayed(), "Product Brand should be visible");

		log.info("All product details verified successfully.");
	}

	// ===== Search =====

	@FindBy(id = "search_product")
	private WebElement searchBox;

	@FindBy(id = "submit_search")
	private WebElement searchButton;

	@FindBy(xpath = "//div[@class='productinfo text-center']/p")
	private List<WebElement> productNames;

	/**
	 * Searches for a product by name.
	 */
	public void searchProduct(String searchTerm) {
		searchBox.sendKeys(searchTerm);
		searchButton.click();
		waitHelper.waitForPageToLoad();
		log.info("Searched for: {}", searchTerm);
	}

	/**
	 * Verifies all displayed products contain the search term in their name.
	 */
	public void verifySearchResults(String searchTerm) {
		String normalizedSearch = searchTerm.replaceAll("[^a-zA-Z]", "").toLowerCase();

		int invalidProducts = 0;
		for (WebElement product : productNames) {
			String productText = product.getText().replaceAll("[^a-zA-Z]", "").toLowerCase();
			if (!productText.contains(normalizedSearch)) {
				log.warn("Product '{}' does not match search term '{}'", product.getText(), searchTerm);
				invalidProducts++;
			}
		}

		Assert.assertEquals(invalidProducts, 0,
				"Found " + invalidProducts + " products not matching search term: " + searchTerm);
		log.info("All {} search results match the term '{}'.", productNames.size(), searchTerm);
	}

	// ===== Categories =====

	@FindBy(xpath = "//h4[@class='panel-title']/a")
	private List<WebElement> categories;

	@FindBy(xpath = "//h4[@class='panel-title']/a[contains(normalize-space(),'Women')]")
	private WebElement womenCategory;

	@FindBy(xpath = "(//div[@id='Women']//li)[1]")
	private WebElement dressSubCategory;

	@FindBy(xpath = "//div[@class='features_items']/h2")
	private WebElement categoryPageHeading;

	/**
	 * Verifies that product categories are visible on the sidebar.
	 */
	public void verifyCategoriesVisible() {
		for (WebElement category : categories) {
			Assert.assertTrue(category.isDisplayed(), "Category is not visible");
		}
		log.info("All {} categories visible on sidebar.", categories.size());
	}

	/**
	 * Clicks on the Women category.
	 */
	public void clickWomenCategory() {
		womenCategory.click();
		log.info("Clicked Women category.");
	}

	/**
	 * Clicks on the first sub-category under Women (Dress).
	 */
	public void clickDressSubCategory() {
		dressSubCategory.click();
		log.info("Clicked Dress sub-category.");
	}

	/**
	 * Verifies the category page heading is displayed correctly.
	 */
	public void verifyCategoryPageDisplayed() {
		waitHelper.waitForVisibleText(categoryPageHeading);
		String heading = categoryPageHeading.getText().toUpperCase();
		Assert.assertTrue(heading.contains("WOMEN"),
				"Category page heading doesn't contain 'WOMEN': " + heading);
		log.info("Category page heading verified: {}", heading);
	}

	// ===== Brand Sidebar Filter =====

	@FindBy(xpath = "//div[@class='brands_products']//a")
	private List<WebElement> brandLinks;

	/**
	 * Verifies that the Brands side-panel is visible.
	 */
	public void verifyBrandsVisible() {
		Assert.assertFalse(brandLinks.isEmpty(), "No brand links visible on the sidebar.");
		log.info("Brands side-panel verified visible.");
	}

	/**
	 * Click on the specified brand link.
	 */
	public void clickBrand(String brandName) {
		for (WebElement link : brandLinks) {
			if (link.getText().toLowerCase().contains(brandName.toLowerCase())) {
				waitHelper.waitForClickability(link);
				link.click();
				log.info("Clicked brand: {}", brandName);
				return;
			}
		}
		throw new AssertionError("Brand not found in sidebar: " + brandName);
	}

	/**
	 * Verify that the brand page is displayed.
	 */
	public void verifyBrandPageVisible(String brandName) {
		waitHelper.waitForVisibleText(allProductsHeading);
		String headingText = allProductsHeading.getText().toUpperCase();
		Assert.assertTrue(headingText.contains(brandName.toUpperCase()),
				"Expected brand name '" + brandName + "' in heading but got: " + headingText);
		log.info("Brand page verified for: {}", brandName);
	}

	// ===== Product Details Page Actions =====

	@FindBy(xpath = "//button[contains(@class,'cart')]")
	private WebElement detailsAddToCartBtn;

	@FindBy(xpath = "//u[text()='View Cart']")
	private WebElement detailsViewCartLink;

	/**
	 * Set product quantity on details page.
	 */
	public void setQuantity(String qty) {
		quantityField.clear();
		quantityField.sendKeys(qty);
		log.info("Set quantity to: {}", qty);
	}

	/**
	 * Click Add to Cart button on details page.
	 */
	public void clickAddToCart() {
		waitHelper.waitForClickability(detailsAddToCartBtn);
		detailsAddToCartBtn.click();
		log.info("Clicked Add to Cart on details page.");
	}

	/**
	 * Click View Cart link in the modal popup.
	 */
	public void clickDetailsViewCart() {
		waitHelper.waitForClickability(detailsViewCartLink);
		detailsViewCartLink.click();
		log.info("Clicked View Cart in success modal.");
	}

	// ===== Product Reviews =====

	@FindBy(xpath = "//a[text()='Write Your Review']")
	private WebElement reviewTab;

	@FindBy(id = "name")
	private WebElement reviewNameField;

	@FindBy(id = "email")
	private WebElement reviewEmailField;

	@FindBy(id = "review")
	private WebElement reviewTextField;

	@FindBy(id = "button-review")
	private WebElement submitReviewBtn;

	@FindBy(xpath = "//span[contains(text(),'Thank you for your review.')]")
	private WebElement reviewSuccessMsg;

	/**
	 * Verify review tab or header is visible.
	 */
	public void verifyReviewTabVisible() {
		Assert.assertTrue(reviewTab.isDisplayed(), "Write Your Review tab is not visible.");
		log.info("Write Your Review tab verified visible.");
	}

	/**
	 * Fill and submit product review.
	 */
	public void submitReview(String name, String email, String reviewText) {
		reviewNameField.sendKeys(name);
		reviewEmailField.sendKeys(email);
		reviewTextField.sendKeys(reviewText);
		submitReviewBtn.click();
		log.info("Submitted product review by: {}", name);
	}

	/**
	 * Verify success message for review submission.
	 */
	public void verifyReviewSuccess() {
		waitHelper.waitForVisibility(reviewSuccessMsg);
		Assert.assertTrue(reviewSuccessMsg.isDisplayed(), "Review success message is not visible.");
		log.info("Review success message verified.");
	}
}
