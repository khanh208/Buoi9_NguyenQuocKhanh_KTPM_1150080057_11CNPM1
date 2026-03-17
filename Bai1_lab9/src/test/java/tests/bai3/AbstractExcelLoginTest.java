package tests.bai3;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelRowData;

public abstract class AbstractExcelLoginTest extends BaseTest implements ITest {

    protected ConfigReader config;
    protected LoginPage loginPage;
    protected String testName;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        config = new ConfigReader(getEnv());
        loginPage = new LoginPage(getDriver()).open(config.getBaseUrl());
    }

    protected void executeLoginCase(ExcelRowData rowData) {
        this.testName = rowData.getDescription();

        if (rowData.isSuccessCase()) {
            InventoryPage inventoryPage = loginPage.login(rowData.getUsername(), rowData.getPassword());

            Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded.");
            Assert.assertEquals(
                    getDriver().getCurrentUrl(),
                    rowData.getExpectedUrl(),
                    "Current URL should match expected URL from Excel."
            );
        } else {
            LoginPage resultPage = loginPage.loginExpectingFailure(rowData.getUsername(), rowData.getPassword());

            Assert.assertTrue(resultPage.isErrorDisplayed(), "Error message should be displayed.");
            Assert.assertTrue(
                    resultPage.getErrorMessage().toLowerCase().contains(rowData.getExpectedError().toLowerCase()),
                    "Actual error should contain expected error from Excel."
            );
        }
    }

    @Override
    public String getTestName() {
        return testName;
    }
}