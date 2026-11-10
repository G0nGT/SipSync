import student.TestCase;

/** Exercises the model without constructing any view. */
public class CoffeeShopModelTest extends TestCase {
    public void testAddsAndCompletesOrders() {
        CoffeeShopModel model = new CoffeeShopModel();
        assertTrue(model.getPendingOrders().isEmpty());
        model.addOrder("Ada", 2);
        model.addOrder("Ben", 4);
        assertEquals(2, model.getPendingOrders().size());
        assertEquals("Ada", model.getPendingOrders().get(0)
            .getCustomerName());
        CoffeeOrder completed = model.completeNextOrder();
        assertEquals("Ada", completed.getCustomerName());
        assertEquals("Latte", completed.getDrinkName());
        assertEquals(1, model.getPendingOrders().size());
        assertEquals("Ben", model.getPendingOrders().get(0)
            .getCustomerName());
        assertEquals("Americano", model.getPendingOrders().get(0)
            .getDrinkName());
    }

    public void testHasSpecifiedDrinkMenu() {
        CoffeeShopModel model = new CoffeeShopModel();
        assertEquals(java.util.Arrays.asList(new String[] {
            "Espresso", "Latte", "Cappuccino", "Americano"
        }), java.util.Arrays.asList(model.getDrinkMenu()));
    }

    public void testRejectsNumericNameWithoutChangingOrders() {
        CoffeeShopModel model = new CoffeeShopModel();
        model.addOrder("Ada", 2);
        try {
            model.addOrder("123", 2);
            fail("Expected an invalid name to be rejected.");
        }
        catch (IllegalArgumentException exception) {
            assertEquals(1, model.getPendingOrders().size());
            assertEquals("Ada", model.getPendingOrders().get(0)
                .getCustomerName());
        }
    }
}
