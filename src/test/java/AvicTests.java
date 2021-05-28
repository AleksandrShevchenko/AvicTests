import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.util.List;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AvicTests
{
    WebDriver driver;

    @BeforeTest
    public void setUp()
    {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void testsSetUp()
    {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test
    public void checkThatRecoveringPasswordUsingInvalidEmailCausesError()
    {
        driver.findElement(By.xpath("//div[@class=\"header-bottom__right flex-wrap middle-xs end-xs\"]//a[@href=\"https://avic.ua/sign-in\"]")).click();
        driver.findElement(By.xpath("//a[@class=\"btn-password fancybox\"]")).click();
        driver.findElement(By.xpath("//div[@class=\"modal-middle\"]//input[@data-validate=\"login\"]")).sendKeys("qweqweqweqwe@gmail.com", Keys.ENTER);
        WebElement expectedErrorPopup = driver.findElement(By.xpath("//div[@id=\"modalAlert\"]"));
        assertTrue(expectedErrorPopup.isDisplayed());
    }

    @Test
    public void checkAmountOfElementsOnThePage()
    {
        driver.findElement(By.xpath("//input[@id=\"input_search\"]")).sendKeys("Xiaomi", Keys.ENTER);
        List<WebElement> webElements = driver.findElements(By.xpath("//div[@class='prod-cart height']"));
        assertEquals(webElements.size(), 12);
    }

    @Test
    public void checkProductBuyingProcess()
    {
        driver.findElement(By.xpath("//input[@id=\"input_search\"]")).sendKeys("Xiaomi", Keys.ENTER);
        driver.findElement(By.xpath("//a[@class=\"prod-cart__buy\"]")).click();
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        driver.findElement(By.xpath("//a[@href=\"https://avic.ua/checkout\"]")).click();
        String actualURL = driver.getCurrentUrl();
        assertTrue(actualURL.contains("checkout"));
    }

    @Test
    public void checkTheCartCleaning()
    {
        driver.findElement(By.xpath("//input[@id=\"input_search\"]")).sendKeys("Xiaomi", Keys.ENTER);
        driver.findElement(By.xpath("//a[@class=\"prod-cart__buy\"]")).click();
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        driver.findElement(By.xpath("//div[@class=\"item\"]/i[@class=\"icon icon-close js-btn-close\"]")).click();
        new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-cart-name=\"modal\"]//b[text()='Корзина пустая!']")));
        String expectedText = driver.findElement(By.xpath("//div[@data-cart-name=\"modal\"]//b[text()='Корзина пустая!']")).getText();
        assertEquals(expectedText, "Корзина пустая!");
    }


    @AfterTest
    public void tearDown()
    {
        driver.close();
    }
}
