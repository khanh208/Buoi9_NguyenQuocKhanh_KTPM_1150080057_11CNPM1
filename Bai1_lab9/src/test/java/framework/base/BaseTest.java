package framework.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import framework.utils.DriverFactory;

public abstract class BaseTest {

    private final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private final ThreadLocal<String> tlEnv = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    protected String getEnv() {
        return tlEnv.get();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({ "browser", "env" })
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("dev") String env
    ) {
        WebDriver driver = DriverFactory.createDriver(browser);
        tlDriver.set(driver);
        tlEnv.set(env);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                captureScreenshot(result.getName());
            }
        } finally {
            WebDriver driver = getDriver();
            if (driver != null) {
                driver.quit();
            }
            tlDriver.remove();
            tlEnv.remove();
        }
    }

    private void captureScreenshot(String testName) {
        WebDriver driver = getDriver();
        if (driver == null) {
            return;
        }

        try {
            Path screenshotDir = Paths.get("target", "screenshots");
            Files.createDirectories(screenshotDir);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

            String safeTestName = testName.replaceAll("[^a-zA-Z0-9-_]", "_");
            Path destination = screenshotDir.resolve(safeTestName + "_" + timestamp + ".png");

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination);

            System.out.println("Screenshot saved: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Cannot save screenshot for test: " + testName);
            e.printStackTrace();
        }
    }
}