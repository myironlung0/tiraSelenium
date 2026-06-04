package pl.seleniumTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;


public class SeleniumTestPlanUZ {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/opt/homebrew/bin/geckodriver");
        // no firefox in PATH
        FirefoxBinary firefoxBinary = new FirefoxBinary(new java.io.File("/Applications/Firefox.app/Contents/MacOS/firefox"));

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(firefoxBinary);

        driver = new FirefoxDriver(options);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void p1() {
        driver.get("http://www.plan.uz.zgora.pl/");
        driver.manage().window().setSize(new Dimension(834, 5233));
        driver.findElement(By.linkText("Plan nauczycieli")).click();
        driver.findElement(By.linkText("B")).click();

// scroll to i przewinac sie do miejsca
        // moze chodzi o rozmiar

        WebElement element = driver.findElement(By.linkText("dr inż. Jacek Bieganowski"));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollBy(0, 300);");

        element.click();
        //driver.findElement(By.linkText("dr inż. Jacek Bieganowski")).click();
        System.out.println("" + driver.findElement(By.cssSelector(".main")).getText());
        assertThat(driver.findElement(By.cssSelector(".main")).getText(), containsString("Seminarium IMEI"));
    }
}
