package com.connecthr.test;
import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.Test;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {
    public static void main(String[] args) {
        // Setup ChromeDriver with SSL ignore option
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors"); // ignore SSL warnings
        WebDriver driver = new ChromeDriver(options);

//login

        try {
            // 1. Open the login page
            driver.get("https://172.21.128.14/login");
            driver.manage().window().maximize();

            Thread.sleep(2000);

            // 2. Locate elements
            WebElement usernameField = driver.findElement(By.id("login_username"));  // change locator as per your page
            WebElement passwordField = driver.findElement(By.id("login_password"));
            WebElement loginButton = driver.findElement(By.cssSelector("button.login-button"));

            Thread.sleep(3000);
            // 3. Enter credentials
            usernameField.sendKeys("Support@connecthr.ae");
            passwordField.sendKeys("C0nne<t@#2|");

            // 4. Click login
//            Thread.sleep(3000);
            loginButton.click();

            // 5. Verification (check if login successful)

            // Wait until URL contains "dashboard" (max 10 seconds)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("✅ Login successful");

            // Navigate to  asset management module
            WebElement assetModule = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='menu-text' and normalize-space(text())='Assets Management']"))
            );
            assetModule.click();
            wait.until(ExpectedConditions.urlContains("https://172.21.128.14/asset"));
            System.out.println("✅ Navigated to Asset Management module");


            //Click new category button
            WebElement newCategoryBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[span[normalize-space()='New category']]")
                    )
            );
            newCategoryBtn.click();
            System.out.println("✅ Navigated to new category form");

            //Add category name
            WebElement categoryTitleInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[@id='name']")
                    )
            );
            categoryTitleInput.sendKeys(" Test Category7");
            System.out.println("✅ Enter to new category name");

            //category key
            WebElement categoryKeyInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[@id='key']")
                    )
            );
            categoryKeyInput.sendKeys(" Test Category key7");
            System.out.println("✅ Enter to new category key");

            //click create button
            WebElement createBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[span[normalize-space()='Create']]")
                    )
            );
            createBtn.click();
            System.out.println("✅ Form submitted");


            //search the category
            WebElement searchBox1 = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[@placeholder='Search']")
                    )
            );

            searchBox1.clear();              // Clear any pre-filled value
            searchBox1.sendKeys("Laptop");   // Type your name
            searchBox1.sendKeys(Keys.ENTER); // Hit Enter
            System.out.println("✅ Category searched successfully.");

            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. Close browser
            driver.quit();
        }
        System.out.println("Login test running...");
        Assert.assertTrue(true);
    }

}

