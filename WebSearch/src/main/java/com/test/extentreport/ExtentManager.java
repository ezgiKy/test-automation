package com.test.extentreport;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager
{
    @Parameters({ "browserIN", "URLIN", "UserIN", "classIN" })
    public static ExtentReports Instance(String browserIN, String URLIN, String classIN) throws Exception
    {
        ExtentReports extent;
        String Path = "./test-output/Suite/ExtentReport" + classIN + ".html";
        System.out.println("End of the test, ExtentReport will be genereated the path : " + Path);
        
        extent = new ExtentReports(Path, false);
        extent.addSystemInfo("Browser", browserIN).addSystemInfo("Environment", URLIN)
        // .addSystemInfo("User Name",UserIN)
        ;

        return extent;
    }

    public static String CaptureScreenShot(WebDriver driver, String ImagesPath)
    {
        System.out.println("CaptureScreen starting");
        TakesScreenshot screenShot = (TakesScreenshot) driver;
        File ssFile = screenShot.getScreenshotAs(OutputType.FILE);
        File destination = new File(ImagesPath + ".jpg");
        try
        {
            FileUtils.copyFile(ssFile, destination);
        }
        catch (IOException e)
        {
            System.out.println("An exception occured while taking ss " + e.getMessage());
        }
        System.out.println("CaptureScreen end");
        return ImagesPath + ".jpg";

    }
}
