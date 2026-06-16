package pl.seleniumTest;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.hamcrest.CoreMatchers.containsString;
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
import java.util.concurrent.TimeoutException;

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
    js.executeScript("window.scrollTo(0,185)");
    js.executeScript("window.scrollTo(0,365)");
    driver.findElement(By.linkText("View Product")).click();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    //reklama
    try {
      WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".continue-prompt-text")));
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    } catch (Exception e) {}

    //sprawdzanie czy przeszliśmy na odpowiednią strone
    assertThat(driver.getCurrentUrl().matches(".*/product_details/\\d+$"), is(true));
    WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantity")));
    driver.findElement(By.id("quantity")).sendKeys(Keys.CONTROL + "a");
    driver.findElement(By.id("quantity")).sendKeys("2");
    driver.findElement(By.id("quantity")).click();
    // sprawdzanie czy poprawnie wpisano ilość
    assertThat(driver.findElement(By.id("quantity")).getAttribute("value"), is("2"));

    driver.findElement(By.cssSelector(".btn.btn-default.cart")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn.btn-success.close-modal.btn-block"))).click();
    driver.findElement(By.xpath("//a[contains(text(),'Products')]")).click();

    // kolejna reklama
    if (!driver.findElements(By.cssSelector(".continue-prompt-text")).isEmpty()) {
      WebElement btn = driver.findElement(By.cssSelector(".continue-prompt-text"));
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    driver.findElement(By.id("search_product")).click();
    driver.findElement(By.id("search_product")).sendKeys("Stylish Dress");
    // sprawdzanie czy dobrze wpisało
    assertThat(driver.findElement(By.id("search_product")).getAttribute("value"), is("Stylish Dress"));
    driver.findElement(By.id("submit_search")).click();
    assertThat(driver.findElement(By.cssSelector(".productinfo.text-center > p")).getText(), is("Stylish Dress"));


    driver.findElement(By.xpath("//a[contains(text(),'Cart')]")).click();
    // sprawdzanie czy przeszliśmy na odpowiednią strone
    assertThat(driver.getCurrentUrl().matches(".*/view_cart$"), is(true));
    // sprawdzanie czy w koszyku znajduje sie poprawna ilość produktów
    assertThat(driver.findElement(By.cssSelector(".cart_quantity")).getText(), is("2"));
    // sprawdzanie czy w koszyku znajduje sie poprawna cena
    assertThat(driver.findElement(By.cssSelector(".cart_total")).getText(), is("Rs. 1000"));
    driver.findElement(By.xpath("//a[contains(text(),'Proceed To Checkout')]")).click();
    List<WebElement> products = driver.findElements(By.cssSelector(".cart_info .cart_description"));
    // sprawdzanie czy w koszyku znajdują się produkty
    assertThat(products.size(), is(1));

    WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-default.check_out")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutBtn);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkoutModal")));
    WebElement closeBtn = driver.findElement(By.cssSelector(".btn.btn-success.close-checkout-modal.btn-block"));
    closeBtn.click();

    assertThat(driver.getCurrentUrl().matches(".*/view_cart"), is(true));
    driver.findElement(By.linkText("Cart")).click();

    js.executeScript("window.scrollTo(0,25.600000381469727)");
    driver.findElement(By.cssSelector(".cart_quantity_delete")).click();
    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cart_info .cart_description")));
    // sprawdzanie czy koszyk jest pusty
    assertThat(driver.findElement(By.id("empty_cart")).getText(), containsString("Cart is empty!"));
  }
}
