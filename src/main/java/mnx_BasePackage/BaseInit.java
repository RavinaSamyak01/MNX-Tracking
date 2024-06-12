package mnx_BasePackage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class BaseInit {

	public static ResourceBundle rb = ResourceBundle.getBundle("config");
	public static StringBuilder msg = new StringBuilder();
	public static WebDriver driver;
	public static Properties storage = new Properties();
	public static Logger logs;
	public static ExtentReports report;
	public static ExtentTest test;
	public String EmailID = rb.getString("MainEmailAddress");

	@BeforeSuite
	public void beforeMethod() throws Exception {
		if (driver == null) {
			String logFilename = this.getClass().getSimpleName();
			logs = Logger.getLogger(logFilename);
			startTest();
			storage = new Properties();
			FileInputStream fi = new FileInputStream(".\\src\\main\\resources\\config.properties");
			storage.load(fi);
			logs.info("initialization of the properties file is done");

			// --Opening Chrome Browser
			DesiredCapabilities capabilities = new DesiredCapabilities();

			/*
			 * WebDriverManager.chromedriver().clearDriverCache().setup();
			 * WebDriverManager.chromedriver().clearResolutionCache().setup();
			 */
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();

			options.addArguments("--headless", "--window-size=1920, 1080");
			options.addArguments("--window-size=1920, 1080");
			options.addArguments("start-maximized"); // open Browser in maximized mode
			options.addArguments("disable-infobars"); // disabling infobars
			options.addArguments("--disable-extensions"); // disabling extensions
			options.addArguments("--disable-gpu"); // applicable to Windows os only
			options.addArguments("--disable-dev-shm-usage"); // overcome limited resource
			options.addArguments("--no-sandbox"); // Bypass OS security model
			options.addArguments("--disable-in-process-stack-traces");
			options.addArguments("--disable-logging");
			options.addArguments("--log-level=3");
			options.addArguments("--remote-allow-origins=*");

			System.setProperty("webDriver.chrome.silentOutput", "true");
			String downloadFilepath = System.getProperty("user.dir") + "\\src\\main\\resources\\Downloads";
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("safebrowsing.enabled", "false");
			chromePrefs.put("download.default_directory", downloadFilepath);
			chromePrefs.put("download.prompt_for_download", "false");
			options.setExperimentalOption("prefs", chromePrefs);
			capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			capabilities.setPlatform(Platform.ANY);
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(options);
			// TimeUnit.SECONDS.sleep(1);

			// Default size
			driver.manage().window().maximize();

			/*
			 * Dimension newDimension = new Dimension(1038, 776);
			 * driver.manage().window().setSize(newDimension);
			 */

			// driver.manage().window().maximize();

		}

	}

	@BeforeMethod
	public void testMethodName(Method method) {

		String testName = method.getName();
		test = report.startTest(testName);

	}

	public static void startTest() {
		// You could find the xml file below. Create xml file in your project and copy
		// past the code mentioned below

		System.setProperty("extent.repoAutoFrameworkr.pdf.start", "true");
		System.setProperty("extent.repoAutoFrameworkr.pdf.out", "./Report/PDFExtentReport/ExtentPDF.pdf");

		// report.loadConfig(new File(System.getProperty("user.dir")
		// +"\\extent-config.xml"));
		report = new ExtentReports("./Report/ExtentReport/ExtentReportResults.html", true);
		// test = report.startTest();
	}

	public static void endTest() {
		report.endTest(test);
		report.flush();
	}

	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {

		String Env = storage.getProperty("Env");
		String baseUrl = null;
		if (Env.equalsIgnoreCase("PROD")) {

			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			// after execution, you could see a folder "FailedTestsScreenshots" under src
			// folder
			String destination = System.getProperty("user.dir") + "/Report/MNX_Screenshot/" + screenshotName + ".png";

			File finalDestination = new File(destination);
			try {
				FileUtils.copyFile(source, finalDestination);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return destination;
		}
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots" under src
		// folder
		String destination = System.getProperty("user.dir") + "/Report/Screenshots/MXTMS_Screenshot/" + screenshotName
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	public static String getFailScreenshot(WebDriver driver, String screenshotName) throws Exception {
		// String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots" under src
		// folder
		String destination = System.getProperty("user.dir") + "/Report/FailedTestsScreenshots/" + screenshotName
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	public static WebElement isElementPresent(String propkey) {

		try {

			if (propkey.contains("xpath")) {

				return driver.findElement(By.xpath(storage.getProperty(propkey)));

			} else if (propkey.contains("id")) {

				return driver.findElement(By.id(storage.getProperty(propkey)));

			} else if (propkey.contains("name")) {

				return driver.findElement(By.name(storage.getProperty(propkey)));

			} else if (propkey.contains("linkText")) {

				return driver.findElement(By.linkText(storage.getProperty(propkey)));

			} else if (propkey.contains("className")) {

				return driver.findElement(By.className(storage.getProperty(propkey)));

			} else if (propkey.contains("cssSelector")) {

				return driver.findElement(By.cssSelector(storage.getProperty(propkey)));

			} else {

				System.out.println("propkey is not defined");

				logs.info("prop key is not defined");
			}

		} catch (Exception e) {

		}
		return null;

	}

	public static void highLight(WebElement element, WebDriver driver) {
		// for (int i = 0; i < 2; i++) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: black; border: 4px solid red;");
			Thread.sleep(500);
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// }

	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception {

		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.FAIL, "Test Case Failed is " + result.getName());
			// test.log(LogStatus.FAIL, "Test Case Failed is " +
			// result.getThrowable().getMessage());
			test.log(LogStatus.FAIL, "Test Case Failed is " + result.getThrowable());
			// To capture screenshot path and store the path of the screenshot in the string
			// "screenshotPath"
			// We do pass the path captured by this mehtod in to the extent reports using
			// "logs.addScreenCapture" method.
			String screenshotPath = getFailScreenshot(driver, result.getName());
			// To add it in the extent report
			test.log(LogStatus.FAIL, test.addScreenCapture(screenshotPath));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(LogStatus.PASS, "Test Case Pass is " + result.getName());
			String screenshotPath = getScreenshot(driver, result.getName());
			// To add it in the extent report
			test.log(LogStatus.PASS, test.addScreenCapture(screenshotPath));
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, "Test Case Skipped is " + result.getName());
		}
	}

	public void Complete() throws Exception {
		// try {
		driver.close();
		driver.quit();

		/*
		 * } catch (Exception e) { driver.quit();
		 * 
		 * }
		 */

	}

	public String CuDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy ");
		Date date = new Date();
		String date1 = dateFormat.format(date);
		System.out.println("Current Date :- " + date1);
		return date1;
	}

	public static String getDate(Calendar cal) {
		return "" + cal.get(Calendar.MONTH) + "/" + (cal.get(Calendar.DATE) + 1) + "/" + cal.get(Calendar.YEAR);
	}

	public static Date addDays(Date d, int days) {
		d.setTime(d.getTime() + days * 1000 * 60 * 60 * 24);
		return d;
	}

	public void scrollToElement(WebElement element, WebDriver driver) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static String getData(String sheetName, int row, int col)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		String Env = storage.getProperty("Env");
		String FilePath = null;
		if (Env.equalsIgnoreCase("Test")) {
			FilePath = storage.getProperty("TestFile");
		} else if (Env.equalsIgnoreCase("STG")) {
			FilePath = storage.getProperty("STGFile");
		} else if (Env.equalsIgnoreCase("PROD")) {
			FilePath = storage.getProperty("PRODFile");
		}

		File src = new File(FilePath);

		FileInputStream FIS = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(FIS);
		Sheet sh1 = workbook.getSheet(sheetName);

		DataFormatter formatter = new DataFormatter();
		String Cell = formatter.formatCellValue(sh1.getRow(row).getCell(col));
		FIS.close();
		return Cell;
	}

	public static void setData(String sheetName, int row, int col, String value)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		String Env = storage.getProperty("Env");
		String FilePath = null;
		if (Env.equalsIgnoreCase("Test")) {
			FilePath = storage.getProperty("TestFile");
		} else if (Env.equalsIgnoreCase("STG")) {
			FilePath = storage.getProperty("STGFile");
		} else if (Env.equalsIgnoreCase("PROD")) {
			FilePath = storage.getProperty("PRODFile");
		}

		File src = new File(FilePath);
		FileInputStream fis = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(fis);
		FileOutputStream fos1 = new FileOutputStream(src);
		Sheet sh = workbook.getSheet(sheetName);

		sh.getRow(row).createCell(col).setCellValue(value);
		workbook.write(fos1);
		fos1.close();
		fis.close();
	}

	public static int getTotalRow(String sheetName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		String Env = storage.getProperty("Env");
		String FilePath = null;
		if (Env.equalsIgnoreCase("Test")) {
			FilePath = storage.getProperty("TestFile");
		} else if (Env.equalsIgnoreCase("STG")) {
			FilePath = storage.getProperty("STGFile");
		} else if (Env.equalsIgnoreCase("PROD")) {
			FilePath = storage.getProperty("PRODFile");
		}

		File src = new File(FilePath);

		FileInputStream FIS = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(FIS);
		Sheet sh1 = workbook.getSheet(sheetName);

		int rowNum = sh1.getLastRowNum() + 1;
		FIS.close();
		return rowNum;

	}

	public static int getTotalCol(String sheetName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		String Env = storage.getProperty("Env");
		String FilePath = null;
		if (Env.equalsIgnoreCase("Test")) {
			FilePath = storage.getProperty("TestFile");
		} else if (Env.equalsIgnoreCase("STG")) {
			FilePath = storage.getProperty("STGFile");
		} else if (Env.equalsIgnoreCase("PROD")) {
			FilePath = storage.getProperty("PRODFile");
		}
		File src = new File(FilePath);

		FileInputStream FIS = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(FIS);
		Sheet sh1 = workbook.getSheet(sheetName);

		Row row = sh1.getRow(0);
		int colNum = row.getLastCellNum();
		FIS.close();
		return colNum;

	}

	@AfterSuite
	public void SendEmail() throws Exception {

		report.flush();
		// --Close browser
		Complete();
		System.out.println("====Sending Email=====");
		logs.info("====Sending Email=====");
		// Send Details email

		msg.append("\n\n" + "*** This is automated generated email and send through automation script ***" + "\n");
		msg.append("Please find attached file of Report and Log");

		String Env = storage.getProperty("Env");
		String File = null;
		String subject = "Selenium Automation Script:" + Env + " MNX Tracking";

		File = ".\\Report\\ExtentReport\\ExtentReportResults.html,.\\src\\main\\resources\\log\\MNXTracking.html";

		try {

			SendEmail.sendMail(EmailID, subject, msg.toString(), File);

		} catch (Exception ex) {
			logs.error(ex);
		}
	}

	public void isFileDownloaded(String fileName) {
		String downloadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\Downloads";
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();

		for (int i = 0; i < dirContents.length; i++) {
			if (dirContents[i].getName().contains(fileName)) {
				logs.info("File is exist with FileName==" + fileName);
				// File has been found, it can now be deleted:
				dirContents[i].delete();
				logs.info(fileName + " File is Deleted");

			} else {
				logs.info("File is not exist with Filename==" + fileName);
			}
		}

	}

	public static boolean waitUntilFileToDownload(String fileName) throws InterruptedException {
		String downloadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\Downloads";
		File dir = new File(downloadPath);
		File[] dir_contents = dir.listFiles();

		if (dir_contents != null) {
			for (File dir_content : dir_contents) {
				if (dir_content.getName().contains(fileName))
					return true;
				logs.info("File is downloaded");
				break;
			}
		}

		return false;
	}

	public String getTimeAsTZone(String timeZone) {

		System.out.println("ZoneID of is==" + timeZone);
		logs.info("ZoneID of is==" + timeZone);
		if (timeZone.equalsIgnoreCase("ET")) {
			timeZone = "America/New_York";
		} else if (timeZone.equalsIgnoreCase("CT")) {
			timeZone = "CST";
		} else if (timeZone.equalsIgnoreCase("PT")) {
			timeZone = "PST";
		} else if (timeZone.equalsIgnoreCase("MT")) {
			timeZone = "America/Denver";
		}

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		logs.info(dateFormat.format(date));
		String Time = dateFormat.format(date);
		System.out.println("Time==" + Time);
		return Time;

	}

	public String getTimeAsTZoneWithOneMinuteAdded(String timeZone) {
		System.out.println("ZoneID of is==" + timeZone);
		logs.info("ZoneID of is==" + timeZone);
		if (timeZone.equalsIgnoreCase("ET")) {
			timeZone = "America/New_York";
		} else if (timeZone.equalsIgnoreCase("CT")) {
			timeZone = "CST";
		} else if (timeZone.equalsIgnoreCase("PT")) {
			timeZone = "PST";
		} else if (timeZone.equalsIgnoreCase("MT")) {
			timeZone = "America/Denver";
		}

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		calendar.add(Calendar.MINUTE, 1); // Add 1 minute to the current time

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		logs.info(dateFormat.format(calendar.getTime()));
		String time = dateFormat.format(calendar.getTime());
		System.out.println("Time==" + time);
		return time;
	}

	public String getDateAsTZone(String timeZone) {

		System.out.println("ZoneID is==" + timeZone);
		logs.info("ZoneID is==" + timeZone);
		if (timeZone.equalsIgnoreCase("ET")) {
			timeZone = "America/New_York";
		} else if (timeZone.equalsIgnoreCase("CT")) {
			timeZone = "CST";
		} else if (timeZone.equalsIgnoreCase("PT")) {
			timeZone = "PST";
		} else if (timeZone.equalsIgnoreCase("MT")) {
			timeZone = "America/Denver";
		}

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		logs.info(dateFormat.format(date));
		String Date = dateFormat.format(date);
		System.out.println("Date==" + Date);
		return Date;

	}

	public void resetResultofExcel() throws EncryptedDocumentException, InvalidFormatException, IOException {
		String Env = storage.getProperty("Env");
		String FilePath = null;

		if (Env.equalsIgnoreCase("STG")) {
			FilePath = storage.getProperty("STGResultFile");
		} else if (Env.equalsIgnoreCase("TEST")) {
			FilePath = storage.getProperty("TESTResultFile");
		} else if (Env.equalsIgnoreCase("Prod")) {
			FilePath = storage.getProperty("PRODResultFile");
		}

		File src = new File(FilePath);
		FileInputStream fis = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(fis);
		FileOutputStream fos1 = new FileOutputStream(src);
		Sheet sh = workbook.getSheet("Result");

		// --Get total Row
		int totalrow = sh.getLastRowNum() + 1;
		System.out.println("Total row==" + totalrow);
		// -Get total Col
		Row RowNo = sh.getRow(0);
		int totalcol = RowNo.getLastCellNum();
		System.out.println("Total Columns==" + totalcol);

		int ResultColIndex = 0;
		int FailLogColIndex = 0;

		// --Get column index by its name

		for (int tcol = 0; tcol < totalcol; tcol++) {
			String Colname = sh.getRow(0).getCell(tcol).getStringCellValue();

			System.out.println("Colname==" + Colname);

			if (Colname.equalsIgnoreCase("Result")) {
				ResultColIndex = sh.getRow(0).getCell(tcol).getColumnIndex();
				System.out.println("Index of the column==" + ResultColIndex);

			} else if (Colname.equalsIgnoreCase("Fail Log")) {
				FailLogColIndex = sh.getRow(0).getCell(tcol).getColumnIndex();
				System.out.println("Index of the column==" + FailLogColIndex);
				break;
			}

		}

		// --Set blank value in Result and Fail Log column
		for (int row = 1; row < totalrow; row++) {
			fis = new FileInputStream(src);
			fos1 = new FileOutputStream(src);

			try {
				sh.getRow(row).createCell(ResultColIndex).setCellValue("");
				sh.getRow(row).createCell(FailLogColIndex).setCellValue("");
				workbook.write(fos1);
				fos1.close();
				fis.close();
			} catch (Exception e) {
				fos1.close();
				fis.close();
				logs.info("Issue in SetData" + e);

			}
		}

	}

	public static void setResultData(String sheetName, int row, int col, String value)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		String Env = storage.getProperty("Env");
		String FilePath = null;
		if (Env.equalsIgnoreCase("STG")) {
			FilePath = storage.getProperty("STGResultFile");
		} else if (Env.equalsIgnoreCase("Prod")) {
			FilePath = storage.getProperty("PRODResultFile");
		} else if (Env.equalsIgnoreCase("TEST")) {
			FilePath = storage.getProperty("TESTResultFile");
		}

		File src = new File(FilePath);
		FileInputStream fis = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(fis);
		FileOutputStream fos1 = new FileOutputStream(src);
		Sheet sh = workbook.getSheet(sheetName);

		try {
			sh.getRow(row).createCell(col).setCellValue(value);
			workbook.write(fos1);
			fos1.close();
			fis.close();

		} catch (Exception e) {
			fos1.close();
			fis.close();
			logs.info("Issue in SetData " + e);

		}

	}

	public static String getRandomInteger(String st) {
		Random rn = new Random();
		int ans = rn.nextInt(98) + 1;
		st = String.valueOf(ans);
		return st;
	}

	public void clearResult() {
		String Env = storage.getProperty("Env");
		String FilePath = null;
		if (Env.equalsIgnoreCase("STG")) {
			FilePath = storage.getProperty("STGResultFile");
		} else if (Env.equalsIgnoreCase("Prod")) {
			FilePath = storage.getProperty("PRODResultFile");
		} else if (Env.equalsIgnoreCase("TEST")) {
			FilePath = storage.getProperty("TESTResultFile");
		}

		try {
			File src = new File(FilePath);
			FileInputStream fis = new FileInputStream(src);
			Workbook workbook = WorkbookFactory.create(fis);
			FileOutputStream fos1 = new FileOutputStream(src);
			int numberOfSheets = workbook.getNumberOfSheets();
			System.out.println("Number of sheets: " + numberOfSheets);

			for (int i = 0; i < numberOfSheets; i++) {
				String sheetName = workbook.getSheetName(i);
				logs.info("Sheet " + (i + 1) + ": " + sheetName);

				Sheet sh = workbook.getSheet(sheetName);

				// --Get total Row
				int totalrow = sh.getLastRowNum() + 1;
				System.out.println("Total row==" + totalrow);

				// -Get total Col
				Row RowNo = sh.getRow(0);
				int totalcol = RowNo.getLastCellNum();
				System.out.println("Total Columns==" + totalcol);

				/*
				 * int row = 1; int col = 1; if (sheetName.equalsIgnoreCase("EditOrderTabs")) {
				 * row = 1; col = 2; } else { row = 1; col = 1; }
				 */
				// --clear the data
				for (int row = 1; row < totalrow; row++) {

					for (int col = 1; col < totalcol; col++) {
						fis = new FileInputStream(FilePath);
						fos1 = new FileOutputStream(src);

						if (sheetName.equalsIgnoreCase("OrderCreationProcess") && (row == 5 || row == 6)) {
							// logs.info("No need to clear");
						} else if (sheetName.equalsIgnoreCase("OrderCreationProcess") && (row == 12 || row == 13)) {
							// logs.info("No need to clear");
							// logs.info("No need to clear");
						} else if (sheetName.equalsIgnoreCase("OrderCreationProcess") && (row == 16 || row == 17)) {
							// logs.info("No need to clear");
						} else if (sheetName.equalsIgnoreCase("MXWeb_OrderCreationProcess") && (col == 17)) {
							// logs.info("No need to clear");
						} else if (sheetName.equalsIgnoreCase("EditOrderTabs") || sheetName.equalsIgnoreCase("Upload")
								|| sheetName.equalsIgnoreCase("Print") && (col == 1)) {
							// logs.info("No need to clear");
						} else if (sheetName.equalsIgnoreCase("Charges_Result") && (row == 1)) {
							// logs.info("No need to clear");
						} else if (sheetName.equalsIgnoreCase("Charges_Result") && (col == 1 || col == 4)) {
							// logs.info("No need to clear");
						} else {
							try {
								sh.getRow(row).createCell(col).setCellValue("");
								workbook.write(fos1);
								fos1.close();
								fis.close();
							} catch (Exception e) {
								fos1.close();
								fis.close();
								logs.info("Issue in SetData" + e);

							}
						}

					}

				}

			}
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
			logs.info(e);
		} catch (IOException e) {
			e.printStackTrace();
			logs.info(e);

		}
	}

	public void mxPartnerLogin() throws IOException, InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 50);

		String Env = storage.getProperty("Env");

		if (Env.equalsIgnoreCase("STG")) {
			String baseUrl = storage.getProperty("STGmxPartner_URL");
			driver.get(baseUrl);
			logs.info(baseUrl);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rpLogin")));
				String UserName = storage.getProperty("STGUserName");
				highLight(isElementPresent("MXPartnerUName_id"), driver);
				isElementPresent("MXPartnerUName_id").sendKeys(UserName);
				logs.info("Entered UserName");
				String Password = storage.getProperty("STGPassword");
				highLight(isElementPresent("MXPartnerPassword_id"), driver);
				isElementPresent("MXPartnerPassword_id").sendKeys(Password);
				logs.info("Entered Password");
			} catch (Exception e) {
				msg.append("URL is not working==FAIL");
				getScreenshot(driver, "MXPartner_LoginIssue");
				driver.quit();
				Env = storage.getProperty("Env");
				String File = ".\\Report\\AutoFramework_Screenshot\\MXPartner_LoginIssue.png";
				Env = storage.getProperty("Env");
				String subject = "Selenium Automation Script:" + Env + " MXTMS Smoke";

				try {
//				/kunjan.modi@samyak.com, pgandhi@samyak.com,parth.doshi@samyak.com
					/*
					 * SendEmail.
					 * sendMail("ravina.prajapati@samyak.com, asharma@samyak.com, parth.doshi@samyak.com"
					 * , subject, msg.toString(), File);
					 */

					SendEmail.sendMail(
							"ravina.prajapati@samyak.com,asharma@samyak.com, parth.doshi@samyak.com, saurabh.jain@samyak.com, himanshu.dholakia@samyak.com",
							subject, msg.toString(), File);

				} catch (Exception ex) {
					logs.error(ex);
				}

			}

		} else if (Env.equalsIgnoreCase("PROD")) {
			String baseUrl = storage.getProperty("PRODmxPartner_URL");
			driver.get(baseUrl);
			logs.info(baseUrl);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rpLogin")));
				String UserName = storage.getProperty("PRODUserName");
				highLight(isElementPresent("MXPartnerUName_id"), driver);
				isElementPresent("MXPartnerUName_id").sendKeys(UserName);
				logs.info("Entered UserName");
				String Password = storage.getProperty("PRODPassword");
				highLight(isElementPresent("MXPartnerPassword_id"), driver);
				isElementPresent("MXPartnerPassword_id").sendKeys(Password);
				logs.info("Entered Password");
			} catch (Exception e) {
				msg.append("URL is not working==FAIL");
				getScreenshot(driver, "MXPartner_LoginIssue");
				driver.quit();
				Env = storage.getProperty("Env");
				String File = ".\\Report\\AutoFramework_Screenshot\\MXPartner_LoginIssue.png";
				Env = storage.getProperty("Env");
				String subject = "Selenium Automation Script:" + Env + " MXTMS Smoke";

				try {
//				/kunjan.modi@samyak.com, pgandhi@samyak.com,parth.doshi@samyak.com
					/*
					 * SendEmail.
					 * sendMail("ravina.prajapati@samyak.com, asharma@samyak.com, parth.doshi@samyak.com"
					 * , subject, msg.toString(), File);
					 */

					SendEmail.sendMail(
							"ravina.prajapati@samyak.com,asharma@samyak.com, parth.doshi@samyak.com, saurabh.jain@samyak.com, himanshu.dholakia@samyak.com",
							subject, msg.toString(), File);

				} catch (Exception ex) {
					logs.error(ex);
				}

			}

		}

		highLight(isElementPresent("MXPartnerSingIn_id"), driver);
		isElementPresent("MXPartnerSingIn_id").click();
		logs.info("MXPartner Login done");
		Thread.sleep(5000);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("Workspace_ASPxRoundPanel1")));

	}

	public void mxPartnerlogOut() throws InterruptedException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		Actions act = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement LogOut = isElementPresent("MXPartnerSingout_id");
		wait.until(ExpectedConditions.visibilityOf(LogOut));
		act.moveToElement(LogOut).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(LogOut));
		highLight(LogOut, driver);
		js.executeScript("arguments[0].click();", LogOut);
		Thread.sleep(5000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rpLogin")));
		logs.info("Logout done");

	}

	public void refresh() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		Actions act = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait1 = new WebDriverWait(driver, 10);

		WebElement MXTMSLogo = isElementPresent("MXTMSLogo_xpath");
		wait.until(ExpectedConditions.visibilityOf(MXTMSLogo));
		act.moveToElement(MXTMSLogo).build().perform();
		wait1.until(ExpectedConditions.elementToBeClickable(MXTMSLogo));
		highLight(MXTMSLogo, driver);
		js.executeScript("arguments[0].click();", MXTMSLogo);
		Thread.sleep(5000);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("pl_Workspace_ASPxRoundPanel1_RPC")));

	}

	public String getDateTimeAsTZone(String timeZone) {

		System.out.println("ZoneID is==" + timeZone);
		logs.info("ZoneID is==" + timeZone);
		if (timeZone.equalsIgnoreCase("ET")) {
			timeZone = "America/New_York";
		} else if (timeZone.equalsIgnoreCase("CT")) {
			timeZone = "CST";
		} else if (timeZone.equalsIgnoreCase("PT")) {
			timeZone = "PST";
		} else if (timeZone.equalsIgnoreCase("MT")) {
			timeZone = "America/Denver";
		}

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		logs.info(dateFormat.format(date));
		String Date = dateFormat.format(date);
		System.out.println("DateTime==" + Date);
		return Date;

	}

	public String selectSundayDate() {
		LocalDate today = LocalDate.now(); // Get the current date
		LocalDate nextSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)); // Find the next Sunday
		// Define a formatter
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy ");

		// Convert the date to a string using the formatter
		String formattedDate = nextSunday.format(formatter);

		return formattedDate;
	}

	public String fullpageScreenshot(String filename) throws IOException {
		// Take the full-page screenshot
		Screenshot screenshot = new AShot().coordsProvider(new WebDriverCoordsProvider())
				.shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);

		// TakesScreenshot ts = (TakesScreenshot) driver;
		BufferedImage source = screenshot.getImage();
		String destination = System.getProperty("user.dir") + "/Report/Screenshots/" + filename + ".png";

		File finalDestination = new File(destination);
		copyFile(source, finalDestination);

		// after execution, you could see a folder "FailedTestsScreenshots" under src
		// folder

		// Save the screenshot in the screenshots directory
		// ImageIO.write(screenshot.getImage(), "PNG", new File(screenshotDir,
		// filename));
		return destination;

	}

	private void copyFile(BufferedImage source, File finalDestination) {
		// TODO Auto-generated method stub

	}

}
