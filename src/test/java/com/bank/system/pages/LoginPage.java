package com.bank.system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private WebDriver driver;

    // Locators
    private By usernameField = By.id("username"); // Assumed ID, update after checking HTML
    private By passwordField = By.id("password"); // Assumed ID
    private By loginButton = By.xpath("//button[@type='submit']"); // Generic xpath

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void enterUsername(String username) {
        WebElement userElement = driver.findElement(usernameField);
        userElement.clear();
        userElement.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passElement = driver.findElement(passwordField);
        passElement.clear();
        passElement.sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
