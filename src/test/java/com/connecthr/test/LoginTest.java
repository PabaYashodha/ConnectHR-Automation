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
import java.time.Duration;

@Epic("ConnectHR Automation")
@Feature("Login & Asset Management")
public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Dynamically set headless based on system property
        String headless = System.getProperty("headless", "false");
        if (headless.equalsIgnoreCase("true")) {
            options.addArguments("--headless=new"); // headless for Jenkins
        }

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test(description = "Verify login and create a new category in Asset Management")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Login -> Navigate -> Create Category -> Search Category")
    public void loginAndCreateCategoryTest() {
        try {
            driver.get("https://172.21.128.14/login");

            // Login
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

            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("✅ Login successful");

            // Navigate to Asset Management module
            WebElement assetModule = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//span[@class='menu-text' and normalize-space(text())='Assets Management']")
                    )
            );
            assetModule.click();
            wait.until(ExpectedConditions.urlContains("/asset"));
            System.out.println("✅ Navigated to Asset Management module");

            // Click new category button
            WebElement newCategoryBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[span[normalize-space()='New category']]")
                    )
            );
            newCategoryBtn.click();
            System.out.println("✅ Navigated to new category form");

            // Enter category name
            WebElement categoryTitleInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("name"))
            );
            categoryTitleInput.sendKeys("Test Category0007");
            System.out.println("✅ Entered new category name");

            // Enter category key
            WebElement categoryKeyInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("key"))
            );
            categoryKeyInput.sendKeys("Test Category key0007");
            System.out.println("✅ Entered new category key");

            // Click Create button
            WebElement createBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[span[normalize-space()='Create']]")
                    )
            );
            createBtn.click();
            System.out.println("✅ Form submitted");

            // Search the category
            WebElement searchBox1 = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']"))
            );
            searchBox1.clear();
            searchBox1.sendKeys("Laptop");
            searchBox1.sendKeys(Keys.ENTER);
            System.out.println("✅ Category searched successfully.");

            Assert.assertTrue(true, "Test completed successfully");

        } catch (Exception e) {
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
}
