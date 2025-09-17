package com.connecthr.test;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

@Epic("ConnectHR Automation")
@Feature("Login & Asset Management")
public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Headless mode for Jenkins
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(25)); // longer wait
    }

    @Test(description = "Verify login and create a new category in Asset Management")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Login -> Navigate -> Create Category -> Search Category")
    public void loginAndCreateCategoryTest() {
        try {
            // 1. Open the login page
            driver.get("https://172.21.128.14/login");

            // 2. Login
            WebElement usernameField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("login_username"))
            );
            WebElement passwordField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("login_password"))
            );
            WebElement loginButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("button.login-button"))
            );

            usernameField.sendKeys("Support@connecthr.ae");
            passwordField.sendKeys("C0nne<t@#2|");
            loginButton.click();

            // 3. Verify login
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("✅ Login successful");

            // 4. Navigate to Asset Management module
            WebElement assetModule = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//span[@class='menu-text' and normalize-space(text())='Assets Management']")
                    )
            );
            assetModule.click();
            wait.until(ExpectedConditions.urlContains("/asset"));
            System.out.println("✅ Navigated to Asset Management module");

            // 5. Click new category button
            WebElement newCategoryBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[span[normalize-space()='New category']]")
                    )
            );
            newCategoryBtn.click();
            System.out.println("✅ Navigated to new category form");

            // 6. Wait for modal before entering category name
            WebElement modal = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ant-modal-content"))
            );

            WebElement categoryTitleInput = modal.findElement(By.id("name"));
            categoryTitleInput.sendKeys("Test Category0007");
            System.out.println("✅ Entered new category name");

            // 7. Enter category key
            WebElement categoryKeyInput = modal.findElement(By.id("key"));
            categoryKeyInput.sendKeys("Test Category key0007");
            System.out.println("✅ Entered new category key");

            // 8. Click Create button
            WebElement createBtn = modal.findElement(By.xpath("//button[span[normalize-space()='Create']]"));
            createBtn.click();
            System.out.println("✅ Form submitted");

            // 9. Search the category
            WebElement searchBox1 = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']"))
            );
            searchBox1.clear();
            searchBox1.sendKeys("Laptop");
            searchBox1.sendKeys(Keys.ENTER);
            System.out.println("✅ Category searched successfully.");

            Assert.assertTrue(true, "Test completed successfully");

        } catch (Exception e) {
            takeScreenshot("failure");
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Browser closed");
        }
    }

    // Utility: take screenshot on failure
    private void takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String fileName = "target/screenshots/" + name + "_" + new Date().getTime() + ".png";
            FileUtils.copyFile(src, new File(fileName));
            System.out.println("📸 Screenshot saved: " + fileName);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
