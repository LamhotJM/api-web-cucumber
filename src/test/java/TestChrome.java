import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestChrome {
    public static void main(String[] args) {
        try {
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();
            driver.get("https://www.google.com");
            Thread.sleep(5000);
            driver.quit();
        } catch (Exception e) {
            System.err.println("Failed to run ChromeDriver: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
