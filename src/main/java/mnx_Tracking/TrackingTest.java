package mnx_Tracking;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import mnx_BasePackage.BaseInit;

public class TrackingTest extends BaseInit {

	@Test
	public void test_Tracking() throws Exception {

		track_Connect TC = new track_Connect();
		track_MXTMS TM = new track_MXTMS();

		try {
			// --create Connect Job
			String COC = TC.oc_Connect();

			// --create MXTMS job
			String MOC = TM.oc_MXTMS();

			if (COC.equalsIgnoreCase("PASS") || MOC.equalsIgnoreCase("PASS")) {
				// --MNX Tracking
				mnx_Tracking();

				// --Cancel connect job
				TC.oCancel_Connect();

				// --cancel MXTMS job
				TM.oCancel_MXTMS();

			}

		} catch (Exception e) {
			logs.info(e);
			logs.info("MNX Tracking Issue");
			fullpageScreenshot("MNXTRack Issue");

			// --Cancel connect job
			TC.oCancel_Connect();

			// --cancel MXTMS job
			TM.oCancel_MXTMS();

		}

	}

	public void mnx_Tracking() throws InterruptedException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click

		WebDriverWait wait1 = new WebDriverWait(driver, 10);

		Actions act = new Actions(driver);

		String Env = storage.getProperty("Env");
		logs.info("Env==" + Env);

		String baseUrl = storage.getProperty("MNXPRODURL");
		driver.get(baseUrl);
		logs.info(baseUrl);
		msg.append("\n" + "URL==" + baseUrl);

		Thread.sleep(10000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fl-main-content")));

		try {
			Thread.sleep(5000);

			// --move to Accept all button
			WebElement AcceptAll = isElementPresent("MNXAcceptAll_xpath");
			wait.until(ExpectedConditions.visibilityOf(AcceptAll));
			act.moveToElement(AcceptAll).build().perform();
			wait1.until(ExpectedConditions.elementToBeClickable(AcceptAll));
			jse.executeScript("arguments[0].click();", AcceptAll);
			logs.info("Click on Accept All");

		} catch (Exception e) {
			logs.info(e);

		}
		msg.append("\n");

		try {
			for (int i = 1; i < 3; i++) {

				// --move to track button
				WebElement TrackBTN = isElementPresent("MNXTrackBTN_xpath");
				act.moveToElement(TrackBTN).build().perform();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.visibilityOf(TrackBTN));
				wait1.until(ExpectedConditions.elementToBeClickable(TrackBTN));
				jse.executeScript("arguments[0].click();", TrackBTN);
				logs.info("Click on Track button");

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpcf7-f1233-o1")));

				// --select prefix

				String Pickup = null;
				String Module = null;

				if (i == 1) {
					Select Pref = new Select(isElementPresent("MNXTPrefix_name"));
					Pref.selectByValue("C");

					// --enter pickupID
					Pickup = getData("Connect", 1, 32);
					WebElement PUID = isElementPresent("MNXTrackingNo_id");
					PUID.clear();
					PUID.sendKeys(Pickup);
					logs.info("Entered PickupID");

					Module = "Connect";

				} else if (i == 2) {
					Select Pref = new Select(isElementPresent("MNXTPrefix_name"));
					Pref.selectByValue("M");

					// --enter pickupID
					Pickup = getData("MXTMS", 1, 3);
					WebElement PUID = isElementPresent("MNXTrackingNo_id");
					PUID.clear();
					PUID.sendKeys(Pickup);
					logs.info("Entered PickupID");

					Module = "MXTMS";

				}

				// --click on track button
				WebElement Track = isElementPresent("MNXTrackingbutton_id");
				wait.until(ExpectedConditions.visibilityOf(Track));
				act.moveToElement(Track).build().perform();
				wait.until(ExpectedConditions.elementToBeClickable(Track));
				jse.executeScript("arguments[0].click();", Track);
				Thread.sleep(5000);

				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Print")));

					WebElement TrackDiv = isElementPresent("MNXTrackDiv_xpath");
					act.moveToElement(TrackDiv).build().perform();
					wait.until(ExpectedConditions.visibilityOf(TrackDiv));
					fullpageScreenshot(Module + "Tracking_" + Pickup);

					logs.info(Module + " Tracking PASS #" + Pickup);
					msg.append(Module + " Tracking PASS #" + Pickup + "\n");

				} catch (Exception e) {
					logs.info(e);
					fullpageScreenshot(Module + "Tracking_Issue" + Pickup);
					logs.info(Module + " Tracking FAIL #" + Pickup);
					msg.append(Module + " Tracking FAIL #" + Pickup + "\n");

				}

			}

		} catch (Exception e) {
			msg.append("URL is not working==FAIL");
			fullpageScreenshot("MNXPage_Issue");
			logs.info("Tracking FAIL");
			msg.append("\n\n" + "Tracking FAIL" + "\n");

			driver.quit();
			Env = storage.getProperty("Env");
			String File = ".\\Report\\Screenshots\\MXTMS-PROD-Screenshot\\LoginPageIssue.png";
			Env = storage.getProperty("Env");
			String subject = "Selenium Automation Script:" + Env + " MNX Tracking";

			try {
				mnx_BasePackage.SendEmail.sendMail(EmailID, subject, msg.toString(), File);

			} catch (Exception ex) {
				logs.error(ex);
			}

		}

		msg.append("\n");

	}

}
