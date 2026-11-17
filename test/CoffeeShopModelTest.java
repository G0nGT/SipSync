import student.TestCase;
import java.util.Arrays;
import java.util.NoSuchElementException;

/** Tests the model independently of the view. */
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

        assertEquals(Arrays.asList(new String[] {
            "Espresso", "Latte", "Cappuccino", "Americano"
        }), Arrays.asList(model.getDrinkMenu()));
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

    public void testRejectsEmptyName() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("", 1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    public void testRejectsNullName() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder(null, 1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    public void testRejectsSpecialCharacters() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("Ada123!", 1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    public void testAcceptsValidNames() {
        CoffeeShopModel model = new CoffeeShopModel();

        model.addOrder("Mary-Jane", 1);
        model.addOrder("O'Brien", 2);

        assertEquals(2, model.getPendingOrders().size());
        assertEquals("Mary-Jane", model.getPendingOrders().get(0)
            .getCustomerName());
        assertEquals("O'Brien", model.getPendingOrders().get(1)
            .getCustomerName());
    }

    public void testRejectsInvalidDrinkChoice() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("Ada", 5);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    public void testRejectsNegativeDrinkChoice() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("Ada", -1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    public void testOrdersCompleteInOrder() {
        CoffeeShopModel model = new CoffeeShopModel();

        model.addOrder("Ada", 1);
        model.addOrder("Ben", 2);
        model.addOrder("Cara", 3);

        assertEquals("Ada",
            model.completeNextOrder().getCustomerName());
        assertEquals("Ben",
            model.completeNextOrder().getCustomerName());
        assertEquals("Cara",
            model.completeNextOrder().getCustomerName());
        assertTrue(model.getPendingOrders().isEmpty());
    }

    public void testInterleavedOrders() {
        CoffeeShopModel model = new CoffeeShopModel();

        model.addOrder("Ada", 1);
        model.addOrder("Ben", 2);

        assertEquals("Ada",
            model.completeNextOrder().getCustomerName());

        model.addOrder("Cara", 3);

        assertEquals("Ben",
            model.completeNextOrder().getCustomerName());

        model.addOrder("Dan", 4);

        assertEquals("Cara",
            model.completeNextOrder().getCustomerName());
        assertEquals("Dan",
            model.completeNextOrder().getCustomerName());
        assertTrue(model.getPendingOrders().isEmpty());
    }

    public void testCompleteEmptyQueue() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.completeNextOrder();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException exception) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    public void testMenuCannotBeModified() {
        CoffeeShopModel model = new CoffeeShopModel();
        String[] menu = model.getDrinkMenu();

        menu[0] = "Changed";

        assertEquals("Espresso", model.getDrinkMenu()[0]);
    }

    public void testHasPendingOrders() {
        CoffeeShopModel model = new CoffeeShopModel();

        assertFalse(model.hasPendingOrders());

        model.addOrder("Ada", 1);

        assertTrue(model.hasPendingOrders());

        model.completeNextOrder();

        assertFalse(model.hasPendingOrders());
    }

    public void testGetOrderCount() {
        CoffeeShopModel model = new CoffeeShopModel();

        assertEquals(0, model.getOrderCount());

        model.addOrder("Ada", 1);
        model.addOrder("Ben", 2);

        assertEquals(2, model.getOrderCount());

        model.completeNextOrder();

        assertEquals(1, model.getOrderCount());
    }

    public void testSkipNextOrder() {
        CoffeeShopModel model = new CoffeeShopModel();

        model.addOrder("Ada", 1);
        model.addOrder("Ben", 2);

        CoffeeOrder skipped = model.skipNextOrder();

        assertEquals("Ada", skipped.getCustomerName());
        assertEquals(2, model.getPendingOrders().size());
        assertEquals("Ben", model.getPendingOrders().get(0)
            .getCustomerName());
        assertEquals("Ada", model.getPendingOrders().get(1)
            .getCustomerName());
    }

    public void testSkipSingleOrder() {
        CoffeeShopModel model = new CoffeeShopModel();
        model.addOrder("Ada", 1);

        CoffeeOrder skipped = model.skipNextOrder();

        assertEquals("Ada", skipped.getCustomerName());
        assertEquals(1, model.getPendingOrders().size());
        assertEquals("Ada", model.getPendingOrders().get(0)
            .getCustomerName());
    }

    public void testSkipEmptyQueue() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.skipNextOrder();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException exception) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    public void testCompleteAfterSkip() {
        CoffeeShopModel model = new CoffeeShopModel();

        model.addOrder("Ada", 1);
        model.addOrder("Ben", 2);
        model.addOrder("Cara", 3);

        model.skipNextOrder();

        assertEquals("Ben",
            model.completeNextOrder().getCustomerName());
        assertEquals("Cara",
            model.completeNextOrder().getCustomerName());
        assertEquals("Ada",
            model.completeNextOrder().getCustomerName());
        assertTrue(model.getPendingOrders().isEmpty());
    }

    public void testPendingOrdersListIsIndependent() {
        CoffeeShopModel model = new CoffeeShopModel();
        model.addOrder("Ada", 1);

        java.util.List<CoffeeOrder> orders =
            model.getPendingOrders();
        orders.clear();

        assertEquals(1, model.getPendingOrders().size());
        assertEquals("Ada", model.getPendingOrders().get(0)
            .getCustomerName());
    }

    public void testModelNameBoundaries() {
        CoffeeShopModel model = new CoffeeShopModel();
        String name = "";

        for (int i = 0; i < 50; i++) {
            name += "A";
        }

        model.addOrder(name, 1);

        String[] invalidNames = {name + "A", "   ", "---", "'''"};

        for (String invalid : invalidNames) {
            try {
                model.addOrder(invalid, 1);
                fail("Expected IllegalArgumentException");
            }
            catch (IllegalArgumentException exception) {
                assertEquals(1, model.getOrderCount());
            }
        }

        model.addOrder(" Ada ", 4);
        assertEquals("Ada", model.getPendingOrders().get(1)
            .getCustomerName());
    }

    public void testZeroDrinkKeepsOrders() {
        CoffeeShopModel model = new CoffeeShopModel();
        model.addOrder("Ada", 1);

        try {
            model.addOrder("Ben", 0);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertEquals(1, model.getOrderCount());
            assertEquals("Ada",
                model.completeNextOrder().getCustomerName());
        }
    }
}