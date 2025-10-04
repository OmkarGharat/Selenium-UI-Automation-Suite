package utility;

import org.testng.Assert;

/**
 * Custom assertion utilities for common validation patterns.
 */
public class CustomAssert {

	/**
	 * Asserts that two strings are equal, ignoring case.
	 */
	public static void assertEqualsIgnoreCase(String actual, String expected, String message) {
		if (actual == null || expected == null) {
			Assert.fail("One of the values is null. Actual: " + actual + " | Expected: " + expected);
		}
		if (!actual.equalsIgnoreCase(expected)) {
			String formattedMessage = String.format("%s%nExpected (case-insensitive): [%s]%nActual: [%s]",
					message, expected, actual);
			Assert.fail(formattedMessage);
		}
	}

	/**
	 * Asserts that actual text contains the expected substring, ignoring case.
	 */
	public static void assertTextContainsIgnoreCase(String actual, String expectedSubstring, String message) {
		if (actual == null || expectedSubstring == null) {
			Assert.fail("One of the values is null. Actual: " + actual + " | Expected substring: " + expectedSubstring);
		}
		if (!actual.toLowerCase().contains(expectedSubstring.toLowerCase())) {
			Assert.fail(message + " | Actual: " + actual + " | Expected substring: " + expectedSubstring);
		}
	}

	/**
	 * Asserts that actual text matches the given regex pattern.
	 */
	public static void assertTextMatchesRegex(String actual, String regex, String message) {
		if (actual == null || regex == null) {
			Assert.fail("One of the values is null. Actual: " + actual + " | Regex: " + regex);
		}
		if (!actual.matches(regex)) {
			Assert.fail(message + " | Actual: " + actual + " | Regex: " + regex);
		}
	}
}
