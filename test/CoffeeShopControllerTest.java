import student.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/** Initial end-to-end console tests. */
public class CoffeeShopControllerTest extends TestCase {
    public void testAddsViewsAndCompletesOldestOrder() {
        CoffeeShopModel model = new CoffeeShopModel();
        String output = run(model, "1\nAda\n2\n1\nBen\n4\n2\n3\n0\n");
        assertTrue(output.contains("1. Ada - Latte"));
        assertTrue(output.contains("2. Ben - Americano"));
        assertTrue(output.contains("Completed: Ada - Latte"));
        String afterCompletion = output.substring(
            output.indexOf("Completed: Ada - Latte"));
        assertTrue(afterCompletion.contains("1. Ben - Americano"));
        assertFalse(afterCompletion.contains("1. Ada - Latte"));
        assertEquals(1, model.getPendingOrders().size());
        assertEquals("Ben", model.getPendingOrders().get(0)
            .getCustomerName());
    }

    public void testIncompleteOrderAtEndOfInputIsNotAdded() {
        CoffeeShopModel model = new CoffeeShopModel();
        String output = run(model, "1\nAda\n");
        assertTrue(model.getPendingOrders().isEmpty());
        assertFalse(output.contains("Order added."));
    }

    private String run(CoffeeShopModel model, String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner(input),
            new PrintStream(bytes));
        new CoffeeShopController(model, view).start();
        return bytes.toString();
    }
}
