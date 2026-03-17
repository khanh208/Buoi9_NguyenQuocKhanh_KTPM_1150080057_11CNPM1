package framework.base;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * BasePage là lớp cha cho mọi Page Object trong framework.
 * Lớp này đóng gói các thao tác Selenium phổ biến với Explicit Wait,
 * giúp tái sử dụng code và tránh viết lặp lại ở từng page.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    private static final int DEFAULT_TIMEOUT_SECONDS = 15;

    /**
     * Khởi tạo BasePage với WebDriver hiện tại và init các @FindBy elements.
     *
     * @param driver WebDriver của thread hiện tại
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    /**
     * Chờ element ở trạng thái clickable rồi click.
     * Dùng khi phần tử có thể chưa sẵn sàng ngay sau khi page render.
     *
     * @param element phần tử cần click
     */
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Chờ element hiển thị, xóa dữ liệu cũ rồi nhập text mới.
     * Dùng cho textbox, password field, search box...
     *
     * @param element phần tử input cần nhập
     * @param text nội dung cần nhập
     */
    protected void waitAndType(WebElement element, String text) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }

    /**
     * Chờ element hiển thị rồi lấy text đã trim.
     * Dùng để đọc label, message, title, error text...
     *
     * @param element phần tử cần lấy text
     * @return text của element sau khi đã trim khoảng trắng đầu/cuối
     */
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    /**
     * Kiểm tra element có đang hiển thị hay không bằng locator.
     * Method này không throw exception nếu element không tồn tại.
     * Có xử lý StaleElementReferenceException vì DOM có thể bị render lại.
     *
     * @param locator locator của phần tử cần kiểm tra
     * @return true nếu element hiển thị, false nếu không tìm thấy/hết thời gian/stale
     */
    protected boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (StaleElementReferenceException e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
            } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException ex) {
                return false;
            }
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Scroll đến element bằng JavaScript và chờ element hiển thị.
     * Dùng khi phần tử nằm ngoài viewport, cần kéo xuống trước khi thao tác.
     *
     * @param element phần tử cần scroll tới
     */
    protected void scrollToElement(WebElement element) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                visibleElement
        );
    }

    /**
     * Chờ page load hoàn tất bằng cách kiểm tra document.readyState = 'complete'.
     * Dùng sau khi chuyển trang hoặc refresh để đảm bảo DOM chính đã tải xong.
     */
    protected void waitForPageLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = webDriver ->
                ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete");

        wait.until(pageLoadCondition);
    }

    /**
     * Chờ element xuất hiện rồi lấy giá trị của attribute chỉ định.
     * Dùng cho các trường hợp cần đọc value, href, src, class, disabled...
     *
     * @param element phần tử cần lấy attribute
     * @param attributeName tên attribute
     * @return giá trị attribute, có thể là null nếu attribute không tồn tại
     */
    protected String getAttribute(WebElement element, String attributeName) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attributeName);
    }
}