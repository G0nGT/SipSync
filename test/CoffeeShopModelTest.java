import student.TestCase;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Tests CoffeeShopModel independently of the view.
 */
public class CoffeeShopModelTest extends TestCase {

    /** Tests adding and completing orders. */
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

    /** Tests the specified drink menu. */
    public void testHasSpecifiedDrinkMenu() {
        CoffeeShopModel model = new CoffeeShopModel();

        assertEquals(Arrays.asList(new String[] {
            "Espresso", "Latte", "Cappuccino", "Americano"
        }), Arrays.asList(model.getDrinkMenu()));
    }

    /** Tests rejecting numeric names. */
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

    /** Tests rejecting empty names. */
    public void testRejectsEmptyName() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("", 1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    /** Tests rejecting null names. */
    public void testRejectsNullName() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder(null, 1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    /** Tests rejecting names containing special characters. */
    public void testRejectsSpecialCharacters() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("Ada123!", 1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    /** Tests that apostrophes and hyphens are accepted. */
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

    /** Tests rejecting invalid drink choices. */
    public void testRejectsInvalidDrinkChoice() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("Ada", 5);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    /** Tests rejecting negative drink choices. */
    public void testRejectsNegativeDrinkChoice() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.addOrder("Ada", -1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    /** Tests completing orders in FIFO order. */
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

    /** Tests alternating additions and completions. */
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

    /** Tests completing an empty queue. */
    public void testCompleteEmptyQueue() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.completeNextOrder();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    /** Tests that the drink menu cannot be changed externally. */
    public void testMenuCannotBeModified() {
        CoffeeShopModel model = new CoffeeShopModel();

        String[] menu = model.getDrinkMenu();

        menu[0] = "Changed";

        assertEquals("Espresso", model.getDrinkMenu()[0]);
    }

    /** Tests checking whether orders are pending. */
    public void testHasPendingOrders() {
        CoffeeShopModel model = new CoffeeShopModel();

        assertFalse(model.hasPendingOrders());

        model.addOrder("Ada", 1);

        assertTrue(model.hasPendingOrders());

        model.completeNextOrder();

        assertFalse(model.hasPendingOrders());
    }

    /** Tests counting pending orders. */
    public void testGetOrderCount() {
        CoffeeShopModel model = new CoffeeShopModel();

        assertEquals(0, model.getOrderCount());

        model.addOrder("Ada", 1);
        model.addOrder("Ben", 2);

        assertEquals(2, model.getOrderCount());

        model.completeNextOrder();

        assertEquals(1, model.getOrderCount());
    }

    /** Tests moving the first order to the back. */
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

    /** Tests skipping when only one order exists. */
    public void testSkipSingleOrder() {
        CoffeeShopModel model = new CoffeeShopModel();

        model.addOrder("Ada", 1);

        CoffeeOrder skipped = model.skipNextOrder();

        assertEquals("Ada", skipped.getCustomerName());

        assertEquals(1, model.getPendingOrders().size());

        assertEquals("Ada", model.getPendingOrders().get(0)
            .getCustomerName());
    }

    /** Tests skipping when the queue is empty. */
    public void testSkipEmptyQueue() {
        CoffeeShopModel model = new CoffeeShopModel();

        try {
            model.skipNextOrder();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertTrue(model.getPendingOrders().isEmpty());
        }
    }

    /** Tests completing orders after skipping. */
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

    /** Tests that returned order lists are independent. */
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
            assertEquals("Ada", model.completeNextOrder().getCustomerName());
        }
    }

}


