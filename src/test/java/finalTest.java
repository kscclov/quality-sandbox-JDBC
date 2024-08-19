import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class finalTest extends productTest {

    @Test
    void addExoticVegetable() throws SQLException {
        insertValue("Артишок", "Овощ", true);
    }

    @Test
    void addNoneExoticVegetable() throws SQLException {
        insertValue("Морковка", "Овощ", false);
    }

    @Test
    void addExoticFruit() throws SQLException {
        insertValue("Маракуйя", "Фрукт", true);
    }

    @Test
    void addNoneExoticFruit() throws SQLException {
        insertValue("Грейпфрут", "Фрукт", false);
    }

}
