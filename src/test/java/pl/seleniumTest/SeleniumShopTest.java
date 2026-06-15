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

import java.time.Duration;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumShopTest {
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
  public void seleniumShop() {
    driver.get("https://automationexercise.com/");
    driver.manage().window().maximize();
    driver.findElement(By.cssSelector(".fc-cta-consent > .fc-button-label")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Products')]")).click();
    js.executeScript("window.scrollTo(0,183.1999969482422)");
    js.executeScript("window.scrollTo(0,363.20001220703125)");
    driver.findElement(By.linkText("View Product")).click();

    if (!driver.findElements(By.cssSelector(".continue-prompt-text")).isEmpty()) {
      driver.findElement(By.cssSelector(".continue-prompt-text")).click();
    }

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantity")));
    driver.findElement(By.id("quantity")).sendKeys(Keys.CONTROL + "a");
    driver.findElement(By.id("quantity")).sendKeys("2");
    driver.findElement(By.id("quantity")).click();
    driver.findElement(By.cssSelector(".btn.btn-default.cart")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("View Cart"))).click();

    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Proceed To Checkout"))).click();
    driver.findElement(By.cssSelector(".btn.btn-success.close-checkout-modal.btn-block")).click();
    driver.findElement(By.linkText("Cart")).click();
    {
      WebElement element = driver.findElement(By.linkText("Cart"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    js.executeScript("window.scrollTo(0,25.600000381469727)");
    driver.findElement(By.cssSelector(".fa.fa-times")).click();
  }
}
