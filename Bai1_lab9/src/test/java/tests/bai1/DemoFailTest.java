package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import framework.base.BaseTest;

public class DemoFailTest extends BaseTest {

    @Test
    public void failTestOne() {
        getDriver().get("https://www.saucedemo.com/");
        Assert.assertEquals(getDriver().getTitle(), "Sai Title Co Chu Dich");
    }

    @Test
    public void failTestTwo() {
        getDriver().get("https://www.saucedemo.com/");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("abcxyz"));
    }

    @Test
    public void failTestThree() {
        getDriver().get("https://www.saucedemo.com/");
        Assert.assertFalse(getDriver().getPageSource().contains("Swag Labs"));
    }
}