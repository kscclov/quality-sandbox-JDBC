package org.ibs.managers;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ResourceBundle;
import static org.ibs.utils.PropConst.PATH_CHROME_DRIVER_MAC;


public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            ResourceBundle bundle = ResourceBundle.getBundle("application");
            System.setProperty("webdriver.chrome.driver", bundle.getString(PATH_CHROME_DRIVER_MAC));
            driver = new ChromeDriver();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}


