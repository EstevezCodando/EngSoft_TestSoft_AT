package br.com.infnet.at.atseleniumex4.base;

import br.com.infnet.at.atseleniumex4.extensions.ScreenshotOnFailureExtension;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;

public abstract class BaseTest {

    protected static WebDriver driver;

    @RegisterExtension
    static ScreenshotOnFailureExtension screenshotExtension =
            new ScreenshotOnFailureExtension();

    @BeforeEach
    void setUp() {
        try {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } catch (Exception e) {
            System.setProperty("webdriver.edge.driver", "C:\\webdrivers\\msedgedriver.exe");
            driver = new EdgeDriver();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
        driver.manage().window().maximize();
        driver.get("https://automationexercise.com");
    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
