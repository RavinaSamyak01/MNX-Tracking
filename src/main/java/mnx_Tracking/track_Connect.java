package mnx_Tracking;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import mnx_BasePackage.BaseInit;

public class track_Connect extends BaseInit {

	public void oc_Connect() throws Exception {

		login();

		orderCreation();

		logOut();

	}

	public void oCancel_Connect() throws Exception {

		login();

		job_cancel(1);

		logOut();

	}

	public void login() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		// Actions act = new Actions(driver);
		String Env = storage.getProperty("Env");
		System.out.println("Env " + Env);
		logs.info("Env for execute script is : " + Env);

		String baseUrl = null;
		if (Env.equalsIgnoreCase("Prod")) {
			baseUrl = storage.getProperty("ConnectPRODURL");
			driver.get(baseUrl);
			Thread.sleep(2000);
			try {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("login")));
				String UserName = storage.getProperty("ConnectPRODUserName");
				highLight(isElementPresent("ConUserName_id"), driver);
				isElementPresent("ConUserName_id").sendKeys(UserName);
				logs.info("Entered UserName");
				String Password = storage.getProperty("ConnectPRODPassword");
				highLight(isElementPresent("ConPassword_id"), driver);
				isElementPresent("ConPassword_id").sendKeys(Password);
				logs.info("Entered Password");
			} catch (Exception e) {
				msg.append("URL is not working==FAIL");
				getScreenshot(driver, "LoginIssue");
				driver.quit();
				Env = storage.getProperty("Env");
				String File = ".\\Report\\MNX_Screenshot\\LoginIssue.png";
				Env = storage.getProperty("Env");
				String subject = "Selenium Automation Script:" + Env + " MNX Tracking";

				try {
//					/kunjan.modi@samyak.com, pgandhi@samyak.com,parth.doshi@samyak.com
					/*
					 * SendEmailOld.
					 * sendMail("ravina.prajapati@samyak.com, asharma@samyak.com, parth.doshi@samyak.com"
					 * , subject, msg.toString(), File);
					 */

					mnx_BasePackage.SendEmail.sendMail("ravina.prajapati@samyak.com", subject, msg.toString(), File);

				} catch (Exception ex) {
					logs.error(ex);
				}

			}
		}

		// BaseURL = baseUrl;
		msg.append("URL==" + baseUrl);
		highLight(isElementPresent("ConLogin_id"), driver);
		isElementPresent("ConLogin_id").click();
		logs.info("Login done");
		getScreenshot(driver, "ConnectLogin");
		try {

			wait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//span[contains(text(),'Logging In...')]")));

			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("welcomecontent")));

		} catch (Exception e) {

			WebDriverWait wait1 = new WebDriverWait(driver, 30);

			wait1.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//span[contains(text(),'Logging In...')]")));

			wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("welcomecontent")));
		}

		Thread.sleep(5000);

	}

	public void logOut() throws InterruptedException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		Actions act = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement LogOut = isElementPresent("LogOut_linkText");
		act.moveToElement(LogOut).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(LogOut));
		highLight(LogOut, driver);
		js.executeScript("arguments[0].click();", LogOut);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@ng-bind=\"LogoutMessage\"]")));
		String LogOutMsg = isElementPresent("LogOutMsg_xpath").getText();
		logs.info("Logout Message is displayed==" + LogOutMsg);
		logs.info("Logout done");
		getScreenshot(driver, "ConnectLogOut");

	}

	public void orderCreation()
			throws EncryptedDocumentException, InvalidFormatException, IOException, AWTException, InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click
		WebDriverWait wait = new WebDriverWait(driver, 20);// wait time

		WebDriverWait wait1 = new WebDriverWait(driver, 10);

		Actions act = new Actions(driver);

		Robot r = new Robot();
		String ServiceID = null;
		try {
			// --Go to Operation
			WebElement operation = isElementPresent("OperMenu_id");
			wait.until(ExpectedConditions.visibilityOf(operation));
			wait1.until(ExpectedConditions.elementToBeClickable(operation));
			act.moveToElement(operation).build().perform();
			act.moveToElement(operation).click().build().perform();
			logs.info("Click on Operation");

			// --Go to TaskLog
			WebElement TaskLog = isElementPresent("OpTaskLog_id");
			wait.until(ExpectedConditions.visibilityOf(TaskLog));
			wait1.until(ExpectedConditions.elementToBeClickable(TaskLog));
			act.moveToElement(TaskLog).click().build().perform();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtContains")));
			logs.info("Click on Tasklog");

			// --Click on New Order
			WebElement Order = isElementPresent("NewOrder_id");
			wait.until(ExpectedConditions.visibilityOf(Order));
			wait1.until(ExpectedConditions.elementToBeClickable(Order));
			jse.executeScript("arguments[0].click();", Order);
			logs.info("Click on New Order");

			// --Waiting for Order section
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("idOrder")));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			// --get the Data
			getData("Connect", 1, 0);

			// --Get Service
			ServiceID = getData("Connect", 1, 18);
			System.out.println("Service== " + ServiceID);
			logs.info("=====Service:- " + ServiceID + "=====");
			msg.append("\n" + "=====Service:- " + ServiceID + "=====" + "\n");

			// Enter Caller
			String Caller = getData("Connect", 1, 0);
			isElementPresent("OCCallerName_id").clear();
			isElementPresent("OCCallerName_id").sendKeys(Caller);
			logs.info("Entered CallerName");

			// Enter Phone
			String Phone = getData("Connect", 1, 1);
			isElementPresent("OCContactPh_id").clear();
			isElementPresent("OCContactPh_id").sendKeys(Phone);
			logs.info("Entered Contact/Phone");

			// Enter Account#
			String Account = getData("Connect", 1, 2);
			isElementPresent("OCCustCode_id").clear();
			isElementPresent("OCCustCode_id").sendKeys(Account);
			isElementPresent("OCCustCode_id").sendKeys(Keys.TAB);
			logs.info("Entered Customer Code : " + Account);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			try {
				WebElement DocDialogue = isElementPresent("DRVDialforDoc_xpath");
				wait.until(ExpectedConditions.visibilityOfAllElements(DocDialogue));
				logs.info("Dialogue is displayed for Customer");
				WebElement BTNOk = isElementPresent("DRVDialOKbtn_id");
				wait1.until(ExpectedConditions.elementToBeClickable(BTNOk));
				jse.executeScript("arguments[0].click();", BTNOk);
				logs.info("Clicked on OK button");

			} catch (Exception Doc) {
				logs.info("Dialogue is not displayed for Customer");

			}

			// --wait until pU section is enabled
			try {
				wait.until(ExpectedConditions
						.invisibilityOfElementLocated(By.xpath("//*[@id=\"PickupSection\"][@disabled=\"disabled\"]")));
			} catch (Exception PUDIsable) {
				logs.error(PUDIsable);
				getScreenshot(driver, "PUSDisabled");
				WebDriverWait waitPUD = new WebDriverWait(driver, 90);// wait time
				waitPUD.until(ExpectedConditions
						.invisibilityOfElementLocated(By.xpath("//*[@id=\"PickupSection\"][@disabled=\"disabled\"]")));
				logs.info("PU Section is Enabled");

			}
			// Enter Pickup Zip code
			String PUZip = getData("Connect", 1, 3);
			isElementPresent("OCPUZp_id").clear();
			isElementPresent("OCPUZp_id").sendKeys(PUZip);
			isElementPresent("OCPUZp_id").sendKeys(Keys.TAB);
			Thread.sleep(2000);
			logs.info("Entered PU Zip");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			// --PU Address
			WebElement Puaddr = isElementPresent("OCPUAdd_id");
			wait1.until(ExpectedConditions.elementToBeClickable(Puaddr));
			jse.executeScript("arguments[0].click();", Puaddr);
			logs.info("Click on PU Address");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			// --PU Company
			String PickupCom = getData("Connect", 1, 4);
			isElementPresent("OCPUComp_id").clear();
			isElementPresent("OCPUComp_id").sendKeys(PickupCom);
			logs.info("Entered PU Company");

			// --PU AddressLine1
			String PUAddress1 = getData("Connect", 1, 5);
			isElementPresent("OCPUAddL1_id").clear();
			isElementPresent("OCPUAddL1_id").sendKeys(PUAddress1);
			logs.info("Entered PU AddressLine1");

			// String Add2 = getData("Connect", 1, 6);
			// driver.findElement(By.id("txtPUAddrLine2")).sendKeys(Add2);

			// --PU Attention
			String Attn = getData("Connect", 1, 7);
			isElementPresent("OCPUAtt_id").clear();
			isElementPresent("OCPUAtt_id").sendKeys(Attn);
			logs.info("Entered PU Attention");

			// --PU Phone
			String PuPhone = getData("Connect", 1, 8);
			isElementPresent("OCPUPhone_id").clear();
			isElementPresent("OCPUPhone_id").sendKeys(PuPhone);
			logs.info("Entered PU Phone");

			// String PUInst = getData("Connect", 1, 9);
			// driver.findElement(By.id(" ")).sendKeys(PUInst);

			// --Wait to get PU Ready time

			try {
				wait1.until(ExpectedConditions.invisibilityOfElementLocated(
						By.xpath("//input[contains(@class,'ng-invalid ng-invalid-required')]")));
				logs.info("PU Ready time is blank");
				msg.append("\n" + "PU Ready time is blank" + "\n");
			} catch (Exception PUTimeNotExist) {
				logs.error(PUTimeNotExist);
				logs.info("PU Ready time is exist");

			}
			// set time in excel

			// tndrtime = driver.findElement(By.id("txtReadyforPickupTime")).getText();

			// --PU Miles
			String pmi = isElementPresent("OCPUMiles_id").getAttribute("value");
			logs.info("PU Mileage==" + pmi);

			// --Del Zip
			String DLZip = getData("Connect", 1, 11);
			isElementPresent("OCDLZip_id").clear();
			isElementPresent("OCDLZip_id").sendKeys(DLZip);
			isElementPresent("OCDLZip_id").sendKeys(Keys.TAB);
			logs.info("Entered DL Zip");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			Thread.sleep(5000);
			// --Del Address
			WebElement DL = isElementPresent("OCDLAdd_id");
			wait1.until(ExpectedConditions.elementToBeClickable(DL));
			jse.executeScript("arguments[0].click();", DL);
			logs.info("Entered DL Address");

			// --DEL Company
			String DelCompany = getData("Connect", 1, 12);
			isElementPresent("OCDLComp_id").clear();
			isElementPresent("OCDLComp_id").sendKeys(DelCompany);
			logs.info("Entered DL Company");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --DEL Address1
			String DLAddress1 = getData("Connect", 1, 13);
			isElementPresent("OCDLAddL1_id").clear();
			isElementPresent("OCDLAddL1_id").sendKeys(DLAddress1);
			logs.info("Entered DL Address Line1");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// String DLAddr2 = getData("Connect", 1, 14);
			// driver.findElement(By.id("txtDelAddrLine2")).sendKeys(DLAddr2);

			// --DL Attention
			String DLAttn = getData("Connect", 1, 15);
			isElementPresent("OCDLAtt_id").clear();
			isElementPresent("OCDLAtt_id").sendKeys(DLAttn);
			logs.info("Entered DL Attention");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --DL Phone
			String DLPhone = getData("Connect", 1, 16);
			isElementPresent("OCDLPhone_id").clear();
			isElementPresent("OCDLPhone_id").sendKeys(DLPhone);
			logs.info("Entered DL Phone");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --DL Miles
			String dmi = isElementPresent("OCDLMiles_id").getAttribute("value");
			logs.info("DL Miles==" + dmi);

			// String DLInst = getData("Connect", 1, 17);
			// driver.findElement(By.id("txtDLPhone")).sendKeys(DLInst);
			// String srv =
			// driver.findElement(By.id("idNewOrderServiceId")).getAttribute("value");

			// --Get the ServiceID
			String GeneratedServiceID = isElementPresent("OCServiceID_id").getAttribute("value");
			System.out.println("ServiceID==" + GeneratedServiceID);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --Total Qty
			isElementPresent("OCTotalQty_id").clear();
			// isElementPresent("OCTotalQty_id").sendKeys(Keys.BACK_SPACE);
			isElementPresent("OCTotalQty_id").sendKeys(Keys.CONTROL, "a");
			isElementPresent("OCTotalQty_id").sendKeys(Keys.BACK_SPACE);
			isElementPresent("OCTotalQty_id").clear();
			isElementPresent("OCTotalQty_id").clear();
			isElementPresent("OCTotalQty_id").sendKeys("01");
			isElementPresent("OCTotalQty_id").sendKeys(Keys.TAB);
			Thread.sleep(2000);
			logs.info("Entered Total Qty");

			// --Weight
			String Weight = getData("Connect", 1, 19);
			isElementPresent("OCWeight_id").clear();
			isElementPresent("OCWeight_id").sendKeys(Weight);
			isElementPresent("OCWeight_id").sendKeys(Keys.TAB);
			logs.info("Entered Weight");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --Length
			String Len = getData("Connect", 1, 20);
			isElementPresent("OCLength_id").clear();
			isElementPresent("OCLength_id").sendKeys(Len);
			isElementPresent("OCLength_id").sendKeys(Keys.TAB);
			logs.info("Entered Length");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --Width
			String Width = getData("Connect", 1, 21);
			isElementPresent("OCWidth_id").clear();
			isElementPresent("OCWidth_id").sendKeys(Width);
			isElementPresent("OCWidth_id").sendKeys(Keys.TAB);
			logs.info("Entered Width");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --Height
			String Height = getData("Connect", 1, 22);
			isElementPresent("OCHeight_id").clear();
			isElementPresent("OCHeight_id").sendKeys(Height);
			isElementPresent("OCHeight_id").sendKeys(Keys.TAB);
			logs.info("Entered Height");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			// -- COMMODITY_DROPDOWN

			Select comodity_drpdown = new Select(isElementPresent("comodity_drpdown_xpath"));
			comodity_drpdown.selectByIndex(1);
			logs.info("comodity dropdown is selected");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			Thread.sleep(2000);

			// --Commodity
			String Commodity = getData("Connect", 1, 23);
			isElementPresent("OCDesc_id").clear();
			isElementPresent("OCDesc_id").sendKeys(Commodity);
			isElementPresent("OCDesc_id").sendKeys(Keys.TAB);
			logs.info("Entered Commodity");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --Scroll Up
			r.keyPress(KeyEvent.VK_TAB);
			jse.executeScript("window.scrollBy(0,250)", "");
			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			// --Total Mileage
			String tmile = isElementPresent("OCTotalMil_id").getAttribute("value");
			logs.info("Total Mileage==" + tmile);

			setData("Connect", 1, 25, pmi);
			setData("Connect", 1, 27, dmi);
			setData("Connect", 1, 29, tmile);

			Thread.sleep(5000);

			// --Click on Create Order button
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			WebElement order = isElementPresent("OCOProcess_id");
			jse.executeScript("arguments[0].scrollIntoView();", order);
			Thread.sleep(5000);
			// act.moveToElement(order).build().perform();
			order = isElementPresent("OCOProcess_id");
			wait1.until(ExpectedConditions.elementToBeClickable(order));
			jse.executeScript("arguments[0].click();", order);
			logs.info("Click on Create Order button");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			Thread.sleep(2000);

			try {
				boolean sameairport = driver.getPageSource()
						.contains("Pickup and Delivery airport are different. Do you want to make it same?");

				if (sameairport == true) {
					logs.info("PopUp message is displayed for Same Airport");
					WebElement Yes = isElementPresent("OCSameApPupYes_xpath");
					wait1.until(ExpectedConditions.elementToBeClickable(Yes));
					jse.executeScript("arguments[0].click();", Yes);
					logs.info("Clicked on YES button of popup");

				}
			} catch (Exception eee) {

			}

			try {
				wait1.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//*[@name=\"NewOrderForm\"]//ul[@id=\"errorid\"]")));
				String ValMsg = isElementPresent("OCValMsg_xpath").getText();
				logs.info("Validation message=" + ValMsg);
				if (ValMsg.contains("Please add atleast one Package.")) {

					// --Total Qty
					isElementPresent("OCTotalQty_id").clear();
					// isElementPresent("OCTotalQty_id").sendKeys(Keys.BACK_SPACE);
					isElementPresent("OCTotalQty_id").sendKeys(Keys.CONTROL, "a");
					isElementPresent("OCTotalQty_id").sendKeys(Keys.BACK_SPACE);
					isElementPresent("OCTotalQty_id").clear();
					isElementPresent("OCTotalQty_id").clear();
					isElementPresent("OCTotalQty_id").sendKeys("01");
					isElementPresent("OCTotalQty_id").sendKeys(Keys.TAB);
					Thread.sleep(2000);
					logs.info("Entered Total Qty");

					// --Weight
					Weight = getData("Connect", 1, 19);
					isElementPresent("OCWeight_id").clear();
					isElementPresent("OCWeight_id").sendKeys(Weight);
					isElementPresent("OCWeight_id").sendKeys(Keys.TAB);
					logs.info("Entered Weight");

					// --Length
					Len = getData("Connect", 1, 20);
					isElementPresent("OCLength_id").clear();
					isElementPresent("OCLength_id").sendKeys(Len);
					isElementPresent("OCLength_id").sendKeys(Keys.TAB);
					logs.info("Entered Length");

					// --Width
					Width = getData("Connect", 1, 21);
					isElementPresent("OCWidth_id").clear();
					isElementPresent("OCWidth_id").sendKeys(Width);
					isElementPresent("OCWidth_id").sendKeys(Keys.TAB);
					logs.info("Entered Width");

					// --Height
					Height = getData("Connect", 1, 22);
					isElementPresent("OCHeight_id").clear();
					isElementPresent("OCHeight_id").sendKeys(Height);
					isElementPresent("OCHeight_id").sendKeys(Keys.TAB);
					logs.info("Entered Height");

					// -- COMMODITY_DROPDOWN

					Select comodity_drpdowns = new Select(isElementPresent("comodity_drpdown_xpath"));
					comodity_drpdowns.selectByIndex(1);
					logs.info("comodity dropdown is selected");
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
					Thread.sleep(2000);

					// --Commodity
					Commodity = getData("Connect", 1, 23);
					isElementPresent("OCDesc_id").clear();
					isElementPresent("OCDesc_id").sendKeys(Commodity);
					isElementPresent("OCDesc_id").sendKeys(Keys.TAB);
					logs.info("Entered Commodity");
					Thread.sleep(5000);

					// --Click on Create Order button
					order = isElementPresent("OCOProcess_id");
					jse.executeScript("arguments[0].scrollIntoView();", order);
					Thread.sleep(2000);
					act.moveToElement(order).build().perform();
					wait1.until(ExpectedConditions.elementToBeClickable(order));
					jse.executeScript("arguments[0].click();", order);
					logs.info("Click on Create Order button");
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
					Thread.sleep(2000);

					try {
						boolean sameairport = driver.getPageSource()
								.contains("Pickup and Delivery airport are different. Do you want to make it same?");

						if (sameairport == true) {
							logs.info("PopUp message is displayed for Same Airport");
							WebElement Yes = isElementPresent("OCSameApPupYes_xpath");
							wait1.until(ExpectedConditions.elementToBeClickable(Yes));
							jse.executeScript("arguments[0].click();", Yes);
							logs.info("Clicked on YES button of popup");

						}
					} catch (Exception eee) {

					}

				}

			} catch (Exception packagee) {
				logs.info("Validation for Package is not displayed");

			}
			// --Scroll down
			/*
			 * r.keyPress(KeyEvent.VK_TAB); jse.executeScript("window.scrollBy(0,250)", "");
			 * Thread.sleep(2000);
			 */

			// --Get the PickUPID
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='lblPickup']/span/b")));

			} catch (Exception WaitOp) {
				try {
					/*
					 * order = isElementPresent("OCOProcess_id");
					 * jse.executeScript("arguments[0].scrollIntoView();", order);
					 * Thread.sleep(2000);
					 */
					/*
					 * order = isElementPresent("OCOProcess_id");
					 * wait1.until(ExpectedConditions.elementToBeClickable(order));
					 * act.moveToElement(order).click().build().perform();
					 * logs.info("Click on Create Order button");
					 * wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")
					 * ));
					 */
					/*
					 * order = isElementPresent("OCOProcess_id");
					 * jse.executeScript("arguments[0].scrollIntoView();", order);
					 * Thread.sleep(2000);
					 */
					/*
					 * order = isElementPresent("OCOProcess_id");
					 * act.moveToElement(order).build().perform();
					 * wait1.until(ExpectedConditions.elementToBeClickable(order));
					 * jse.executeScript("arguments[0].click();", order);
					 * logs.info("Click on Create Order button");
					 * wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")
					 * )); Thread.sleep(5000);
					 */

					try {
						boolean sameairport = driver.getPageSource()
								.contains("Pickup and Delivery airport are different. Do you want to make it same?");

						if (sameairport == true) {
							logs.info("PopUp message is displayed for Same Airport");
							WebElement Yes = isElementPresent("OCSameApPupYes_xpath");
							wait1.until(ExpectedConditions.elementToBeClickable(Yes));
							jse.executeScript("arguments[0].click();", Yes);
							logs.info("Clicked on YES button of popup");

						}
					} catch (Exception eee) {

					}

					try {
						wait.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class=\"modal-dialog\"]")));
						String DialogueContent = isElementPresent("AirSTationH_xpath").getText();
						logs.info("Content of the Dialogue is==" + DialogueContent);

						if (DialogueContent.contains("Quoted Pickup time cannot be greater than Schedule Drop time.")) {
							msg.append("Wrong QPT QDT set==FAIL" + "\n");
							try {
								// --CLick on Yes button
								WebElement YesProceed = isElementPresent("CPUDYesPrc_xpath");
								wait1.until(ExpectedConditions.elementToBeClickable(YesProceed));
								jse.executeScript("arguments[0].click();", YesProceed);
								logs.info("Click on Yes Proceed button");
								wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
								wait.until(ExpectedConditions
										.invisibilityOfElementLocated(By.xpath("//*[@class=\"modal-dialog\"]")));

							} catch (Exception YesBTN) {
								// --CLick on Yes button
								WebElement YesProceed = isElementPresent("EOCont_xpath");
								wait1.until(ExpectedConditions.elementToBeClickable(YesProceed));
								act.moveToElement(YesProceed).click().build().perform();
								logs.info("Click on Yes Proceed button");
								wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

							}

						}

					} catch (Exception e) {
						logs.info("QPT QDT validation not displayed");
						Thread.sleep(5000);
					}
					/*
					 * WebDriverWait wait2 = new WebDriverWait(driver, 40);// wait time
					 * wait2.until(ExpectedConditions
					 * .visibilityOfElementLocated(By.xpath("//*[@class=\"modal-dialog modal-sm\"]")
					 * ));
					 */
				} catch (Exception e) {

				}
			}

			WebElement PickUPID = isElementPresent("OCPickuPID_xpath");
			wait.until(ExpectedConditions.visibilityOf(PickUPID));
			wait1.until(ExpectedConditions.elementToBeClickable(PickUPID));
			String pck = PickUPID.getText();
			System.out.println("Pickup = " + pck);
			logs.info("=====Pickup =" + pck + "=====" + "\n");
			msg.append("=====Pickup =" + pck + "=====" + "\n");

			// --Set PickUPID
			setData("Connect", 1, 32, pck);

			// --Click on Edit Order
			WebElement EditOrder = isElementPresent("OCEditOrder_id");
			wait1.until(ExpectedConditions.elementToBeClickable(EditOrder));
			act.moveToElement(EditOrder).build().perform();
			EditOrder.click();
			logs.info("Clicked on Edit Order");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			Thread.sleep(1500);
			try {
				EditOrder = isElementPresent("OCEditOrder_id");
				act.moveToElement(EditOrder).build().perform();
				EditOrder.click();
			} catch (Exception ee) {

			}
		} catch (Exception e) {
			logs.error(e);
			msg.append("Issue in Create Order for Service==" + ServiceID + "\n");
			getScreenshot(driver, "CreateOrder_" + ServiceID);

			// --ScrollUp
			jse.executeScript("window.scrollBy(0,-250)", "");
			Thread.sleep(2000);
			getScreenshot(driver, "CreateOrder1_" + ServiceID);

			// --ScrollDown
			jse.executeScript("window.scrollBy(0,250)", "");
			Thread.sleep(2000);
			getScreenshot(driver, "CreateOrder2_" + ServiceID);

			System.out.println("Issue in Create Order");
			logs.info("Issue in Create Order");
			getScreenshot(driver, "CreateOrderIssue");
		}

	}

	public void job_cancel(int i) throws Exception {

		// WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));//
		// wait time
		WebDriverWait wait = new WebDriverWait(driver, 20);// wait time
		Actions act = new Actions(driver);
		WebDriverWait wait2 = new WebDriverWait(driver, 10);//

		String Env = storage.getProperty("Env");
		logs.info("Start process for job cancel");
		// msg.append("Start process for job cancel" + "\n");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
		Thread.sleep(1500);

		// --Search the Job
		searchJob(i);

		// --Go to Edit Job tab
		WebElement EditOrTab = isElementPresent("EOEditOrderTab_id");
		act.moveToElement(EditOrTab).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(EditOrTab));
		act.moveToElement(EditOrTab).click().perform();
		logs.info("Click on Edit Order Tab");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
		Thread.sleep(1500);

		// -- select Cancel Job : Cancel/NC
		WebElement cancel_nc_drp_down = isElementPresent("job_type_cancel_id");
		act.moveToElement(cancel_nc_drp_down).build().perform();
		Thread.sleep(1500);
		Select cancel_nc = new Select(isElementPresent("job_type_cancel_id"));
		cancel_nc.selectByVisibleText("CANCEL/NC");
		logs.info("Select job type : CANCEL/NC");
		Thread.sleep(2000);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
		getScreenshot(driver, "Select_canceljob_" + i);

		// --Click on Save Changes

		WebElement Savechanges = isElementPresent("TLSaveChanges_id");
		wait.until(ExpectedConditions.visibilityOf(Savechanges));
		act.moveToElement(Savechanges).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(Savechanges));
		js.executeScript("arguments[0].click();", Savechanges);
		Thread.sleep(5000);
		logs.info("Clicked on Save Changes button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

		// --Click yes for cancel cnfm

		Thread.sleep(2000);
		WebElement job_cance_cnfm = isElementPresent("BTNOk_id");
		job_cance_cnfm.click();
		logs.info("Clicked on ok for cancel job confirmation");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

		// -- enter cancel note

		WebElement cancel_note = isElementPresent("cancel_note_xpath");
		cancel_note.sendKeys("cancel_note_xpath");
		logs.info("Entering Cancellation description");
		getScreenshot(driver, "canceljob_description_" + i);

		// -- Click on ok to final cancel job

		Thread.sleep(1500);
		WebElement job_cancel_cnfm = isElementPresent("BTNOk_id");
		job_cancel_cnfm.click();
		logs.info("Click on Ok for Final Job cancel Confirmation");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
		Thread.sleep(2000);
		getScreenshot(driver, "job_cancel_" + i);

		// -- additinal popup for change ready time after cancellation process
		try {

			WebElement cnfm_popup_redy_time = isElementPresent("redy_time_change_popup_xpath");
			if (cnfm_popup_redy_time.isDisplayed()) {

				WebElement redy_time_change_yes = isElementPresent("TLDPUOK_id");

				js.executeScript("arguments[0].click();", redy_time_change_yes);
				logs.info("Click on No button for change ready time popup");

				// - -Click on SAve changes button

				WebElement save_changes = isElementPresent("TLSaveChanges_id");
				js.executeScript("arguments[0].scrollIntoView(true);", save_changes);
				js.executeScript("arguments[0].click();", save_changes);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				Thread.sleep(1500);
				logs.info("Click on Save Changes button");

			}

		}

		catch (Exception e) {
			// TODO: handle exception
			logs.info("no addidtional popup up is display");
		}

		// -- validate job is cancel or not by reropen and verify it's title

		searchallJob(i);

		String Job_status = isElementPresent("TLStageLable_id").getText();
		logs.info("Fetched Job status is : " + Job_status);
		WebElement cancel_nc_nav = isElementPresent("job_type_cancel_id");
		act.moveToElement(cancel_nc_nav).build().perform();
		getScreenshot(driver, "final_job_status_" + i);
		Thread.sleep(1500);
		getScreenshot(driver, "canceljob_type_" + i);

		String PUID = getData("Connect", 1, 32);
		//msg.append("PickupID=" + PUID + "\n");

		if (Env.equalsIgnoreCase("PROD")) {

			if (Job_status.equalsIgnoreCase("CANCEL")) {
				logs.info("job is cancelled");
				logs.info("job cancel process == PASS");
				getStageName();
				logs.info("Job Cancellation Process == pass");
				msg.append("Order Cancelled=PASS" + "\n");
			}

			// -- re-attempt to cancel job
			else {

				Select cancel_ncs = new Select(isElementPresent("job_type_cancel_id"));
				cancel_ncs.selectByVisibleText("CANCEL/NC");
				logs.info("Select job type : CANCEL/NC");
				Thread.sleep(1500);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				getScreenshot(driver, "Select_canceljob_" + i);
				// --Click on Save Changes
				isElementPresent("TLSaveChanges_id").click();
				logs.info("Clicked on Save Changes button");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				// --Click yes for cancel cnfm

				Thread.sleep(1500);
				WebElement job_cance_cnfm1 = isElementPresent("BTNOk_id");
				job_cance_cnfm1.click();
				logs.info("Clicked on ok for cancel job confirmation");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				// -- enter cancel note

				WebElement cancel_note1 = isElementPresent("cancel_note_xpath");
				cancel_note1.sendKeys("cancel_note_xpath");
				logs.info("Entering Cancellation description");
				getScreenshot(driver, "canceljob_description_" + i);

				// -- Click on ok to final cancel job

				Thread.sleep(1500);
				WebElement job_cancel_cnfm1 = isElementPresent("BTNOk_id");
				job_cancel_cnfm1.click();
				logs.info("Click on Ok for Final Job cancel Confirmation");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				// -- additinal popup for change ready time after cancellation process
				try {

					WebElement cnfm_popup_redy_time = isElementPresent("redy_time_change_popup_xpath");
					if (cnfm_popup_redy_time.isDisplayed()) {

						WebElement redy_time_change_yes = isElementPresent("TLDPUOK_id");

						js.executeScript("arguments[0].click();", redy_time_change_yes);
						logs.info("Click on No button for change ready time popup");

						// - -Click on SAve changes button

						WebElement save_changes = isElementPresent("TLSaveChanges_id");
						js.executeScript("arguments[0].scrollIntoView(true);", save_changes);
						js.executeScript("arguments[0].click();", save_changes);
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
						Thread.sleep(1500);
						logs.info("Click on Save Changes button");

					}

				}

				catch (Exception e) {
					// TODO: handle exception
					logs.info("no addidtional popup up is display");
				}

				open_pickup_frm_tasklog(i);
				// -- validate job is cancel or not by reropen and verify it's title

				String Job_statuses = getStageName();

				WebElement cancel_nc1 = isElementPresent("job_type_cancel_id");
				act.moveToElement(cancel_nc1).build().perform();

				Thread.sleep(1500);
				getScreenshot(driver, "canceljob_type_" + i);

				if (Job_statuses.equalsIgnoreCase("CANCEL")) {
					logs.info("job is cancelled");
					setData("Test_Case", i, 6, Job_status + " -PASS");
					setData("Test_Case", i, 2, "PASS");
				}

				else {

					logs.info("job is not cancelled == Fail");
					setData("Test_Case", i, 6, Job_status + " - FAIL");
					setData("Test_Case", i, 2, "FAIL");
					msg.append("Job cancel == FAIL" + "\n");
				}

			}
		}

		else if (Env.equalsIgnoreCase("STG") || Env.equalsIgnoreCase("TEST")) {

			logs.info("Do not process for job cancelaltion");

		}

	}

	public void open_pickup_frm_tasklog(int i)
			throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 20);// wait time
		Actions act = new Actions(driver);
		WebDriverWait wait2 = new WebDriverWait(driver, 15);// wait time
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click

		try {
			// Go To Operations
			WebElement operations = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_operations")));
			act.moveToElement(operations).click().perform();
			wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			// Go to TaskLog
			WebElement taskLog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_TaskLog")));
			taskLog.click();
			wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			// Enter pickUpID
			String pickupidSeg = getData("Test_Case", i, 6);
			logs.info("Pickup ID use for verification is : " + pickupidSeg);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("txtContains")));
			WebElement tlSearch = isElementPresent("TLSearch_id");
			tlSearch.clear();
			tlSearch.sendKeys(pickupidSeg);
			logs.info("Pickup id entered in search box");

			// Click on search button for pickup search
			WebElement searchButton = isElementPresent("TLSearchButton_id");
			searchButton.click();
			wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

			List<WebElement> totalResult = driver.findElements(By.xpath("//span[@class='dx-checkbox-icon']"));
			int size = totalResult.size();

			if (size > 1) {
				WebElement pickupIdTask = driver
						.findElement(By.xpath("//label[contains(text(),'" + pickupidSeg + "')]"));
				act.moveToElement(pickupIdTask).click().perform();
				logs.info("Appropriate Job selected from multiple jobs");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
			} else {
				logs.info("Multiple jobs not visible for the entered pickup id in taskbar");
			}
		} catch (Exception e) {
			logs.info("Error in opening tasklog: " + e);
		}
	}

	public void searchJob(int i) throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click
		WebDriverWait wait = new WebDriverWait(driver, 15);// wait time

		WebDriverWait wait1 = new WebDriverWait(driver, 10);
		Actions act = new Actions(driver);

		String Env = storage.getProperty("Env");
		try {
			try {
				// Enter JobID#
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtContains")));
				wait1.until(ExpectedConditions.elementToBeClickable(By.id("txtContains")));

				String PUID = getData("Connect", i, 32);
				msg.append("PickupID=" + PUID + "\n");

				isElementPresent("TLSearch_id").clear();
				isElementPresent("TLSearch_id").sendKeys(PUID);
				isElementPresent("TLSearch_id").sendKeys(Keys.TAB);

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				WebElement Search = isElementPresent("TLSearchButton_id");
				wait1.until(ExpectedConditions.elementToBeClickable(Search));
				jse.executeScript("arguments[0].click();", Search);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				try {
					getScreenshot(driver, "H3PJob_After_TenderTo3P");

					List<WebElement> Jobs = driver.findElements(
							By.xpath("//*[contains(@aria-label,'Pickup #,')]//label[@id=\"lblDateTime\"]"));
					for (int job = 0; job < Jobs.size(); job++) {
						String PickupID = Jobs.get(job).getText();
						String PickID = null;

						if (PickupID.startsWith("N")) {
							String[] PickValue = PickupID.split("N");
							PickID = PickValue[1];
						} else if (PickupID.startsWith("F")) {
							String[] PickValue = PickupID.split("F");
							PickID = PickValue[1];
						} else if (PickupID.startsWith("R")) {
							String[] PickValue = PickupID.split("R");
							PickID = PickValue[1];
						}

						logs.info("Searched PickUpID==" + PickID);
						PUID = getData("Sheet1", i, 32);
						if (PickID.equalsIgnoreCase(PUID)) {
							Jobs.get(job).click();
							wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

						}

					}

					/*
					 * // --Click on Job Name WebElement JobName =
					 * isElementPresent("TLH3PJobName_id");
					 * wait1.until(ExpectedConditions.elementToBeClickable(JobName));
					 * JobName.click(); logs.info("Clicked on Job Name");
					 * wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")
					 * ));
					 */
					logs.info("Same job is displayed with 2 status==PASS");
					// msg.append("Same job is displayed with 2 status==PASS" + "\n");

					// --Get StageName
				} catch (Exception eTenderTo3P) {
					logs.info("Same job is not displayed with 2 status");
					// msg.append("Same job is not displayed with 2 status" + "\n");

					// --Get StageName

				}

			} catch (Exception eTasklog) {
				// --Go To Operations
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_operations")));
				WebElement Operations = isElementPresent("Operations_id");
				act.moveToElement(Operations).click().perform();
				logs.info("Clicked on Operations");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath("//*[@class=\"OpenCloseClass dropdown open\"]//ul")));

				// --Go to TaskLog
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_TaskLog")));
				isElementPresent("OpTaskLog_id").click();
				logs.info("Clicked on TaskLog");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				getScreenshot(driver, "TaskLog");

				// Enter JobID#
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				String PUID = getData("Connect", i, 32);

				logs.info("PickUpID=" + PUID + "\n");
				msg.append("\n"+"PickupID=" + PUID + "\n");

				isElementPresent("TLSearch_id").clear();
				isElementPresent("TLSearch_id").sendKeys(PUID);
				isElementPresent("TLSearch_id").sendKeys(Keys.TAB);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				WebElement Search = isElementPresent("TLSearchButton_id");
				wait1.until(ExpectedConditions.elementToBeClickable(Search));
				jse.executeScript("arguments[0].click();", Search);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				try {
					getScreenshot(driver, "H3PJob_After_TenderTo3P");

					List<WebElement> Jobs = driver.findElements(
							By.xpath("//*[contains(@aria-label,'Pickup #,')]//label[@id=\"lblDateTime\"]"));
					for (int job = 0; job < Jobs.size(); job++) {
						String PickupID = Jobs.get(job).getText();
						String[] PickValue = PickupID.split("N");
						String PickID = PickValue[1];
						logs.info("Searched PickUpID==" + PickID);
						if (PickID.equalsIgnoreCase(PUID)) {
							Jobs.get(job).click();
							wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

						}

						/*
						 * // --Click on Job Name WebElement JobName =
						 * isElementPresent("TLH3PJobName_id");
						 * wait1.until(ExpectedConditions.elementToBeClickable(JobName));
						 * JobName.click(); logs.info("Clicked on Job Name");
						 * wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")
						 * ));
						 */
						logs.info("Same job is displayed with 2 status opt relevent == PASS");
						// msg.append("Same job is displayed with 2 status==PASS" + "\n");
					}
					// --Get StageName
				} catch (Exception e) {
					logs.info("Same job is not displayed with 2 status");
					// msg.append("Same job is not displayed with 2 status" + "\n");

					// --Get StageName

				}

			}

			if (Env.equalsIgnoreCase("STG")) {
				setResultData("Result", 22, 4, "PASS");

			}

		} catch (Exception ewait) {
			getScreenshot(driver, "SearchJobError" + i);
			String Error = ewait.getMessage();
			setResultData("Result", 22, 4, "FAIL");
			setResultData("Result", 22, 5, Error);

		}

	}

	public void searchallJob(int i) throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) driver;// scroll,click
		WebDriverWait wait = new WebDriverWait(driver, 15);// wait time

		WebDriverWait wait1 = new WebDriverWait(driver, 10);
		Actions act = new Actions(driver);

		String Env = storage.getProperty("Env");
		try {
			try {

				// --Go To Operations
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_operations")));
				WebElement Operations = isElementPresent("OperationsTab_id");
				act.moveToElement(Operations).build().perform();
				act.moveToElement(Operations).click().perform();
				logs.info("Clicked on Operations");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath("//*[@class=\"OpenCloseClass dropdown open\"]//ul")));

				// --Go to TaskLog
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_TaskLog")));
				isElementPresent("TaskLog_id").click();
				logs.info("Clicked on TaskLog");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				getScreenshot(driver, "TaskLog");

				// --Zoom Out
				jse.executeScript("document.body.style.zoom='90%';");
				Thread.sleep(2000);

				// --Go to Search All Job
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hlkOrderSearch")));
				WebElement SearchAllJob = isElementPresent("TLSearchAllJob_id");
				act.moveToElement(SearchAllJob).build().perform();
				jse.executeScript("arguments[0].click();", SearchAllJob);
				logs.info("Clicked on SearchAllJobs");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("AdvancesSearch")));

				// --Zoom In
				jse.executeScript("document.body.style.zoom='100%';");
				Thread.sleep(2000);

				// --Reset button
				isElementPresent("RLReset_id").click();
				logs.info("Clicked on Reset button");

				// --Set Origin FRom Date
				WebElement OFromDate = isElementPresent("TLOrderRFrom_id");
				OFromDate.clear();
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				logs.info(dateFormat.format(date));
				OFromDate.sendKeys(dateFormat.format(date));
				OFromDate.sendKeys(Keys.TAB);
				logs.info("Entered Origin From Date");

				// --Set Origin To Date
				WebElement OToDate = isElementPresent("TLOrderRTo_id");
				OToDate.clear();
				date = new Date();
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				logs.info(dateFormat.format(date));
				OToDate.sendKeys(dateFormat.format(date));
				OToDate.sendKeys(Keys.TAB);
				logs.info("Entered Origin From Date");

				String PUID = getData("Connect", i, 32);
				isElementPresent("TLSAllJPickup_id").sendKeys(PUID);
				logs.info("Entered PickupID : " + PUID);

				// --Click on Search
				wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSearch")));
				isElementPresent("RLSearch_id").click();
				logs.info("Click on Search button");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				String JobID = "lblJobIdValue_0";

				// ---Select Record
				WebElement Job = driver.findElement(By.id(JobID));
				act.moveToElement(Job).build().perform();
				jse.executeScript("arguments[0].click();", Job);
				logs.info("Clicked on Record");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				jse.executeScript("document.body.style.zoom='100%';");
				Thread.sleep(2000);

			} catch (Exception eTasklog) {
				driver.navigate().refresh();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				logs.info("Page is refreshed");
				// --Go To Operations
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_operations")));
				WebElement Operations = isElementPresent("OperationsTab_id");
				act.moveToElement(Operations).build().perform();
				act.moveToElement(Operations).click().perform();
				logs.info("Clicked on Operations");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath("//*[@class=\"OpenCloseClass dropdown open\"]//ul")));

				// --Go to TaskLog
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("a_TaskLog")));
				isElementPresent("TaskLog_id").click();
				logs.info("Clicked on TaskLog");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				getScreenshot(driver, "TaskLog");

				// --Zoom Out
				jse.executeScript("document.body.style.zoom='90%';");
				Thread.sleep(2000);

				// --Go to Search All Job
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hlkOrderSearch")));
				WebElement SearchAllJob = isElementPresent("TLSearchAllJob_id");
				act.moveToElement(SearchAllJob).build().perform();
				jse.executeScript("arguments[0].click();", SearchAllJob);
				logs.info("Clicked on SearchAllJobs");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("AdvancesSearch")));

				// --Zoom In
				jse.executeScript("document.body.style.zoom='100%';");
				Thread.sleep(2000);

				// --Reset button
				isElementPresent("RLReset_id").click();
				logs.info("Clicked on Reset button");

				// --Set Origin FRom Date
				WebElement OFromDate = isElementPresent("TLOrderRFrom_id");
				OFromDate.clear();
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				logs.info(dateFormat.format(date));
				OFromDate.sendKeys(dateFormat.format(date));
				OFromDate.sendKeys(Keys.TAB);
				logs.info("Entered Origin From Date");

				// --Set Origin To Date
				WebElement OToDate = isElementPresent("TLOrderRTo_id");
				OToDate.clear();
				date = new Date();
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				logs.info(dateFormat.format(date));
				OToDate.sendKeys(dateFormat.format(date));
				OToDate.sendKeys(Keys.TAB);
				logs.info("Entered Origin From Date");

				String PUID = getData("Connect", i, 32);
				isElementPresent("TLSARoutTrackNo_id").sendKeys(PUID);
				logs.info("Entered RouteTrackingID");

				// --Click on Search
				wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSearch")));
				isElementPresent("TLSAllJSearchBtn_id").click();
				logs.info("Click on Search button");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));

				String JobID = "lblJobIdValue_0";

				// ---Select Record
				WebElement Job = isElementPresent("TLSAllJob1stJob_id");
				act.moveToElement(Job).build().perform();
				jse.executeScript("arguments[0].click();", Job);
				logs.info("Clicked on Record");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loaderDiv")));
				jse.executeScript("document.body.style.zoom='100%';");
				Thread.sleep(2000);

			}

		} catch (Exception ewait) {
			getScreenshot(driver, "SearchJobError" + i);
			String Error = ewait.getMessage();

		}

	}

	public String getStageName() {
		WebDriverWait wait = new WebDriverWait(driver, 15);// wait time

		WebDriverWait wait1 = new WebDriverWait(driver, 10);

		// --Get the Stage Name
		WebElement Stage = isElementPresent("EOStageName_id");
		wait.until(ExpectedConditions.visibilityOf(Stage));
		String StageName = Stage.getText();
		System.out.println(StageName);
		logs.info("Stage=" + StageName);
		// msg.append("Stage=" + StageName + "\n");
		return StageName;

	}

}
