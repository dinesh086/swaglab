package com.qa;

import org.testng.annotations.Test;

import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import io.appium.java_client.screenrecording.CanRecordScreen;

public class BaseTest1 {
	protected static AppiumDriver driver;
	protected static Properties props;
	protected static String dateTime;
	protected static HashMap<String,String> strings = new HashMap<String,String>();
	InputStream inputStream;
	InputStream stringis;
	TestUtils utils;
	public BaseTest1() {
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}
	
	@BeforeMethod
	public void beforeMethod () {
		System.out.println("BestTest before method");
		((CanRecordScreen) driver).startRecordingScreen();
		
	}
	@AfterMethod
	public void afterMethod(ITestResult result) {
		System.out.println("BestTest after method");
		String media= ((CanRecordScreen) driver).stopRecordingScreen();
		
		if (result.getStatus()==2) {
			Map <String, String> params = result.getTestContext()
					.getCurrentXmlTest().getAllParameters()	;
		
			
					String dir = "videos" + File.separator + params.get("platformName")+ "-" + params.get("platformVersion")+ "-" +params.get("deviceName")+ File.separator+dateTime
					+ File.separator+result.getTestClass().getRealClass().getSimpleName();

		File videoDir = new File(dir);
		if (!videoDir.exists()) {
			videoDir.mkdirs();
			
		}
		try {
			FileOutputStream stream = new FileOutputStream(videoDir+File.separator+result.getName()+".mp4");
		try {
			stream.write(Base64.decodeBase64(media));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
	
	
	
	}
	
	
	
	
  @Parameters({"deviceName","udid","platformName","platformVersion"})	
  @BeforeTest
  public void beforeTest(String deviceName, String udid,  String platformName,String platformVersion) throws Exception {
	  utils = new TestUtils();
	  dateTime = utils.dateTime();	  
	  URL url;
	  try {
		  
		  props = new Properties();
		  String propFileName ="config.properties";
		  String xmlFileName = "strings/strings.xml";		  
		  inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		  props.load(inputStream);
		  stringis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
		  strings = utils.parseStringXML(stringis);		  
		  DesiredCapabilities cap = new DesiredCapabilities();
		  cap.setCapability("deviceName", deviceName);
		  cap.setCapability("udid", udid);
		  cap.setCapability("platformName", platformName);
		  cap.setCapability("platformVersion", platformVersion);
		  
			switch(platformName) {			
			case "Android":
				cap.setCapability("appPackage", props.getProperty("androidAppPackage"));
				cap.setCapability("appActivity", props.getProperty("androidAppActivity"));
				//URL appUrl= getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));
				//String androidappUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
				//cap.setCapability("app", androidappUrl); to install the application
				url = new URL(props.getProperty("appiumURL"));
				driver = new AndroidDriver<MobileElement>(url, cap);
				break;
			case"iOS":	
				cap.setCapability("automationNAme", props.getProperty("iOSAutomationName"));
				String iOSappUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
				//cap.setCapability("app", iOSappUrl); to install the application
				url = new URL(props.getProperty("appiumURL"));
				driver = new IOSDriver<MobileElement>(url, cap);
				break;
			default:
				throw new Exception("invalid platfrom!- "+ platformName);
			}
									
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	  finally {
		  if(inputStream != null) {
			  inputStream.close();
		  }
		  if(stringis != null) {
			  stringis.close();
		  }
	  }
	  
  }

  public void waitForVisibility(MobileElement e) {
	  WebDriverWait wait = new WebDriverWait(driver, TestUtils.WAIT);
	  wait.until(ExpectedConditions.visibilityOf(e));
  }
  
  public void clear(MobileElement e) {	  
	  waitForVisibility(e);	
	  e.clear();
  }
  public void click(MobileElement e) {	  
	  waitForVisibility(e);	
	  e.click();
  }
  
  
  public void sendKeys(MobileElement e, String txt)  {	  
	 waitForVisibility(e);	
	  e.sendKeys(txt);
	  
  }
  
  public String getAttribute(MobileElement e, String attribute)  {	  
	  waitForVisibility(e);	
	  return e.getAttribute(attribute);
	  
  }
  
  public void closeApp(){
	  ((InteractsWithApps) driver).closeApp();
  }
  public void launchApp(){
	  ((InteractsWithApps) driver).launchApp();
  }
  
  public MobileElement scrollToElement () {
	
	  return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView("
						+ "new UiSelector().description(\"test-Price\"));");
	  
			  
	  
  }
 
  public AppiumDriver getDriver() {
	  return driver;
  }
  
  public String getDateTime() {
	  return dateTime;
  }
  
  public String getText(MobileElement e) {
	  switch("Android") {
	  case "Android":
		  return getAttribute(e, "text");		
	  case "iOS":
		  return getAttribute(e, "label");		 
	  }
	  return null;
  }
  @AfterTest
  public void afterTest() {
	  driver.quit();
	  
  }

}
