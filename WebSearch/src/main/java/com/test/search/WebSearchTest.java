package com.test.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.test.extentreport.ExtentManager;
import com.test.util.Mapping;
import com.test.util.SeleniumUtils;

public class WebSearchTest
{

    String browser;
    String URL = "";
    String WebPageLink = "";
    String keyword = "";
    String downloadpath = "";

    private static final long PAGE_LOAD_TIMEOUT = Integer.parseInt(Mapping.PAGE_LOAD_TIMEOUT.toString());
    private static final long ELEMENT_EXPECTED_TIME = Integer.parseInt(Mapping.ELEMENT_EXPECTED_TIME.toString());
    private static final long ELEMENT_LOAD_TIMEOUT = Integer.parseInt(Mapping.ELEMENT_LOAD_TIMEOUT.toString());

    private static WebDriver driver;
    private static ExtentReports extent;
    private static ExtentTest test;

    // maximize browser
    boolean maximized = Boolean.valueOf(Mapping.BROWSER_MAXIMIZED.toString());

    @BeforeTest
    @Parameters({ "browserIN", "URLIN", "KeywordIN", "TestNameIN", "DriverPathIN" })
    public void prepare(String browserIN, String URLIN, String KeywordIN, String TestNameIN, String DriverPathIN)
    {
        System.out.println("Test automation environment is preparing...");
        try
        {
            extent = ExtentManager.Instance(browserIN, URLIN, TestNameIN);
        }
        catch (Exception e)
        {
            System.out.println("ExtenManager Instance method exception " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        extent.assignProject(this.getClass().getName());
        test = extent.startTest("Google Search Test Automation");
        browser = browserIN;
        URL = URLIN;
        WebPageLink = URL;
        keyword = KeywordIN;

        downloadpath = Mapping.Downloadpath_Test.toString();

        // open the desired browser
        if (browser.equals("FIREFOX"))
        {

            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("intl.accept_languages", "en");

            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.download.dir", downloadpath);
            profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/plain"); 

            try
            {
                driver = new FirefoxDriver(profile);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getStackTrace());
            }
        }
        else if (browser.equals("CHROME"))
        {
            System.out.println("chrome if condition, chromedriver.exe file must be here : " + DriverPathIN);
            System.setProperty("webdriver.chrome.driver", DriverPathIN);
            ChromeOptions chromeprofile = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("download.default_directory", downloadpath);
            chromeprofile.setExperimentalOption("prefs", prefs);
            chromeprofile.addArguments("disable-extensions");
            String locale = "en";
            chromeprofile.addArguments("--lang=" + locale);
            chromeprofile.addArguments("window-size=1920,1080");
            try
            {
                driver = new ChromeDriver(chromeprofile);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getStackTrace());
            }
        }
        else if (browser.equals("PHANTOM"))
        {
            DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
            List<String> cliArgs = new ArrayList<String>();
            capabilities.setJavascriptEnabled(true);
            capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgs);
            driver = new PhantomJSDriver(capabilities);
        }
        else if (browser.equals("IEXPLORER"))
        {

            driver = new InternetExplorerDriver();
        }
        else
        {
            System.out.println("No such Browser");
            Assert.assertFalse(true);
        }

        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);

        test.log(LogStatus.INFO, "Test automation preparing completed.");
        System.out.println("Preparing ended.");
    }

    @Test(priority = 0, enabled = true)
    public void openWebPage()
    {
        System.out.println("Web page is opening...");

        // call web-page in browser
        driver.navigate().to(WebPageLink);
        
        if (SeleniumUtils.getVisibleElementBool(Mapping.WelcomePageImage.toString(), driver, ELEMENT_EXPECTED_TIME))
        {
            test.log(LogStatus.PASS, "Web page opened successfully.");
            System.out.println("Web page opened.");
        }
        else
        {
            test.log(LogStatus.ERROR, "Web page couldn't be opened.");
            System.out.println("Web page couldn't be opened..");
        }
    }
    
    @Test(priority = 1, enabled = true, dependsOnMethods = { "openWebPage" })
    public void searchForKeyword()
    {
        System.out.println("Search for keyword starting...");
        
        SeleniumUtils.waitForPageLoaded(driver);      
        
        if( SeleniumUtils.getVisibleElementBool(Mapping.SearchTextBox.toString(), driver, ELEMENT_EXPECTED_TIME)) {
            
            SeleniumUtils.sendKeysToElement(Mapping.SearchTextBox.toString(), keyword, driver, ELEMENT_EXPECTED_TIME, ELEMENT_LOAD_TIMEOUT);
            test.log(LogStatus.INFO, "Keyword is written in searchbox.");
            System.out.println("Keyword is written in searchbox.");
        }
        
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
               
        SeleniumUtils.pressTabKey(driver, Mapping.SearchTextBox.toString(), ELEMENT_EXPECTED_TIME, ELEMENT_LOAD_TIMEOUT);
        test.log(LogStatus.INFO, "Tab Button is pressed.");
        System.out.println("Tab Button is pressed.");
        
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        if( SeleniumUtils.getVisibleElementBool(Mapping.GoButton.toString(), driver, ELEMENT_EXPECTED_TIME)) {
            
            SeleniumUtils.getVisibleElement(Mapping.GoButton.toString(), driver, ELEMENT_EXPECTED_TIME, ELEMENT_LOAD_TIMEOUT).click();
            test.log(LogStatus.INFO, "Go Button is pressed.");
            System.out.println("Go Button is pressed.");
        }
        
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        
        test.log(LogStatus.PASS, "Search completed successfully.");
        System.out.println("Search for keyword completed.");
    }
    
    @Test(priority = 2, enabled = true, dependsOnMethods = { "searchForKeyword" })
    public void takeScreenShotForResult()
    {
        
        System.out.println("SS for listed results starting...");

        String screenShot = ExtentManager.CaptureScreenShot(driver, System.getProperty("user.dir") + "\\test-output\\Suite\\resultSS");

        test.log(LogStatus.PASS, "Screen shot is captured");
        test.log(LogStatus.INFO, "Screen shot is stored in " + screenShot);
        System.out.println("SS captured and stored " + screenShot);
    
    }
    
    @AfterTest(enabled = true)
    public void cleanUp()
    {
        System.out.println("Clean up starting ...");

        test.log(LogStatus.INFO, "Browser closed successfully");
        extent.endTest(test);
        extent.flush();
        extent.close();

        driver.close();

        if (driver != null)
        {
            driver.quit();
        }
        System.out.println("The End.");
    }

}
