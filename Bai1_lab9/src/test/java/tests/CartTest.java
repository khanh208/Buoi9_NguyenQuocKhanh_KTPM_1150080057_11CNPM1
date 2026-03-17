package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.CheckoutPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;

public class CartTest extends BaseTest {

    private ConfigReader config;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void testSetUp() {
        config = new ConfigReader(getEnv());
        loginPage = new LoginPage(getDriver()).open(config.getBaseUrl());
    }

    @Test
    public void addFirstItemToCartShouldIncreaseBadgeCount() {
        InventoryPage inventoryPage = loginPage.login(config.getUsername(), config.getPassword())
                .addFirstItemToCart();

        Assert.assertEquals(inventoryPage.getCartItemCount(), 1, "Cart badge count should be 1.");
    }

    @Test
    public void addSpecificItemByNameShouldAppearInCart() {
        String itemName = "Sauce Labs Backpack";

        CartPage cartPage = loginPage.login(config.getUsername(), config.getPassword())
                .addItemByName(itemName)
                .goToCart();

        List<String> itemNames = cartPage.getItemNames();

        Assert.assertEquals(cartPage.getItemCount(), 1, "Cart should contain exactly 1 item.");
        Assert.assertTrue(itemNames.contains(itemName), "Selected item should appear in cart.");
    }

    @Test
    public void emptyCartShouldReturnZeroItems() {
        CartPage cartPage = loginPage.login(config.getUsername(), config.getPassword())
                .goToCart();

        Assert.assertEquals(cartPage.getItemCount(), 0, "Empty cart should return 0 item.");
    }

    @Test
    public void removeFirstItemShouldMakeCartEmpty() {
        CartPage cartPage = loginPage.login(config.getUsername(), config.getPassword())
                .addFirstItemToCart()
                .goToCart()
                .removeFirstItem();

        Assert.assertEquals(cartPage.getItemCount(), 0, "Cart should be empty after removing the only item.");
    }

    @Test
    public void goToCheckoutShouldNavigateToCheckoutPage() {
        CheckoutPage checkoutPage = loginPage.login(config.getUsername(), config.getPassword())
                .addFirstItemToCart()
                .goToCart()
                .goToCheckout();

        Assert.assertTrue(checkoutPage.isLoaded(), "Checkout page should be loaded.");
    }
}