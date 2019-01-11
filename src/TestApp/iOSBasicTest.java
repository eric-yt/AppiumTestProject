package TestApp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class iOSBasicTest {
	private IOSDriver<WebElement> driver;
	
	@BeforeTest
    public void setUp() throws IOException {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "./app");
        File app = new File(appDir.getCanonicalPath(), "TestApp.app.zip");

        String deviceName = System.getenv("IOS_DEVICE_NAME");
        String platformVersion = System.getenv("IOS_PLATFORM_VERSION");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName == null ? "iPhone X" : deviceName);
        capabilities.setCapability("platformVersion", platformVersion == null ? "11.4" : platformVersion);
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("automationName", "XCUITest");
        driver = new IOSDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
    }
	
	@AfterTest
    public void tearDown() {
        driver.quit();
    }
	
	@Test
    public void testSendKeysToInput () {
        // Find TextField input element
        String textInputId = "TextField1";
        IOSElement textViewsEl = (IOSElement) new WebDriverWait(driver, 30)
                .until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId(textInputId)));

        // Check that it doesn't have a value
        String value = textViewsEl.getAttribute("value");
        Assert.assertEquals(value, null);

        // Send keys to that input
        textViewsEl.sendKeys("Hello World!");

        // Check that the input has new value
        value = textViewsEl.getAttribute("value");
        Assert.assertEquals(value, "Hello World!");
    }

	
}
