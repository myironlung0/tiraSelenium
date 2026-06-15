package pl.seleniumTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RezerwacjeTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    private static final String URL = "https://rezerwacja.zielona-gora.pl/";

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/opt/homebrew/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();

        driver = new FirefoxDriver(options);

        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    // zamyka cookies
    private void preparePage() {
        driver.get(URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(driver -> driver.getPageSource().contains("multiselect"));
        driver.findElement(By.className("close")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("cookies")));
    }

    @Test
    public void pageShouldLoadAndTitleIsCorrect(){
        driver.get(URL);
        String title = driver.getTitle();

        assertEquals("Biuro Rejestracji Pojazdów i Praw Jazdy Urzędu Miasta Zielona Góra", title);

    }

    @Test
    void shouldOpenReservationCategory(){
        preparePage();

        driver.findElement(By.className("multiselect__tags")).click();

        // <span>Rejestracja - nowy pojazd dotychczas niezarejestrowany</span>
        WebElement option = driver.findElement(By.xpath("//span[text()='Rejestracja - nowy pojazd dotychczas niezarejestrowany']"));
        assertTrue(option.isDisplayed());
    }

    // zmienia miesiac, wybiera dzien i godzine
    @Test
    void shouldChangeMonthAndChooseDayAndHourAfterDisplayingCalendar(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.className("multiselect__tags")).click();

        // <span>Rejestracja - nowy pojazd dotychczas niezarejestrowany</span>
        WebElement option = driver.findElement(By.xpath("//span[text()='Rejestracja pojazdu sprowadzonego z zagranicy']"));
        option.click();

        WebElement calendar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("calendar-days-list")));

        String currentMonth = driver.findElement(By.className("calendar-nav-month")).getText();

        driver.findElement(By.className("calendar-nav-next")).click();
        wait.until(driver -> !driver.findElement(By.className("calendar-nav-month")).getText().equals(currentMonth));

        String month = driver.findElement(By.className("calendar-nav-month")).getText();

        List<WebElement> days = driver.findElements(By.cssSelector(".calendar-days-list-cell.is-open.is-valid"));

        assertTrue(calendar.isDisplayed());
        assertNotEquals(currentMonth, month);
        assertFalse(days.isEmpty());

        days.get(0).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("hours-list")));
        List<WebElement> hours = driver.findElements(By.cssSelector(".hours-list-item"));

        assertFalse(hours.isEmpty());

        hours.get(0).click();
    }


    @Test
    void shouldFillOwnerAndVehicleData(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //wyboer dnia i godz
        driver.findElement(By.className("multiselect__tags")).click();

        WebElement option = driver.findElement(By.xpath("//span[text()='Odbiór dowodu rejestracyjnego']"));
        option.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("calendar-days-list")));

        List<WebElement> days = driver.findElements(By.cssSelector(".calendar-days-list-cell.is-open.is-valid"));
        days.get(0).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("hours-list")));
        List<WebElement> hours = driver.findElements(By.cssSelector(".hours-list-item"));
        hours.get(0).click();

        // ===== FORMULARZ =====

//        <div class="field field-3241 is-required">
//          <label>Dane właściciela pojazdu</label>
//          <input>
//      </div>

        WebElement ownerField = driver.findElement(By.cssSelector(".field-3241 input"));
        ownerField.sendKeys("Max Verstappen");

//        <div data-v-087b5080="" class="field field-3186 is-required">
        WebElement vinField = driver.findElement(By.cssSelector(".field-3186 input"));
        vinField.sendKeys("ABC123456789");

        assertEquals("Max Verstappen", ownerField.getAttribute("value"));
        assertEquals("ABC123456789", vinField.getAttribute("value"));

        driver.findElement(By.className("submit-button")).click();
        wait.until(ExpectedConditions.textToBe(By.className("bookero-plugin-header"), "Podsumowanie"));
        String podsumowanie = driver.findElement(By.className("bookero-plugin-header")).getText();
        assertEquals("Podsumowanie", podsumowanie);
    }

    @Test
    void shouldGoBackToReservation() throws InterruptedException{
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //wyboer dnia i godz
        driver.findElement(By.className("multiselect__tags")).click();

        WebElement option = driver.findElement(By.xpath("//span[text()='Odbiór dowodu rejestracyjnego']"));
        option.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("calendar-days-list")));

        List<WebElement> days = driver.findElements(By.cssSelector(".calendar-days-list-cell.is-open.is-valid"));
        days.get(0).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("hours-list")));
        List<WebElement> hours = driver.findElements(By.cssSelector(".hours-list-item"));
        hours.get(0).click();

        // ===== FORMULARZ =====
        WebElement ownerField = driver.findElement(By.cssSelector(".field-3241 input"));
        ownerField.sendKeys("Max Verstappen");
        WebElement vinField = driver.findElement(By.cssSelector(".field-3186 input"));
        vinField.sendKeys("ABC123456789");

        driver.findElement(By.className("submit-button")).click();
        wait.until(ExpectedConditions.textToBe(By.className("bookero-plugin-header"), "Podsumowanie"));
        Thread.sleep(2000);
        driver.findElement(By.className("back-button")).click();
        Thread.sleep(2000);
        wait.until(ExpectedConditions.textToBe(By.className("bookero-plugin-header"), "Zarezerwuj termin"));

        String zarezerwujTermin = driver.findElement(By.className("bookero-plugin-header")).getText();
        assertEquals("Zarezerwuj termin", zarezerwujTermin);
    }
}


