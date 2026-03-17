package framework.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import framework.base.BasePage;

public class InventoryPage extends BasePage {

    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = ".inventory_item button")
    private List<WebElement> addToCartButtons;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"));
    }

    public InventoryPage addFirstItemToCart() {
        if (addToCartButtons == null || addToCartButtons.isEmpty()) {
            throw new IllegalStateException("No Add to Cart button found on Inventory Page.");
        }
        waitAndClick(addToCartButtons.get(0));
        return this;
    }

    public InventoryPage addItemByName(String itemName) {
        for (WebElement item : inventoryItems) {
            WebElement itemNameElement = item.findElement(By.cssSelector(".inventory_item_name"));
            String currentName = itemNameElement.getText().trim();

            if (currentName.equalsIgnoreCase(itemName)) {
                WebElement addButton = item.findElement(By.tagName("button"));
                scrollToElement(addButton);
                waitAndClick(addButton);
                return this;
            }
        }
        throw new IllegalArgumentException("Cannot find item with name: " + itemName);
    }

    public int getCartItemCount() {
        try {
            String badgeText = getText(cartBadge);
            return Integer.parseInt(badgeText);
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage goToCart() {
        waitAndClick(cartLink);
        waitForPageLoad();
        return new CartPage(driver);
    }
}