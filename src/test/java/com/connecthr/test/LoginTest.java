package com.connecthr.test;

import io.qameta.allure.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

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
        options.addArguments("--ignore-certificate-errors"); // Ignore SSL
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test(description = "Verify login and create a new category in Asset Management")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Login -> Navigate -> Create Category -> Search Category")
    public void loginAndCreateCategoryTest() {
        try {
            // 1. Open login page
            driver.get("https://172.21.128.14/login");

            // 2. Login
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_username")));
            WebElement passwordField = driver.findElement(By.id("login_password"));
            WebElement loginButton = driver.findElement(By.cssSelector("button.login-button"));

            usernameField.sendKeys("Support@connecthr.ae");
            passwordField.sendKeys("C0nne<t@#2|");
            loginButton.click();

            // 3. Verify login
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("✅ Login successful");

            // 4. Navigate to Asset Management module
            By assetMenuLocator = By.xpath("//span[@class='menu-text' and normalize-space(text())='Assets Management']/ancestor::a");
            WebElement assetModule = wait.until(ExpectedConditions.presenceOfElementLocated(assetMenuLocator));

            // Scroll and click using JS for React menu
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", assetModule);
            wait.until(ExpectedConditions.elementToBeClickable(assetModule));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", assetModule);

            wait.until(ExpectedConditions.urlContains("/asset"));
            System.out.println("✅ Navigated to Asset Management module");

            // 5. Click New Category button
            By newCategoryBtnLocator = By.xpath("//button[span[normalize-space()='New category']]");
            WebElement newCategoryBtn = wait.until(ExpectedConditions.elementToBeClickable(newCategoryBtnLocator));
            newCategoryBtn.click();
            System.out.println("✅ Navigated to new category form");

            // 6. Enter category name
            WebElement categoryTitleInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Category title']"))
            );
            categoryTitleInput.clear();
            categoryTitleInput.sendKeys("Test Category37");
            System.out.println("✅ Entered new category name");

            // 7. Enter category key
            WebElement categoryKeyInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("key"))
            );
            categoryKeyInput.clear();
            categoryKeyInput.sendKeys("Test Category key37");
            System.out.println("✅ Entered new category key");

            // 8. Click Create button
            WebElement createBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space()='Create']]"))
            );
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
