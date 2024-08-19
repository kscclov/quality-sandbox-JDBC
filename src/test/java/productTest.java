import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.*;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class productTest extends BaseTests {

    public void insertValue(String name, String type, boolean shouldCheck) throws SQLException {
        inputNameOfVegetable(name);
        chooseTypeVegetable(type);
        handleCheckbox(shouldCheck);
        clickOnBtnSave();
        addProduct(connection, name, type, shouldCheck);
        checkNewProductOnList(name, type, shouldCheck);
    }

    public void handleCheckbox (boolean shouldCheck) {

        WebElement checkbox = driver.findElement(By.xpath("//input[@id='exotic']")); // Замените XPath

        WebDriverWait waitforCheck = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitforCheck.until(ExpectedConditions.elementToBeClickable(checkbox));
        if (shouldCheck) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        } else {
            if (checkbox.isSelected()) {
                checkbox.click();
            }
            shouldCheck = checkbox.isSelected();
        }
    }

    public void chooseTypeVegetable(String type) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='type']")));
        Select select = new Select(dropdown);

        select.selectByVisibleText(type);

        dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='type']")));
        WebElement selectedOption = select.getFirstSelectedOption();
        assertEquals(type, selectedOption.getText(), "Неверный пункт выбран в выпадающем списке");
    }

    public void inputNameOfVegetable (String name) {
        WebElement nameOfEat = driver.findElement(By.xpath("//input[@id='name']"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(nameOfEat));

        nameOfEat.sendKeys(name);
        wait.until(ExpectedConditions.textToBePresentInElementValue(nameOfEat, name));

        String actualValue = nameOfEat.getAttribute("value");
        assertEquals(name, actualValue, "Значение в поле не совпадает с ожидаемым");
    }

    public void clickOnBtnSave() {
        WebElement btnSave = driver.findElement(By.xpath("//button[@id='save']"));
        btnSave.click();
    }

    public void addProduct(Connection connection, String name, String type, boolean shouldCheck) throws SQLException {
        int exoticValue = shouldCheck ? 1 : 0;
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO food (food_name, food_type, food_exotic) VALUES (?, ?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, type);
            statement.setInt(3, exoticValue);
            statement.executeUpdate();
        }
    }

    public void checkNewProductOnList(String name, String type, boolean shouldCheck) throws SQLException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table")));

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        WebElement lastRow = rows.get(rows.size() - 1);

        String actualName = lastRow.findElement(By.cssSelector("td:nth-child(2)")).getText();
        String actualType = lastRow.findElement(By.cssSelector("td:nth-child(3)")).getText();
        if (actualType.equals("Фрукт")) {
            actualType = "FRUIT";
        } else {
            actualType = "VEGETABLE";
        }
        String actualCheck = lastRow.findElement(By.cssSelector("td:nth-child(4)")).getText();

        int exoticValue = shouldCheck ? 1 : 0;
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM food ORDER BY FOOD_ID DESC LIMIT 1"); // Получаем последнюю запись

            if (resultSet.next()) {
                String db_name = resultSet.getString("food_name");
                String db_type = resultSet.getString("food_type");
                int db_exotic = resultSet.getInt("food_exotic");

                Assertions.assertEquals(actualName, db_name, "Название продукта не совпадает");
                Assertions.assertEquals(actualType, db_type, "Тип продукта не совпадает");
                Assertions.assertEquals(shouldCheck, db_exotic == 1, "Значение чекбокса не совпадает");
            } else {
                Assertions.fail("Нет записей в таблице food.");
            }
        }
    }

}