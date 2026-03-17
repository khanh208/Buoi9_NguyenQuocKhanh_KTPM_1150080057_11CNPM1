package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;

public class LoginTest extends BaseTest {

    private ConfigReader config;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void testSetUp() {
        config = new ConfigReader(getEnv());
        loginPage = new LoginPage(getDriver()).open(config.getBaseUrl());
    }

    @Test
    public void loginSuccessWithValidAccount() {
        InventoryPage inventoryPage = loginPage.login(config.getUsername(), config.getPassword());

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded after successful login.");
    }

    @Test
    public void loginFailureWithInvalidPassword() {
        LoginPage resultPage = loginPage.loginExpectingFailure(config.getUsername(), config.getInvalidPassword());

        Assert.assertTrue(resultPage.isErrorDisplayed(), "Error message should be displayed.");
        Assert.assertTrue(resultPage.getErrorMessage().toLowerCase().contains("epic sadface"));
    }

    @Test
    public void loginFailureWithInvalidUsername() {
        LoginPage resultPage = loginPage.loginExpectingFailure(config.getInvalidUsername(), config.getPassword());

        Assert.assertTrue(resultPage.isErrorDisplayed(), "Error message should be displayed.");
        Assert.assertTrue(resultPage.getErrorMessage().toLowerCase().contains("epic sadface"));
    }

    @Test
    public void loginFailureWithLockedUser() {
        LoginPage resultPage = loginPage.loginExpectingFailure(config.getLockedUsername(), config.getPassword());

        Assert.assertTrue(resultPage.isErrorDisplayed(), "Locked user should see error message.");
        Assert.assertTrue(resultPage.getErrorMessage().toLowerCase().contains("locked out"));
    }
}