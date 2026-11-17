import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/** Tests console actions through the controller. */
public class CoffeeShopControllerTest {

    @Test
    public void testAddsViewsAndCompletesOldestOrder() {
        CoffeeShopModel model = new CoffeeShopModel();

        String output = run(model,
            "1\nAda\n2\n1\nBen\n4\n2\n3\n0\n");

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

    @Test
    public void testIncompleteOrderAtEndOfInputIsNotAdded() {
        CoffeeShopModel model = new CoffeeShopModel();
        String output = run(model, "1\nAda\n");

        assertTrue(model.getPendingOrders().isEmpty());
        assertFalse(output.contains("Order added."));
    }

    @Test
    public void testSkipRefreshAndCompletion() {
        CoffeeShopModel model = new CoffeeShopModel();

        String output = run(model,
            "1\nAda\n1\n1\nBen\n2\n4\n3\n0\n");

        String after = output.substring(output.indexOf("Skipped: Ada"));

        assertTrue(after.contains("1. Ben - Latte"));
        assertTrue(after.contains("2. Ada - Espresso"));
        assertTrue(after.contains("Completed: Ben - Latte"));
        assertEquals(1, model.getOrderCount());
        assertEquals("Ada", model.getPendingOrders().get(0)
            .getCustomerName());
    }

    @Test
    public void testEmptyActionsAndInvalidMenuRecover() {
        CoffeeShopModel model = new CoffeeShopModel();

        String output = run(model,
            "abc\n\n1.5\n99\n3\n4\n1\nAda\n1\n0\n");

        assertTrue(output.contains("Choose 0, 1, 2, 3, or 4."));
        assertTrue(output.contains("No pending orders to complete."));
        assertTrue(output.contains("No pending orders to skip."));
        assertEquals(1, model.getOrderCount());
    }

    @Test
    public void testBadOrdersRecover() {
        String[] inputs = {
            "\n1\n", "123\n1\n", "Ada!\n1\n", "Ada\n0\n",
            "Ada\n5\n", "Ada\n-1\n", "Ada\nabc\n",
            "Ada\n1.5\n", "Ada\n\n", "Ada\n999999999999999\n"
        };

        for (String input : inputs) {
            CoffeeShopModel model = new CoffeeShopModel();

            run(model, "1\n" + input + "Ben\n2\n0\n");

            assertEquals(1, model.getOrderCount());
            assertEquals("Ben", model.getPendingOrders().get(0)
                .getCustomerName());
        }
    }

    @Test
    public void testLongNameRecovers() {
        String name = "";

        for (int i = 0; i < 51; i++) {
            name += "A";
        }

        CoffeeShopModel model = new CoffeeShopModel();

        String output = run(model,
            "1\n" + name + "\n1\nBen\n2\n0\n");

        assertTrue(output.contains("50 characters or fewer"));
        assertEquals(1, model.getOrderCount());
        assertEquals("Ben", model.getPendingOrders().get(0)
            .getCustomerName());
    }

    @Test
    public void testEndOfInputAtEveryPrompt() {
        String[] inputs = {"", "1\n", "1\nAda\n", "1\nAda\nabc\n"};

        for (String input : inputs) {
            CoffeeShopModel model = new CoffeeShopModel();

            run(model, input);

            assertEquals(0, model.getOrderCount());
        }
    }

    @Test
    public void testAddAfterClearingAndSkipSingle() {
        CoffeeShopModel model = new CoffeeShopModel();

        String output = run(model,
            "1\nAda\n1\n3\n1\nBen\n2\n4\n0\n");

        assertTrue(output.contains("Skipped: Ben - Latte"));
        assertEquals(1, model.getOrderCount());
    }

    private String run(CoffeeShopModel model, String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        ConsoleView view = new ConsoleView(new Scanner(input),
            new PrintStream(bytes));

        new CoffeeShopController(model, view).start();

        return bytes.toString();
    }
}
