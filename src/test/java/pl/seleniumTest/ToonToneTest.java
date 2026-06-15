package pl.seleniumTest;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ToonToneTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  @Before
  public void setUp() {
    System.setProperty("webdriver.gecko.driver", "D:\\Informatyka\\Studia\\TestowanieAplikacji\\geckodriver\\geckodriver.exe");
    FirefoxBinary firefoxBinary = new FirefoxBinary(new java.io.File("C:\\Program Files\\Mozilla Firefox\\firefox.exe"));

    FirefoxOptions options = new FirefoxOptions();
    options.setBinary(firefoxBinary);

    driver = new FirefoxDriver(options);
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void toonTone() throws InterruptedException {
    driver.get("https://toontone.app/characters/spongebob-body/");
    driver.manage().window().maximize();
    driver.findElement(By.cssSelector(".fc-cta-consent > .fc-button-label")).click();
    {
      WebElement element = driver.findElement(By.linkText("Play now"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".char-cta:nth-child(5) > span")).click();
    JavascriptExecutor js = (JavascriptExecutor) driver;

    WebElement hue = driver.findElement(By.id("hue-input"));
    WebElement sat = driver.findElement(By.id("sat-input"));
    WebElement light = driver.findElement(By.id("light-input"));

    js.executeScript("arguments[0].value=304;" + "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));", hue);

    js.executeScript("arguments[0].value=100;" + "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));", sat);

    js.executeScript("arguments[0].value=100;" + "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));", light);

    driver.findElement(By.id("submit-btn")).click();
    Thread.sleep(2000);
  }
}