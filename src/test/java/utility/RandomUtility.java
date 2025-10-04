package utility;

import java.util.List;
import java.util.Random;

import models.UserData;

/**
 * Provides random test data from the Excel data source.
 */
public final class RandomUtility {

	private static final String TEST_DATA_PATH = "src/test/resources/TestData.xlsx";

	/**
	 * Returns a random UserData record from the Excel test data file.
	 */
	public static UserData getRandomUserFromExcel() {
		List<UserData> users = ExcelReader.readUserData(TEST_DATA_PATH);
		return users.get(new Random().nextInt(users.size()));
	}
}
