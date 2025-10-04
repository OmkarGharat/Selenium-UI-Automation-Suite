package utility;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.UserData;

/**
 * Reads test data from Excel files and maps rows to UserData POJOs.
 */
public class ExcelReader {

	private static final Logger log = LoggerFactory.getLogger(ExcelReader.class);

	private static String getCellValue(Cell cell, DataFormatter formatter) {
		return (cell == null) ? "" : formatter.formatCellValue(cell).trim();
	}

	/**
	 * Reads all user data rows from the "Users" sheet of the given Excel file.
	 */
	public static List<UserData> readUserData(String filePath) {

		List<UserData> userList = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheet("Users");
			DataFormatter dataFormatter = new DataFormatter();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				UserData user = new UserData();

				String firstName = getCellValue(row.getCell(0), dataFormatter);
				String lastName = getCellValue(row.getCell(1), dataFormatter);

				user.setName(firstName + " " + lastName);

				// Generate unique email: firstname.lastname + timestamp + @gmail.com
				String uniqueEmail = generateUniqueEmail(firstName, lastName);
				user.setEmail(uniqueEmail);

				user.setGender(getCellValue(row.getCell(2), dataFormatter));
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setPassword(getCellValue(row.getCell(4), dataFormatter));

				user.setDay(getCellValue(row.getCell(5), dataFormatter));
				user.setMonth(getCellValue(row.getCell(6), dataFormatter));
				user.setYear(getCellValue(row.getCell(7), dataFormatter));

				user.setCompany(getCellValue(row.getCell(8), dataFormatter));

				user.setAddress(getCellValue(row.getCell(9), dataFormatter));
				user.setCity(getCellValue(row.getCell(10), dataFormatter));
				user.setState(getCellValue(row.getCell(11), dataFormatter));
				user.setZipcode(getCellValue(row.getCell(12), dataFormatter));
				user.setMobile(getCellValue(row.getCell(13), dataFormatter));

				userList.add(user);
			}

		} catch (Exception e) {
			log.error("Failed to read Excel data from: {}", filePath, e);
		}

		return userList;
	}

	private static String generateUniqueEmail(String firstName, String lastName) {
		String cleanFirstName = firstName.toLowerCase().replaceAll("[^a-z]", "");
		String cleanLastName = lastName.toLowerCase().replaceAll("[^a-z]", "");
		return String.format("%s.%s%d@gmail.com", cleanFirstName, cleanLastName, System.currentTimeMillis());
	}
}
