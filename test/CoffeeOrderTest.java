import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Tests order data and validation. */
public class CoffeeOrderTest {

    @Test
    public void testStoresNameAndDrink() {
        CoffeeOrder order = new CoffeeOrder("Ada", "Latte");
        assertEquals("Ada", order.getCustomerName());
        assertEquals("Latte", order.getDrinkName());
    }

    @Test
    public void testRejectsBlankName() {
        try {
            new CoffeeOrder("  ", "Latte");
            fail("Expected a blank name to be rejected.");
        }
        catch (IllegalArgumentException exception) {
            assertNotNull(exception.getMessage());
        }
    }

    @Test
    public void testOrderValidationAndTrimming() {
        CoffeeOrder order = new CoffeeOrder(" Ada ", " Latte ");
        assertEquals("Ada", order.getCustomerName());
        assertEquals("Latte", order.getDrinkName());

        String[][] cases = {
            {null, "Latte"}, {"Ada", null}, {"Ada", ""},
            {"Ada", "  "}, {"", "Latte"}
        };

        for (String[] details : cases) {
            try {
                new CoffeeOrder(details[0], details[1]);
                fail("Expected IllegalArgumentException");
            }
            catch (IllegalArgumentException exception) {
                assertNotNull(exception.getMessage());
            }
        }
    }
}
