package ApiDemos;


import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidBasicTest {
    private AndroidDriver<WebElement> driver;
	
    private final String SEARCH_ACTIVITY = ".app.SearchInvoke";
    private final String ALERT_DIALOG_ACTIVITY = ".app.AlertDialogSamples";
    private final String PACKAGE = "io.appium.android.apis";

    @BeforeClass
    public void setUp() throws IOException {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "./app");
        File app = new File(appDir.getCanonicalPath(), "ApiDemos-debug.apk");
		// Apk exists or not?
		if (!app.exists()) {
			System.out.println("Apk doesn't exist!");
		}
		//Configure the startup capabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
		//If browser is empty, use Appium default browser
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		//Support unicode
		capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("resetKeyboard", true);
		//Setup platform and device and android version
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", "Android Emulator");
		capabilities.setCapability("platformVersion", "9");
		//Get app absolute local path
		capabilities.setCapability("app", app.getAbsolutePath());
        driver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test()
    public void testSendKeys() {
        driver.startActivity(new Activity(PACKAGE, SEARCH_ACTIVITY));
        AndroidElement searchBoxEl = (AndroidElement) driver.findElementById("txt_query_prefill");
        searchBoxEl.sendKeys("Hello world!");
        AndroidElement onSearchRequestedBtn = (AndroidElement) driver.findElementById("btn_start_search");
        onSearchRequestedBtn.click();
        AndroidElement searchText = (AndroidElement) new WebDriverWait(driver, 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/search_src_text")));
        String searchTextValue = searchText.getText();
        Assert.assertEquals(searchTextValue, "Hello world!");
    }
}