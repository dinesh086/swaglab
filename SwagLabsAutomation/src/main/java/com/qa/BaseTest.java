package com.qa;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import io.appium.java_client.screenrecording.CanRecordScreen;



public class BaseTest {
	protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	protected static ThreadLocal <Properties> props = new ThreadLocal <Properties>();
	protected static ThreadLocal  <HashMap<String,String>> strings = new ThreadLocal <HashMap<String,String>>();
	protected static ThreadLocal <String> platform = new ThreadLocal <String>();
	protected static ThreadLocal <String> dateTime = new ThreadLocal <String>();
	protected static ThreadLocal <String> deviceName = new ThreadLocal <String>();
	TestUtils utils;
	
	
	
	
	 public AppiumDriver getDriver() {
		  return driver.get();
	  }
	 public void setDriver(AppiumDriver driver2) {
		  driver.set(driver2);
	  }
	 public Properties getProps() {
		  return props.get();
	  }
	 public void setProps(Properties props2) {
		  props.set(props2);
	  }
	 public HashMap<String,String> getStrings() {
		  return strings.get();
	  }
	 public void setStrings(HashMap<String,String> strings2) {
		 strings.set(strings2);
	  }
	 
	 
	  public String getDateTime() {
		  return dateTime.get();
	  }
	  public void setDateTime(String dateTime2) {
		  dateTime.set(dateTime2);
	  }
	  public String getPlatform() {
		  return platform.get();
	  }
	  public void setPlatform(String platform2) {
		  platform.set(platform2);
	  }
	  public String getDeviceName() {
		  return deviceName.get();
	  }
	  public void setDeviceName(String deviceName2) {
		  deviceName.set(deviceName2);
	  }
	
	
	public BaseTest() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	@BeforeMethod
	public void beforeMethod () {
			((CanRecordScreen) getDriver()).startRecordingScreen();
		
	}
	@AfterMethod
	public void afterMethod(ITestResult result) {
		System.out.println("BestTest after method");
		String media= ((CanRecordScreen) getDriver()).stopRecordingScreen();
		
		if (result.getStatus()==2) {
			Map <String, String> params = result.getTestContext()
					.getCurrentXmlTest().getAllParameters()	;
		
			
					String dir = "videos" + File.separator + params.get("platformName")+ "-" + params.get("platformVersion")+ "-" +params.get("deviceName")+ File.separator
							+getDateTime()+ File.separator+result.getTestClass().getRealClass().getSimpleName();

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
	
	
	
	
  @Parameters({"deviceName","udid","platformName","platformVersion","systemPort","chromeDriverPort"})	
  @BeforeTest
  public void beforeTest(String deviceName, String udid,  String platformName,String platformVersion, String chromeDriverPort,String systemPort ) throws Exception {
	  
	  
		  
	  InputStream inputStream = null;
	  InputStream stringis = null; 
	  utils = new TestUtils();
	  setPlatform(platformName);
	  setDeviceName(deviceName);
	  setDateTime(utils.dateTime());	  
	  URL url;
	  Properties props = new Properties();
	  AppiumDriver driver;
	  
	  String strFile ="logs"+ File.separator + platformName + "-" + deviceName;
	  File logFile = new File(strFile);
	  if (!logFile.exists()) {
		  logFile.mkdirs();
		  }
	  
	  ThreadContext.put("ROUTINGKEY", strFile);
	  
	  try {
		  
		  props = new Properties();
		  String propFileName ="config.properties";
		  String xmlFileName = "strings/strings.xml";		  
		  inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		  props.load(inputStream);		  
		  setProps(props);
		  
		  
		  stringis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
		  setStrings(utils.parseStringXML(stringis));		  
		  DesiredCapabilities cap = new DesiredCapabilities();
		  cap.setCapability("deviceName", deviceName);
		  cap.setCapability("udid", udid);
		  cap.setCapability("platformName", platformName);
		  cap.setCapability("platformVersion", platformVersion);
		  url = new URL(props.getProperty("appiumURL"));
		  
			switch(platformName) {			
			case "Android":
				cap.setCapability("appPackage", props.getProperty("androidAppPackage"));
				cap.setCapability("appActivity", props.getProperty("androidAppActivity"));
				cap.setCapability("systemPort", systemPort);
				cap.setCapability("chromeDriverPort", chromeDriverPort);
				
				//URL appUrl= getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));
				//String androidappUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
				//cap.setCapability("app", androidappUrl); to install the application
				
				driver = new AndroidDriver(url, cap);
				break;
			case"iOS":	
				cap.setCapability("automationNAme", props.getProperty("iOSAutomationName"));
				String iOSappUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
				//cap.setCapability("app", iOSappUrl); to install the application
				
				driver = new IOSDriver(url, cap);
				break;
			default:
				throw new Exception("invalid platfrom!- "+ platformName);
			}
			
			setDriver(driver);
									
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
	  WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.WAIT);
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
  public void click(MobileElement e, String msg) {	  
	  waitForVisibility(e);	
	  //utils.log().info(msg);
	  ExtentReport.getTest().log(Status.INFO, msg);
	  e.click();
  }
  
  
  public void sendKeys(MobileElement e, String txt)  {	  
	 waitForVisibility(e);	
	  e.sendKeys(txt);
	  
  }
  public void sendKeys(MobileElement e, String txt, String msg) {
	  waitForVisibility(e);
	  utils.log().info(msg);
	  ExtentReport.getTest().log(Status.INFO, msg);
	  e.sendKeys(txt);
  }
  

  
  public void closeApp(){
	  ((InteractsWithApps) getDriver()).closeApp();
  }
  public void launchApp(){
	  ((InteractsWithApps) getDriver()).launchApp();
  }
  
  public MobileElement scrollToElement () {
	
	  return (MobileElement) ((FindsByAndroidUIAutomator) getDriver()).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView("
						+ "new UiSelector().description(\"test-Price\"));");
	  
			  
	  
  }
 
  public void iOSScrollToElement() {
	  RemoteWebElement element = (RemoteWebElement)getDriver().findElement(By.name("test-ADD TO CART"));
	  String elementID = element.getId();
	  HashMap<String, String> scrollObject = new HashMap<String, String>();
	  scrollObject.put("element", elementID);
//	  scrollObject.put("direction", "down");
//	  scrollObject.put("predicateString", "label == 'ADD TO CART'");
//	  scrollObject.put("name", "test-ADD TO CART");
	  scrollObject.put("toVisible", "sdfnjksdnfkld");
	  getDriver().executeScript("mobile:scroll", scrollObject);
  }

  public String getAttribute(MobileElement e, String attribute)  {	  
	  waitForVisibility(e);	
	  return e.getAttribute(attribute);
	  
  }
  public String getText(MobileElement e, String msg) {
	  String txt = null;
	  
	  switch(getPlatform()) {
	  case "Android":
		  txt = getAttribute(e, "text");
		  	break;	
	  case "iOS":
		  txt =  getAttribute(e, "label");
			break;	
	  }
	  utils.log().info(msg+txt);
	  ExtentReport.getTest().log(Status.INFO, msg);
	  return txt;
  }
  @AfterTest
  public void afterTest() {
	  getDriver().quit();
	  
  }

}
