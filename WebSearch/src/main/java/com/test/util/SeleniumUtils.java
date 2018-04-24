package com.test.util;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class SeleniumUtils
{

    /**
     * Returns the first element matching the given request and is visible, or
     * null if no element was found
     * 
     * @param driver
     *            - selenium webdriver instance
     * @param request
     *            - xpath request (use helper methods 'partialRequest' and
     *            'fixedRequest')
     * @param expectedTimeout
     *            - expected element waiting timeout -> will throw warning
     * @param definitelyTimeout
     *            - definitely element waiting timeout -> will throw an error
     * @return element or null
     */
    public static WebElement getVisibleElement(String request, WebDriver driver, long expectedTimeout,
            long definitelyTimeout)
    {
        assert definitelyTimeout > expectedTimeout;
        long pollingtime = 500;
        WebElement element = null;
        try
        {
            element = getVisibleElementPrivate(request, driver, expectedTimeout, pollingtime);
        }
        catch (TimeoutException ex1)
        {
            System.out.println("Waited longer than expected to find an element matching the given request: " + request);
            try
            {
                element = getVisibleElementPrivate(request, driver, definitelyTimeout - expectedTimeout, pollingtime);
            }
            catch (TimeoutException ex2)
            {
                throw ex2;
            }
        }
        return element;
    }

    /**
     * ... (uses previous defined private Function)
     * 
     * @param driver
     *            - selenium webdriver instance
     * @param request
     *            - xpath request (use helper methods 'partialRequest' and
     *            fixedRequest'
     * @param expectedTimeout
     *            - expected element waiting timeout -> will throw warning
     * 
     * @return
     */
    public static boolean getVisibleElementBool(String request, WebDriver driver, long expectedTimeout)
    {
        long pollingtime = 500;
        try
        {
            getVisibleElementPrivate(request, driver, expectedTimeout, pollingtime);
        }
        catch (TimeoutException ex1)
        {
            System.out.println(
                    "     INFO: Waited longer than expected to find an element matching the given request: " + request);
            return false;
        }
        return true;
    }

    private static WebElement getVisibleElementPrivate(String request, WebDriver driver, long timeout, long pollingtime)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.pollingEvery(pollingtime, TimeUnit.MILLISECONDS);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(request)));
    }

   
    /**
     * Sends a sequence of keys to the first element matching the request
     * 
     * @param driver
     *            - selenium webdriver instance
     * @param request
     *            - xpath request (use helper methods 'partialRequest' ans
     *            'fixedRequest')
     * @param keysToSend
     *            - sequence of keys to send
     * @param expectedTimeout
     *            - expected element waiting timeout -> will throw a warning
     * @param definitelyTimeout
     *            - definitely element waiting timeout -> will throw an error
     * @return true if the keys were sent, false if not
     */
    public static boolean sendKeysToElement(String request, CharSequence keysToSend, WebDriver driver,
            long expectedTimeout, long definitelyTimeout)
    {
        assert definitelyTimeout > expectedTimeout;
        WebElement element = null;
        try
        {
            element = sendKeysToElement(request, keysToSend, driver, expectedTimeout);
            element.sendKeys(keysToSend);
        }
        catch (TimeoutException ex1)
        {
            System.out.println(
                    "Waited longer than expected to find and sent keys to an element matching the given request: "
                            + request);
            try
            {
                element = sendKeysToElement(request, keysToSend, driver, definitelyTimeout - expectedTimeout);
                element.sendKeys(keysToSend);
            }
            catch (TimeoutException ex2)
            {
                throw ex2;
            }
        }
        return false;
    }

    /**
     * PRESS TAB Button
     * 
     * @param driver
     *            - selenium webdriver instance
     * @param request
     *            - xpath request (use helper methods 'partialRequest' ans
     *            'fixedRequest')
     * @param expectedTimeout
     *            - expected element waiting timeout -> will throw a warning
     * @param definitelyTimeout
     *            - definitely element waiting timeout -> will throw an error
     * @return true if the keys were sent, false if not
     */
    public static boolean pressTabKey(WebDriver driver, String request, long expectedTimeout, long definitelyTimeout)
    {
        assert definitelyTimeout > expectedTimeout;
        WebElement element = null;
        try
        {
            element = driver.findElement(By.xpath(request));
            element.sendKeys(Keys.TAB);
        }
        catch (TimeoutException ex1)
        {
            System.out.println(
                    "Waited longer than expected to find and sent keys to an element matching the given request for Tab key ");
            try
            {
                element = driver.findElement(By.xpath(""));
                element.sendKeys(Keys.TAB);
            }
            catch (TimeoutException ex2)
            {
                throw ex2;
            }
        }
        return false;
    }

    private static WebElement sendKeysToElement(String request, CharSequence keysToSend, WebDriver driver, long timeout)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.pollingEvery(500, TimeUnit.MILLISECONDS);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(request)));
        return element;
    }
    
    /**
     * 
     * While opening or skipping a new page this function should be used for being ready state 
     * 
     * @param driver
     */
    public static void waitForPageLoaded(WebDriver driver)
    {
        ExpectedCondition<Boolean> expectationLoad = new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                        .equals("complete");
            }
        };
        try
        {
            Thread.sleep(250);
            WebDriverWait waitForLoad = new WebDriverWait(driver, 33);
            waitForLoad.until(expectationLoad);
        }
        catch (Throwable error)
        {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }


}
