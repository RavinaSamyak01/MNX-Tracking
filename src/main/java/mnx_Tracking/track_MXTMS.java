package mnx_Tracking;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import mnx_BasePackage.BaseInit;

public class track_MXTMS extends BaseInit {

	public void oc_MXTMS() throws Exception {

		mxtmslogin();

		mxtms_createOrder();

		logOut();

	}

	public void oCancel_MXTMS() throws Exception {

		mxtmslogin();

		jobCancel();

		logOut();

	}

	public void mxtmslogin() throws InterruptedException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 50);

		String Env = storage.getProperty("Env");
		logs.info("Env==" + Env);

		if (Env.equalsIgnoreCase("PROD")) {
			String baseUrl = storage.getProperty("MXTMSPRODURL");
			driver.get(baseUrl);
			logs.info(baseUrl);
			Thread.sleep(2000);
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rpLogin_RPC")));
				String UserName = storage.getProperty("MXTMSPRODUserName");
				highLight(isElementPresent("UserName_id"), driver);
				isElementPresent("UserName_id").sendKeys(UserName);
				logs.info("Entered UserName");
				String Password = storage.getProperty("MXTMSPRODPassword");
				highLight(isElementPresent("Password_id"), driver);
				isElementPresent("Password_id").sendKeys(Password);
				logs.info("Entered Password");
			} catch (Exception e) {
				msg.append("URL is not working==FAIL");
				getScreenshot(driver, "LoginPageIssue");
				driver.quit();
				Env = storage.getProperty("Env");
				String File = ".\\Report\\Screenshots\\MXTMS-Stage-Screenshot\\LoginPageIssue.png";
				Env = storage.getProperty("Env");
				String subject = "Selenium Automation Script:" + Env + " MXTMS Smoke";

				try {
					mnx_BasePackage.SendEmail.sendMail(EmailID, subject, msg.toString(), File);

				} catch (Exception ex) {
					logs.error(ex);
				}

			}

		}
		highLight(isElementPresent("SignIn_id"), driver);
		isElementPresent("SignIn_id").click();
		logs.info("Login done");

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("pl_Workspace_ASPxRoundPanel1_RPC")));
		} catch (Exception e) {
			msg.append("Login is not working==FAIL");
			getScreenshot(driver, "LoginIssue");
			driver.quit();
			String File = ".\\Report\\Screenshots\\MXTMS-Stage-Screenshot\\MXWeb_LoginIssue.png";
			Env = storage.getProperty("Env");
			String subject = "Selenium Automation Script:" + Env + " MXTMS Smoke";

			try {
				mnx_BasePackage.SendEmail.sendMail(EmailID, subject, msg.toString(), File);

			} catch (Exception ex) {
				logs.error(ex);
			}

		}
	}

	public void logOut() throws InterruptedException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		Actions act = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement LogOut = isElementPresent("SignOut_id");
		wait.until(ExpectedConditions.visibilityOf(LogOut));
		act.moveToElement(LogOut).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(LogOut));
		highLight(LogOut, driver);
		js.executeScript("arguments[0].click();", LogOut);
		Thread.sleep(5000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rpLogin_RPC")));
		logs.info("Logout done");
		Thread.sleep(5000);

	}

	public void mxtms_createOrder() throws IOException {

		JavascriptExecutor js = (JavascriptExecutor) driver;// scroll,click
		WebDriverWait wait = new WebDriverWait(driver, 30);// wait time
		Actions act = new Actions(driver);
		WebDriverWait wait1 = new WebDriverWait(driver, 7);// wait time

		String ServiceID = null;

		try {
			// --Get Service
			ServiceID = getData("MXTMS", 1, 0);
			System.out.println("Service== " + ServiceID);
			logs.info("=====Service:- " + ServiceID + "=====");
			msg.append("\n" + "===Service:- " + ServiceID + "===" + "\n");

			// --Click on Operations
			WebElement OperationTab = isElementPresent("OperationTab_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(OperationTab));
			wait1.until(ExpectedConditions.elementToBeClickable(OperationTab));
			act.moveToElement(OperationTab).build().perform();
			js.executeScript("arguments[0].click();", OperationTab);
			System.out.println("Click on Operations Tab");
			logs.info("Click on Operations Tab");
			Thread.sleep(2000);

			// --Create Order
			WebElement CreateOrder = isElementPresent("OPCreatOrder_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(CreateOrder));
			wait1.until(ExpectedConditions.elementToBeClickable(CreateOrder));
			act.moveToElement(CreateOrder).build().perform();
			js.executeScript("arguments[0].click();", CreateOrder);
			System.out.println("Click on Create Order");
			logs.info("Click on Create Order");

			wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("pl_ws_fv_tx")));

			// --Enter AcNo
			String AccNo = getData("MXTMS", 1, 1);
			WebElement AcNo = isElementPresent("CorAccount_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(AcNo));
			wait1.until(ExpectedConditions.elementToBeClickable(AcNo));
			AcNo.clear();
			AcNo.sendKeys(AccNo);
			System.out.println("Enter Account Number");
			logs.info("Enter Account Number");
			AcNo.sendKeys(Keys.TAB);
			Thread.sleep(2000);

			WebElement Loader = isElementPresent("Loader_id");
			wait.until(ExpectedConditions.invisibilityOf(Loader));

			// --Select Shipper
			String Ship = getData("MXTMS", 1, 2);
			WebElement Shipper = isElementPresent("COShipper_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(Shipper));
			wait1.until(ExpectedConditions.elementToBeClickable(Shipper));
			Shipper.clear();
			Shipper.sendKeys(Ship);
			System.out.println("Enter Shipper");
			logs.info("Enter Shipper");
			Shipper.sendKeys(Keys.TAB);
			Thread.sleep(5000);

			// --Enter Pickup
			enterPickup(1);

			// --Get PU TimeZone
			WebElement PUTimeZone = isElementPresent("COPUtimeZone_id");
			String TimeZone = PUTimeZone.getAttribute("value");
			logs.info("Pickup timezone==" + TimeZone);
			System.out.println("Pickup timezone==" + TimeZone);

			// --Get the RouteWorkID
			String inLine = TimeZone;
			String[] lineSplits = inLine.split("-");
			String lineDetails = lineSplits[1];
			logs.info("Pickup timezone==" + lineDetails);
			System.out.println("Pickup timezone==" + lineDetails);

			String PUTime = getTimeAsTZone(lineDetails);
			System.out.println("PU Time==" + PUTime);
			logs.info("PU Time==" + PUTime);

			// --Enter Ready PU Time
			WebElement RPUtime = isElementPresent("COPUReadyTime_id");
			// wait1.until(ExpectedConditions.visibilityOfAllElements(RPUtime));
			wait1.until(ExpectedConditions.elementToBeClickable(RPUtime));
			RPUtime.clear();
			Thread.sleep(2000);
			PUTime = getTimeAsTZone(lineDetails);
			RPUtime.sendKeys(Keys.CONTROL, "a");
			RPUtime.sendKeys(Keys.BACK_SPACE);
			RPUtime.sendKeys(PUTime);
			System.out.println("Enter Ready PU Time");
			logs.info("Enter Ready PU Time");
			RPUtime.sendKeys(Keys.TAB);
			Thread.sleep(5000);

			// --Enter Del Zip
			String DelCode = getData("MXTMS", 1, 6);
			WebElement DelZip = isElementPresent("OCDelZip_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(DelZip));
			wait1.until(ExpectedConditions.elementToBeClickable(DelZip));
			DelZip.clear();
			DelZip.sendKeys(DelCode);
			DelZip.sendKeys(Keys.TAB);
			System.out.println("Enter DelZip");
			logs.info("Enter DelZip");
			wait.until(ExpectedConditions.invisibilityOf(Loader));

			Thread.sleep(10000);

			try {
				driver.switchTo().alert().accept();
				logs.info(
						"Alert accepted=Would you like to override the curent pickup instructions with: AC PU,ADD LEV PU");

				Thread.sleep(5000);
				getScreenshot(driver, "PickupZipSelected");
			} catch (Exception eee) {
				logs.info(eee);
				logs.info(
						"Alert:-Would you like to override the curent pickup instructions with: AC PU,ADD LEV PU not displayed");

			}

			// --Implement QDt instead of Manual

			/*
			 * // --Get Del TimeZone WebElement DelTimeZoneID =
			 * isElementPresent("CODeltimeZone_id"); String DelTimeZone =
			 * DelTimeZoneID.getAttribute("value"); logs.info("Delivery timezone==" +
			 * DelTimeZone); System.out.println("Delivery timezone==" + DelTimeZone);
			 * 
			 * // --Get the String DinLine = DelTimeZone; String[] DlineSplits =
			 * DinLine.split("-"); String DlineDetails = DlineSplits[1];
			 * logs.info("Delivery timezone==" + DlineDetails);
			 * System.out.println("Delivery timezone==" + DlineDetails);
			 * 
			 * String DelZone = getTimeAsTZone(DlineDetails);
			 * System.out.println("Del Time==" + DelZone); logs.info("Del Time==" +
			 * DelZone);
			 * 
			 * // --Enter Ready Del Time WebElement RDelTime =
			 * isElementPresent("CODelQDT_id"); //
			 * wait1.until(ExpectedConditions.visibilityOfAllElements(RDelTime));
			 * wait1.until(ExpectedConditions.elementToBeClickable(RDelTime)); DelZone =
			 * getTimeAsTZone(DlineDetails); RDelTime.sendKeys(Keys.CONTROL, "a");
			 * RDelTime.sendKeys(Keys.BACK_SPACE); RDelTime.sendKeys(DelZone);
			 * System.out.println("Enter Ready Del Time");
			 * logs.info("Enter Ready Del Time"); RDelTime.sendKeys(Keys.TAB);
			 * 
			 * String DelDate = getDateAsTZone(DlineDetails);
			 * System.out.println("Del Date==" + DelDate); logs.info("Del Date==" +
			 * DelDate);
			 * 
			 * // --Enter Ready Del Date WebElement RDelDate =
			 * isElementPresent("CODelDate_id");
			 * wait1.until(ExpectedConditions.visibilityOfAllElements(RDelDate));
			 * wait1.until(ExpectedConditions.elementToBeClickable(RDelDate));
			 * RDelDate.click(); RDelDate.clear(); Thread.sleep(2000);
			 * RDelDate.sendKeys(DelDate); RDelDate.sendKeys(Keys.TAB);
			 * System.out.println("Enter Ready Del Date");
			 * logs.info("Enter Ready Del Date"); Thread.sleep(5000);
			 */

			/*
			 * try { driver.switchTo().alert().accept(); logs.info("Alert accepted");
			 * 
			 * } catch (Exception eee) { logs.info(eee);
			 * logs.info("Alert regarding Del Date Time not displayed");
			 * 
			 * }
			 */

			// --move to downword
			WebElement Content = isElementPresent("CoContent_id");
			act.moveToElement(Content).build().perform();
			Thread.sleep(2000);

			// --Enter Service
			String ServID = getData("MXTMS", 1, 0);
			WebElement Service = isElementPresent("COService_id");
			act.moveToElement(Service).build().perform();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.visibilityOf(Service));
			wait1.until(ExpectedConditions.elementToBeClickable(Service));
			Service.clear();
			Thread.sleep(2000);
			Service.sendKeys(ServID);
			Service.sendKeys(Keys.TAB);
			System.out.println("Enter Service");
			logs.info("Enter Service");
			Thread.sleep(2000);

			// --Enter PCs
			WebElement PCs = isElementPresent("COPcs_id");
			act.moveToElement(PCs).build().perform();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.visibilityOf(PCs));
			wait1.until(ExpectedConditions.elementToBeClickable(PCs));
			PCs.clear();
			Thread.sleep(2000);
			PCs.sendKeys("2");
			System.out.println("Enter PCs");
			logs.info("Enter PCs");

			// --Enter Weight
			WebElement Weight = isElementPresent("CoWeight_id");
			wait.until(ExpectedConditions.visibilityOf(Weight));
			wait1.until(ExpectedConditions.elementToBeClickable(Weight));
			Weight.clear();
			Thread.sleep(2000);
			Weight.sendKeys("2");
			System.out.println("Enter Weight");
			logs.info("Enter Weight");

			// --Enter Pack
			WebElement Pack = isElementPresent("COPack_id");
			wait.until(ExpectedConditions.visibilityOf(Pack));
			wait1.until(ExpectedConditions.elementToBeClickable(Pack));
			js.executeScript("arguments[0].click();", Pack);
			logs.info("Click on Pack");
			Thread.sleep(10000);

			// --Switch to iFrame
			WebElement PackIFrame = isElementPresent("PackIframe_id");
			driver.switchTo().frame(PackIFrame);
			Thread.sleep(2000);
			wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("form1")));
			WebElement PackSelect = isElementPresent("COPackfirst_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(PackSelect));
			wait1.until(ExpectedConditions.elementToBeClickable(PackSelect));
			js.executeScript("arguments[0].click();", PackSelect);
			Thread.sleep(2000);
			// wait.until(ExpectedConditions.invisibilityOf(Loader));
			driver.switchTo().defaultContent();
			System.out.println("Select Pack");
			logs.info("Select Pack");
			Thread.sleep(10000);

			/*
			 * // --Enter Length WebElement Length = isElementPresent("COLength_id");
			 * wait1.until(ExpectedConditions.visibilityOfAllElements(Length));
			 * wait1.until(ExpectedConditions.elementToBeClickable(Length)); Length.clear();
			 * Thread.sleep(2000); Length.sendKeys("2"); System.out.println("Enter Length");
			 * logs.info("Enter Length");
			 * 
			 * // --Enter Width WebElement Width = isElementPresent("CoWidth_id");
			 * wait1.until(ExpectedConditions.visibilityOfAllElements(Width));
			 * wait1.until(ExpectedConditions.elementToBeClickable(Width)); Width.clear();
			 * Thread.sleep(2000); Width.sendKeys("2"); System.out.println("Enter Width");
			 * logs.info("Enter Width");
			 * 
			 * // --Enter Height WebElement Height = isElementPresent("CoHeight_id");
			 * wait1.until(ExpectedConditions.visibilityOfAllElements(Height));
			 * wait1.until(ExpectedConditions.elementToBeClickable(Height)); Height.clear();
			 * Thread.sleep(2000); Height.sendKeys("2"); System.out.println("Enter Height");
			 * logs.info("Enter Height");
			 * 
			 * // --Enter DimWit WebElement DimWit = isElementPresent("CoDimWit_id");
			 * wait1.until(ExpectedConditions.visibilityOfAllElements(DimWit));
			 * wait1.until(ExpectedConditions.elementToBeClickable(DimWit)); DimWit.clear();
			 * Thread.sleep(2000); DimWit.sendKeys("2"); System.out.println("Enter DimWit");
			 * logs.info("Enter DimWit");
			 */

			// --Enter Commodity
			WebElement Commodity = isElementPresent("COCommInp_id");
			act.moveToElement(Content).build().perform();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.visibilityOf(Commodity));
			wait1.until(ExpectedConditions.elementToBeClickable(Commodity));
			Commodity.click();
			Thread.sleep(2000);
			Commodity.sendKeys("Other Machinery");
			Thread.sleep(2000);
			/*
			 * Commodity.sendKeys(Keys.DOWN); Commodity.sendKeys(Keys.ENTER);
			 */
			System.out.println("Enter Commodity");
			logs.info("Enter Commodity");

			// --Enter Content
			Content = isElementPresent("CoContent_id");
			wait.until(ExpectedConditions.visibilityOf(Content));
			wait1.until(ExpectedConditions.elementToBeClickable(Content));
			Content.clear();
			Thread.sleep(2000);
			Content.sendKeys("Non - Dangerous");
			Thread.sleep(2000);
			/*
			 * Content.sendKeys(Keys.DOWN); Content.sendKeys(Keys.ENTER);
			 */
			System.out.println("Enter Content");
			logs.info("Enter Content");

			// --Calculate QDT

			getQDT(1);

			// --Click on SAve
			WebElement Save = isElementPresent("COSave_id");
			act.moveToElement(Save).build().perform();
			wait.until(ExpectedConditions.visibilityOf(Save));
			wait1.until(ExpectedConditions.elementToBeClickable(Save));
			js.executeScript("arguments[0].click();", Save);
			System.out.println("Click on Save button");
			logs.info("Click on Save button");
			wait.until(ExpectedConditions.invisibilityOf(Loader));
			Thread.sleep(5000);

			try {
				wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By.id("pl_wccPopupManager_popupControlManager_PW-1")));
				getScreenshot(driver, "Select Order Source");

				// --Switch to iFrame
				WebElement SourceIFrame = isElementPresent("OCSourceIframe_id");
				driver.switchTo().frame(SourceIFrame);
				Thread.sleep(2000);

				WebElement Source = isElementPresent("OCSourceNO_id");
				wait.until(ExpectedConditions.visibilityOf(Source));
				wait1.until(ExpectedConditions.elementToBeClickable(Source));
				Source.sendKeys("Email");
				System.out.println("Enter Source");
				logs.info("Enter Source");

				// --Click on Save button
				WebElement SaveSource = isElementPresent("OCSaveSource_id");
				wait.until(ExpectedConditions.visibilityOf(SaveSource));
				wait1.until(ExpectedConditions.elementToBeClickable(SaveSource));
				act.moveToElement(SaveSource).build().perform();
				SaveSource.click();
				System.out.println("Click on Save button");
				logs.info("Click on Save button");
				wait.until(ExpectedConditions.invisibilityOf(Loader));
				Thread.sleep(2000);

				driver.switchTo().defaultContent();

				try {
					WebElement Savesucc = isElementPresent("OCSaveSucMsg_id");
					wait.until(ExpectedConditions.visibilityOf(Savesucc));
					getScreenshot(driver, "OrderCreated_EGD");
					String SUccMsg = Savesucc.getText();
					logs.info("Message==" + SUccMsg);
					System.out.println("Message==" + SUccMsg);

					// --get OrderID
					WebElement GEToRDERid = isElementPresent("OCOrderID_id");
					wait.until(ExpectedConditions.visibilityOf(GEToRDERid));
					getScreenshot(driver, "Orderid_EGD");
					String OrderID = GEToRDERid.getText();
					logs.info("Order==" + OrderID);
					System.out.println("Order==" + OrderID);

					String[] GetOrder = OrderID.split("#");
					String OrdrID = GetOrder[1];
					logs.info("OrderID==" + OrdrID);
					System.out.println("OrderID==" + OrdrID);
					msg.append("===OrderID:- " + OrdrID + "===" + "\n");

					// --get PUID
					WebElement PickupID = isElementPresent("OCPick_id");
					wait.until(ExpectedConditions.visibilityOf(PickupID));
					getScreenshot(driver, "Orderid_EGD");
					String PUID = PickupID.getAttribute("value");
					logs.info("Order==" + PUID);
					System.out.println("Order==" + PUID);

					// --Set PUID in Sheet
					setData("MXTMS", 1, 3, OrdrID);
					// setResultData("Result", 1, 1, OrdrID);

					// --Set PUID in Sheet
					setData("MXTMS", 1, 4, PUID);
					// setResultData("Result", 1, 2, PUID);
					// setResultData("Result", 1, 3, "PASS");

				} catch (Exception ee) {
					logs.info(ee);
					getScreenshot(driver, "SaveSourcebuttonIssue");
					System.out.println("Issue with save Source button");
					logs.info("Issue with save button");
					// setResultData("Result", 1, 4, ee.getMessage());
					// setResultData("Result", 1, 3, "FAIL");

				}

			} catch (Exception e) {
				logs.info(e);
				getScreenshot(driver, "SavebuttonIssue");
				System.out.println("Issue with save button");
				logs.info("Issue with save button");
				setData("MXTMS", 1, 3, "FAIL");
				// setResultData("Result", 1, 3, "FAIL");

			}
		} catch (Exception e) {
			logs.info(e);
			getScreenshot(driver, "CreateOrderIssue_");
		}

	}

	public void enterPickup(int i) throws InterruptedException, EncryptedDocumentException, InvalidFormatException,
			IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click
		WebDriverWait wait = new WebDriverWait(driver, 30);// wait time
		Actions act = new Actions(driver);
		WebDriverWait wait1 = new WebDriverWait(driver, 5);

		// --Get pickupZip
		String PUZip = getData("MXTMS", i, 5);
		logs.info("Pickup Zipcode=" + PUZip);

		WebElement PickupZip = isElementPresent("COPickupZip_id");
		act.moveToElement(PickupZip).build().perform();
		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOf(PickupZip));
		wait1.until(ExpectedConditions.elementToBeClickable(PickupZip));
		PickupZip.click();
		PickupZip.clear();
		PickupZip.sendKeys(PUZip);
		PickupZip.sendKeys(Keys.TAB);
		logs.info("Enter PickupZip");
		Thread.sleep(10000);

		try {
			// --CLick on close button
			WebElement LooKuPClose = isElementPresent("COAddLookupClose_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(LooKuPClose));
			wait1.until(ExpectedConditions.elementToBeClickable(LooKuPClose));
			act.moveToElement(LooKuPClose).build().perform();
			act.moveToElement(LooKuPClose).click().build().perform();
			System.out.println("Click on Close button");
			logs.info("Click on Close button");
		} catch (Exception e) {

		}

		Thread.sleep(5000);

		// --Accept Alert

		try {
			driver.switchTo().alert().accept();
			logs.info(
					"Alert accepted=Would you like to override the curent pickup instructions with: AC PU,ADD LEV PU");

			Thread.sleep(5000);
			getScreenshot(driver, "PickupZipSelected");
		} catch (Exception eee) {
			logs.info(eee);
			logs.info(
					"Alert:-Would you like to override the curent pickup instructions with: AC PU,ADD LEV PU not displayed");

		}

	}

	public void getQDT(int i) throws InterruptedException, IOException, EncryptedDocumentException,
			InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click
		WebDriverWait wait = new WebDriverWait(driver, 60);// wait time
		Actions act = new Actions(driver);
		WebDriverWait wait1 = new WebDriverWait(driver, 10);

		WebElement QDT = isElementPresent("COQDTImg_id");
		wait1.until(ExpectedConditions.visibilityOfAllElements(QDT));
		wait1.until(ExpectedConditions.elementToBeClickable(QDT));
		act.moveToElement(QDT).build().perform();
		jse.executeScript("arguments[0].click();", QDT);
		System.out.println("Click on QDT icon");
		logs.info("Click on QDT icon");
		Thread.sleep(5000);

		// --wait for the iframe
		WebElement PackIFrame = isElementPresent("AutoPIFrame_id");
		wait.until(ExpectedConditions.visibilityOf(PackIFrame));
		driver.switchTo().frame(PackIFrame);
		Thread.sleep(2000);
		WebElement OkBTN = isElementPresent("COQDTPupOKBtn_id");
		wait1.until(ExpectedConditions.visibilityOfAllElements(OkBTN));
		wait1.until(ExpectedConditions.elementToBeClickable(OkBTN));
		jse.executeScript("arguments[0].click();", OkBTN);
		logs.info("Click on OK button of QDT popup");
		Thread.sleep(5000);

		// wait.until(ExpectedConditions.invisibilityOf(Loader));
		driver.switchTo().defaultContent();

		try {
			// QDT
			WebElement RDelTime = isElementPresent("CODelQDT_id");
			String RDelTimeValue = (String) jse.executeScript("return arguments[0].value", RDelTime);
			logs.info("QD Time=" + RDelTimeValue);

			// QDDate
			WebElement RDelDate = isElementPresent("CODelDate_id");
			String RDelDateValue = (String) jse.executeScript("return arguments[0].value", RDelDate);
			logs.info("QD Date=" + RDelDateValue);

			// QDT
			WebElement RUpdateDelTime = isElementPresent("COQDTupdateTime_id");
			String RUpdateDelTimeValue = (String) jse.executeScript("return arguments[0].value", RUpdateDelTime);
			logs.info("QD Time=" + RUpdateDelTimeValue);

			// QDDate
			WebElement RUpdateDelDate = isElementPresent("COQDTupdateDate_id");
			String RUpdateDelDateValue = (String) jse.executeScript("return arguments[0].value", RUpdateDelDate);
			logs.info("QD Date=" + RUpdateDelDateValue);

			if ((RDelTimeValue.isBlank()) || (RDelDateValue.isBlank()) || (RUpdateDelTimeValue.isBlank())
					|| (RUpdateDelDateValue.isBlank())) {
				getScreenshot(driver, "QDT_Issue" + i);
				logs.info("Issue in QDT calculation");
				logs.info("QDT calculation working=FAIL");
				msg.append("QDT calculation working=FAIL" + "\n");

			} else {
				getScreenshot(driver, "QDT_Working" + i);
				logs.info("QDT calculation working=PASS");
				msg.append("QDT calculation working=PASS" + "\n");

			}
		} catch (Exception e) {
			logs.info(e);
			getScreenshot(driver, "QDTIssue_" + i);
			logs.info("Issue with QDT");
			msg.append("QDT calculation working=FAIL" + "\n");

		}

	}

	public void jobCancel() throws EncryptedDocumentException, InvalidFormatException, IOException,
			InterruptedException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click
		WebDriverWait wait = new WebDriverWait(driver, 40);// wait time
		Actions act = new Actions(driver);
		WebDriverWait wait1 = new WebDriverWait(driver, 5);

		// msg.append("\n\n" + "Search Order Test" + "\n");
		try {
			wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"appworkspace\"]")));
			wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("pl_Workspace_gvOpsLog0_gvOpsLog")));
			logs.info("Operation Log is already opened");

		} catch (Exception ee) {
			logs.info("Operation Log is not Opened");
			// --Click on Operations
			WebElement OperationTab = isElementPresent("OperationTab_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(OperationTab));
			wait1.until(ExpectedConditions.elementToBeClickable(OperationTab));
			act.moveToElement(OperationTab).build().perform();
			jse.executeScript("arguments[0].click();", OperationTab);
			System.out.println("Click on Operations Tab");
			logs.info("Click on Operations Tab");
			Thread.sleep(2000);

			// --OPerations Log
			WebElement OPLog = isElementPresent("OPLog_id");
			wait1.until(ExpectedConditions.visibilityOfAllElements(OPLog));
			wait1.until(ExpectedConditions.elementToBeClickable(OPLog));
//		/	act.moveToElement(OPLog).build().perform();
			act.moveToElement(OPLog).build().perform();
			jse.executeScript("arguments[0].click();", OPLog);
			System.out.println("Click on OPerations Log");
			logs.info("Click on OPerations Log");
			wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"appworkspace\"]")));

			getScreenshot(driver, "OperationsLog");
		}

		// Enter JobID#
		String OrderID = getData("MXTMS", 1, 3);
		msg.append("OrderID=" + OrderID + "\n");

		logs.info("OrderID=" + OrderID + "\n");
		WebElement OrderInp = isElementPresent("OPLOrderId_id");
		OrderInp.clear();
		OrderInp.sendKeys(OrderID);
		OrderInp.sendKeys(Keys.TAB);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

		WebElement Search = isElementPresent("OPLSearchBTN_id");
		wait1.until(ExpectedConditions.elementToBeClickable(Search));
		jse.executeScript("arguments[0].click();", Search);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

		try {
			wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("pl_ws_fv_tx")));
			// msg.append("Same job is displayed with 2 status==PASS" + "\n");
			logs.info("Job is displayed in edit job");
			getScreenshot(driver, "EditJob_" + OrderID);
			// msg.append("\n" + "Search Order is working==PASS" + "\n");
			Thread.sleep(5000);

			// --update status cancel
			WebElement QuoteType = isElementPresent("QRQuoteType_id");
			act.moveToElement(QuoteType).build().perform();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.visibilityOf(QuoteType));
			QuoteType.sendKeys(Keys.CONTROL, "a");
			QuoteType.sendKeys(Keys.BACK_SPACE);
			QuoteType.sendKeys("CANCELLED");
			QuoteType.sendKeys(Keys.TAB);
			logs.info("Select Order as a CANCELLED");

			try {
				wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By.id("pl_wccPopupManager_popupControlManager_PW-1")));
				getScreenshot(driver, "OrderCancelPopUp");

				// --Switch to iFrame
				WebElement SourceIFrame = isElementPresent("PackIframe_id");
				driver.switchTo().frame(SourceIFrame);
				Thread.sleep(2000);

				WebElement CancelNote = isElementPresent("OCCanOrNote_id");
				wait.until(ExpectedConditions.visibilityOf(CancelNote));
				wait1.until(ExpectedConditions.elementToBeClickable(CancelNote));
				CancelNote.sendKeys("Order for test only");
				System.out.println("Enter Cancel Note");
				logs.info("Enter Cancel Note");

				// --Click on Save button
				WebElement SaveNote = isElementPresent("ChargeSave_id");
				wait.until(ExpectedConditions.visibilityOf(SaveNote));
				wait1.until(ExpectedConditions.elementToBeClickable(SaveNote));
				act.moveToElement(SaveNote).build().perform();
				SaveNote.click();
				System.out.println("Click on Save button");
				logs.info("Click on Save button");
				driver.switchTo().defaultContent();

				WebElement Loader = isElementPresent("Loader_id");
				wait.until(ExpectedConditions.invisibilityOf(Loader));
				Thread.sleep(5000);

				try {
					WebElement CancelOrder = isElementPresent("COCancelOrder_id");
					wait.until(ExpectedConditions.visibilityOf(CancelOrder));
					getScreenshot(driver, "OrderCanceled");
					String SUccMsg = CancelOrder.getText();
					logs.info("Message==" + SUccMsg);
					System.out.println("Message==" + SUccMsg);
					if (SUccMsg.equalsIgnoreCase("(CANCELLED)")) {
						logs.info("Order Cancelled");
						msg.append("Order Cancelled=PASS" + "\n");
					} else {
						logs.info("Order not Cancelled");
						msg.append("Order Cancelled=FAIL" + "\n");
					}

					// --Click on SAve
					WebElement Save = isElementPresent("COSave_id");
					act.moveToElement(Save).build().perform();
					wait.until(ExpectedConditions.visibilityOf(Save));
					wait1.until(ExpectedConditions.elementToBeClickable(Save));
					jse.executeScript("arguments[0].click();", Save);
					System.out.println("Click on Save button");
					logs.info("Click on Save button");
					wait.until(ExpectedConditions.invisibilityOf(Loader));
					Thread.sleep(5000);

				} catch (Exception e) {
					logs.info(e);
					getScreenshot(driver, "OrderCancelIssue");
					logs.info("Cancel Order issue");
					msg.append("Order Cancelled=FAIL" + "\n");

				}

				// wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("pl_Workspace_gvOpsLog0_gvOpsLog")));

				// --Get StageName
			} catch (Exception e) {
				getScreenshot(driver, "CancelNotesPopUpIssue");
				logs.info(e);
				logs.info("Cancel Order notes pop up not displayed");
				// msg.append("Same job is not displayed with 2 status" + "\n");

			}

		} catch (Exception e) {
			getScreenshot(driver, "NoJob" + OrderID);
			logs.info(e);
			logs.info("Job is not displayed in edit job");
			// msg.append("Same job is not displayed with 2 status" + "\n");
			try {
				WebElement Error = isElementPresent("OPLErrMsg_id");
				wait1.until(ExpectedConditions.visibilityOfAllElements(Error));
				String ErrorMsg = Error.getText();

				logs.info("Message==" + ErrorMsg);
				System.out.println("Message==" + ErrorMsg);

				if (ErrorMsg.contains("No record(s) found.")) {
					getScreenshot(driver, "NoRecordFound" + OrderID);
					System.out.println("There is no record with " + OrderID + " OrderID");
					logs.info("There is no record with " + OrderID + " OrderID");

				} else {
					getScreenshot(driver, "OtherError" + OrderID);
					System.out.println("There is another error");
					logs.info("There is another error");
				}

			} catch (Exception ee) {
				getScreenshot(driver, "NoJob" + OrderID);
				logs.info(ee);
				logs.info("Job is not displayed in edit job");
				logs.info("Issue with Search Order");

			}

		}
	}

}