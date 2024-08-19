import org.ibs.utils.PropConst;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.sql.*;
import java.time.Duration;


public class BaseTests {

 public static Connection connection;

 static WebDriver driver;

 @BeforeAll
 static void beforeAllTest() throws IOException, SQLException {

  driver = new ChromeDriver();
  driver.get(PropConst.getProperty(PropConst.BASE_URL));
  driver.manage().window().maximize();

  WebElement dropdownBtn = driver.findElement(By.xpath("//a[@id='navbarDropdown']"));
  dropdownBtn.click();

  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  WebElement products
          = driver.findElement(By.xpath("//a[@href='/food']"));
  products.click();

  connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb;user=user;password=pass");
  Statement statement = connection.createStatement();

 }

 @BeforeEach
 void clickOnAddProductButton() {
  System.out.println("");
  System.out.println("@BeforeEach");
  System.out.println("");

  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

  WebElement clickOnAddBtnTest = driver.findElement(By.xpath("//button[text()='Добавить']"));
  WebDriverWait waitForClickBtn = new WebDriverWait(driver, Duration.ofSeconds(5));
  waitForClickBtn.until(ExpectedConditions.elementToBeClickable(clickOnAddBtnTest));

  clickOnAddBtnTest.click();
 }

 @AfterAll
 static void afterAllTest() throws IOException, SQLException {

  try (Statement statement = connection.createStatement()) {
   ResultSet resultSet = statement.executeQuery("SELECT * FROM food");
   while (resultSet.next()) {
    String foodName = resultSet.getString("food_name");
    String foodType = resultSet.getString("food_type");
    boolean foodExotic = resultSet.getInt("food_exotic") == 1;

    System.out.println("Название: " + foodName + ", Тип: " + foodType + ", Экзотический: " + foodExotic);
   }
  }

  connection.close();
  driver.quit();

 }
}
