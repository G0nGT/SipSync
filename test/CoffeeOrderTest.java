import student.TestCase;

/** Initial tests for order data. */
public class CoffeeOrderTest extends TestCase {
    public void testStoresNameAndDrink() {
        CoffeeOrder order = new CoffeeOrder("Ada", "Latte");
        assertEquals("Ada", order.getCustomerName());
        assertEquals("Latte", order.getDrinkName());
    }

    public void testRejectsBlankName() {
        try {
            new CoffeeOrder("  ", "Latte");
            fail("Expected a blank name to be rejected.");
        }
        catch (IllegalArgumentException exception) {
            assertNotNull(exception.getMessage());
        }
    }

    // Remaining constructor cases are listed in README.md.
}
