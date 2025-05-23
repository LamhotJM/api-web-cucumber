package web.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

/**
 * Page object for the DemoBlaze login workflow.
 */
public class LoginPage {
    private static final String BASE_URL = "https://www.demoblaze.com";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private static final By LOGIN_NAV_BUTTON   = By.id("login2");
    private static final By USERNAME_FIELD     = By.id("loginusername");
    private static final By PASSWORD_FIELD     = By.id("loginpassword");
    private static final By LOGIN_BUTTON       = By.xpath("//button[text()='Log in']");
    private static final By LOGGED_IN_USER     = By.id("nameofuser");
    private static final By LOGOUT_BUTTON      = By.id("logout2");
    private static final By INLINE_ERROR_MSG   = By.xpath("//*[contains(text(),'Please fill out Username and Password.')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, TIMEOUT);
    }

    /**
     * Navigate to the home page and open the login dialog.
     * @return this page object for chaining
     */
    public LoginPage openLoginModal() {
        driver.get(BASE_URL);
        click(LOGIN_NAV_BUTTON);
        waitForVisibility(USERNAME_FIELD);
        return this;
    }

    /**
     * Fill username and password, then submit.
     * @param username your username (may be blank)
     * @param password your password (may be blank)
     * @return this page object for chaining
     */
    public LoginPage login(String username, String password) {
        type(USERNAME_FIELD, username);
        type(PASSWORD_FIELD, password);
        handleAlertIfPresent();
        click(LOGIN_BUTTON);
        return this;
    }

    /**
     * True if the "Welcome, X" label is visible.
     */
    public boolean isUserLoggedIn() {
        try {
            return waitForVisibility(LOGGED_IN_USER).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the displayed username (e.g. "Welcome lamhot"), or empty if not present.
     */
    public String getLoggedInUsername() {
        try {
            return waitForVisibility(LOGGED_IN_USER).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * True if either a JS alert or inline error message is present.
     */
    public boolean isErrorMessageDisplayed() {
        return alertIsPresent() || elementExists(INLINE_ERROR_MSG);
    }

    /**
     * Returns the error text from alert or inline message, or empty if none.
     */
    public String getErrorMessage() {
        if (alertIsPresent()) {
            Alert a = driver.switchTo().alert();
            String text = a.getText();
            a.accept();
            return text;
        }
        try {
            return waitForVisibility(INLINE_ERROR_MSG).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click logout and wait for the login nav button to reappear.
     * @return this page object for chaining
     */
    public LoginPage logout() {
        click(LOGOUT_BUTTON);
        waitForVisibility(LOGIN_NAV_BUTTON);
        return this;
    }

    // ────── private helpers ──────

    /** Clicks and scrolls into view if needed. */
    private void click(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    /** Clears & types into the target field. */
    private void type(By locator, String text) {
        WebElement el = waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    /** Waits until element is visible, then returns it. */
    private WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Returns true if a JS alert is currently present. */
    private boolean alertIsPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** If an alert is present, accept it to avoid blocking. */
    private void handleAlertIfPresent() {
        if (alertIsPresent()) {
            driver.switchTo().alert().accept();
        }
    }

    /** Shorthand check for presence of an inline element without waiting visibly. */
    private boolean elementExists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
