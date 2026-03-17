package framework.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import framework.base.BasePage;

public class CartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_item .inventory_item_name")
    private List<WebElement> itemNameElements;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        if (cartItems == null || cartItems.isEmpty()) {
            return 0;
        }
        return cartItems.size();
    }

    public CartPage removeFirstItem() {
        if (removeButtons == null || removeButtons.isEmpty()) {
            return this;
        }
        waitAndClick(removeButtons.get(0));
        return this;
    }

    public CheckoutPage goToCheckout() {
        waitAndClick(checkoutButton);
        waitForPageLoad();
        return new CheckoutPage(driver);
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();

        if (itemNameElements == null || itemNameElements.isEmpty()) {
            return names;
        }

        for (WebElement itemNameElement : itemNameElements) {
            names.add(getText(itemNameElement));
        }
        return names;
    }
}