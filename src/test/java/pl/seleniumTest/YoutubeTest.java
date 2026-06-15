package pl.seleniumTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class YoutubeTest {
    private WebDriver driver;
    JavascriptExecutor js;

    private static final String URL = "https://www.youtube.com/";

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/opt/homebrew/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();

        driver = new FirefoxDriver(options);

        js = (JavascriptExecutor) driver;
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    // zamyka cookies
    private void preparePage() {
        driver.get(URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        <div class="ytSpecButtonShapeNextButtonTextContent"><span style="" class="ytAttributedStringHost ytAttributedStringWhiteSpaceNoWrap" role="text">Odrzuć wszystko</span></div>
        WebElement rejectButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Nie wyrażaj zgody na wykorzystywanie plików cookie i innych danych do opisanych celów']")));
        rejectButton.click();
//        cssSelector nie umie szukać po treści tekstu
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[text()='Odrzuć wszystko']")));
    }

    private void skipAd(){
        try {
            WebElement skipAd = new WebDriverWait(driver, Duration.ofSeconds(35)).until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ytp-skip-ad-button")));
            skipAd.click();
        } catch (TimeoutException e) {
            System.out.println("No skip button/no ad");
        }
        new WebDriverWait(driver, Duration.ofSeconds(40)).until(driver -> !driver.findElement(By.id("movie_player"))
                        .getAttribute("class")
                        .contains("ad-showing"));

    }


    @Test
    void shouldCloseCookies(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement rejectButton = driver.findElement(By.xpath("//button[@aria-label='Nie wyrażaj zgody na wykorzystywanie plików cookie i innych danych do opisanych celów']"));
        WebElement rejectButton = driver.findElement(By.cssSelector("button[aria-label='Nie wyrażaj zgody na wykorzystywanie plików cookie i innych danych do opisanych celów']"));
        assertFalse(rejectButton.isDisplayed());
    }

    @Test
    void shouldSearchForVideo() {
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement searchBox = driver.findElement(By.name("search_query"));
        searchBox.sendKeys("Selenium tutorial");
        searchBox.submit();

        wait.until(ExpectedConditions.titleContains("Selenium tutorial"));
        assertTrue(driver.getTitle().contains("Selenium tutorial"));
    }

    @Test
    void shouldOpenShorts(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
        wait.until(ExpectedConditions.urlContains("/shorts"));
        assertTrue(driver.getCurrentUrl().contains("/shorts"));

    }

//    // POPRAW
//    @Test
//    void shouldScrollThroughShorts(){
//        preparePage();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
//        wait.until(ExpectedConditions.urlContains("/shorts"));
//
//        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Następny film']")));
//
//        String previousUrl = driver.getCurrentUrl();
//
//        WebElement element = driver.findElement(By.cssSelector("#navigation-button-down .ytSpecTouchFeedbackShapeFill"));
//        new Actions(driver).moveToElement(element).click().perform();
//
//        wait.until(ExpectedConditions.not(ExpectedConditions.urlMatches(previousUrl)));
//        assertNotEquals(previousUrl, driver.getCurrentUrl());
//    }

    @Test
    void shouldPauseShort(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
        wait.until(ExpectedConditions.urlContains("/shorts"));

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Następny film']")));

        WebElement video = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#shorts-player .video-stream")));
        video.click();

        // czy ma klase paused-mode
        WebElement player = driver.findElement(By.id("shorts-player"));
        assertTrue(player.getAttribute("class").contains("paused-mode"));
    }

    @Test
    void shouldOpenComments(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
        wait.until(ExpectedConditions.urlContains("/shorts"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Następny film']")));

        WebElement commentsButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label,'komentarzy')]")));
        commentsButton.click();

        WebElement commentsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("yt-formatted-string#title-text[title='Komentarze']")));
        assertTrue(commentsTitle.isDisplayed());
    }

    @Test
    void shouldGoBackToMainPage(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
        wait.until(ExpectedConditions.urlContains("/shorts"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Następny film']")));

        driver.findElement(By.id("logo-icon")).click();
        wait.until(ExpectedConditions.urlToBe("https://www.youtube.com/"));
        assertEquals("https://www.youtube.com/", driver.getCurrentUrl());
    }

//    @Test
//    void shouldForwardInShort(){
//        preparePage();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
//        wait.until(ExpectedConditions.urlContains("/shorts"));
//        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Następny film']")));
//
//
//        WebElement progressBar = wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.cssSelector(".ytProgressBarLineProgressBarBackground")
//        ));
//
//        // kliknij w prawą część paska - przewiń do przodu
//        int width = progressBar.getSize().getWidth();
//        new Actions(driver)
//                .moveToElement(progressBar, width / 3, 0) // kliknij w 1/3 paska
//                .click()
//                .perform();
//
//        // sprawdź czy film jest w trakcie odtwarzania (nie na początku)
//        Boolean isPaused = (Boolean) js.executeScript(
//                "return document.querySelector('.video-stream').paused;"
//        );
//        assertFalse(isPaused);
//    }

//    @Test
//    void shouldScrollThroughShorts2()throws InterruptedException{
//        preparePage();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//
//        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
//        wait.until(ExpectedConditions.urlContains("/shorts"));
//
//        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Następny film']")));
//
//        String previousUrl = driver.getCurrentUrl();
//        System.out.println("URL przed: " + previousUrl);
//
//        WebElement element = driver.findElement(By.cssSelector("button[aria-label='Następny film']"));
//
//        System.out.println("Element widoczny: " + element.isDisplayed());
//        System.out.println("Element lokalizacja: " + element.getLocation());
//        Thread.sleep(2000);
//
//        new Actions(driver).moveToElement(element).click().perform();
//
//        Thread.sleep(2000);
//        System.out.println("URL po: " + driver.getCurrentUrl());
//    }

//    @Test
//    void shouldScrollThroughShorts3()  {
//        preparePage();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        driver.findElement(By.cssSelector("a[title='Shorts']")).click();
//        wait.until(ExpectedConditions.urlContains("/shorts"));
//
//        wait.until(driver -> driver.getCurrentUrl().matches(".*shorts/[a-zA-Z0-9_-]+$"));
//
//        String previousUrl = driver.getCurrentUrl();
//        System.out.println("URL przed: " + previousUrl);
//
//        new Actions(driver).scrollByAmount(0, 1000).perform();
//
//        wait.until(ExpectedConditions.not(
//                ExpectedConditions.urlToBe(previousUrl)
//        ));
//
//        System.out.println("URL po: " + driver.getCurrentUrl());
//        assertNotEquals(previousUrl, driver.getCurrentUrl());
//    }
//


    @Test
    void shouldSkipAdIfPresent(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement searchBox = driver.findElement(By.name("search_query"));
        searchBox.sendKeys("Art of dying");
        searchBox.submit();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-video-renderer")));
        driver.findElement(By.cssSelector("ytd-video-renderer a#video-title")).click();

        wait.until(ExpectedConditions.urlContains("/watch"));

        skipAd();

        assertTrue(driver.findElements(By.cssSelector(".ytp-skip-ad-button")).isEmpty());
    }

    @Test
    void shouldClickFirstSearchResultAndForwardByPressingKey(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement searchBox = driver.findElement(By.name("search_query"));
        searchBox.sendKeys("Selenium tutorial");
        searchBox.submit();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-video-renderer")));

        // kliknij w pierwszy wynik
        WebElement video = driver.findElement(By.cssSelector("ytd-video-renderer a#video-title"));
        video.click();

        wait.until(ExpectedConditions.urlContains("/watch"));
        assertTrue(driver.getCurrentUrl().contains("/watch"));

        WebElement videoStream = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".video-stream")));

        skipAd();

        WebElement progressBar = driver.findElement(By.cssSelector(".ytp-progress-bar"));
        String timeBefore = progressBar.getAttribute("aria-valuenow");

        videoStream.sendKeys(Keys.ARROW_RIGHT);

        wait.until(driver -> !driver.findElement(By.cssSelector(".ytp-progress-bar")).getAttribute("aria-valuenow").equals(timeBefore));
        String timeAfter = progressBar.getAttribute("aria-valuenow");

        assertTrue(Integer.parseInt(timeAfter) > Integer.parseInt(timeBefore));

    }

    @Test
    void shouldSetPlaybackSpeedTo2x(){
        preparePage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement searchBox = driver.findElement(By.name("search_query"));
        searchBox.sendKeys("10's pantera");
        searchBox.submit();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-video-renderer")));

        WebElement video = driver.findElement(By.cssSelector("ytd-video-renderer a#video-title"));
        video.click();

        wait.until(ExpectedConditions.urlContains("/watch"));
        assertTrue(driver.getCurrentUrl().contains("/watch"));

        WebElement videoStream = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".video-stream")));

        skipAd();

        driver.findElement(By.cssSelector(".ytp-settings-button")).click();

        // predkosc odtwarzania
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'ytp-menuitem-label') and text()='Prędkość odtwarzania']"))).click();

        // wybierz 2x
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'ytp-variable-speed-panel-preset-button')]//span[text()='2,0']"))).click();

        Number speed = (Number) js.executeScript("return document.querySelector('.video-stream').playbackRate;");
        assertEquals(2.0, speed.doubleValue());
    }
}
