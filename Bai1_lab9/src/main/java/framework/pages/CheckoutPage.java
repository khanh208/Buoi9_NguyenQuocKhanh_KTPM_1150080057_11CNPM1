package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import framework.base.BasePage;

public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.id("checkout_info_container"));
    }
}