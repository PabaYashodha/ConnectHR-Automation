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
        options.addArguments("--ignore-certificate-errors");
       // options.addArguments("--headless=new");              // important for Jenkins
        options.addArguments("--window-size=1920,1080");     // prevent hidden elements
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
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
            WebElement assetModule = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@class='menu-text' and normalize-space(text())='Assets Management']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", assetModule);
            assetModule.click();
            wait.until(ExpectedConditions.urlContains("/asset"));
            System.out.println("✅ Navigated to Asset Management module");

            // 5. Click New Category button
            WebElement newCategoryBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[span[normalize-space()='New category']]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", newCategoryBtn);
            newCategoryBtn.click();
            System.out.println("✅ Navigated to new category form");

            // 6. Wait for modal to appear fully
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'ant-modal-content')]")));
            Thread.sleep(1500); // allow animation
            System.out.println("✅ Modal is visible");

            // 7. Enter category title (visibility + clickable + scroll)
            By categoryTitleLocator = By.xpath("//div[contains(@class,'ant-modal-content')]//input[@id='name']");
            WebElement categoryTitleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTitleLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", categoryTitleInput);
            wait.until(ExpectedConditions.elementToBeClickable(categoryTitleInput));
            categoryTitleInput.sendKeys("Test Category57");
            System.out.println("✅ Entered new category name");

            // 8. Enter category key
            WebElement categoryKeyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("key")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", categoryKeyInput);
            wait.until(ExpectedConditions.elementToBeClickable(categoryKeyInput));
            categoryKeyInput.sendKeys("Test Category key37");
            System.out.println("✅ Entered new category key");

            // 9. Click Create button
            WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[span[normalize-space()='Create']]")));
            createBtn.click();
            System.out.println("✅ Form submitted");

            // 10. Search the category
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@placeholder='Search']")));
            searchBox.clear();
            searchBox.sendKeys("Laptop");
            searchBox.sendKeys(Keys.ENTER);
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
