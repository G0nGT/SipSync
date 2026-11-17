import student.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/** Initial tests for console input and list display. */
public class ConsoleViewTest extends TestCase {
    public void testDisplaysEmptyAndPopulatedLists() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner(""),
            new PrintStream(bytes));
        view.displayOrders(Collections.<CoffeeOrder>emptyList());
        assertTrue(bytes.toString().contains("No pending orders."));
        bytes.reset();
        view.displayOrders(Arrays.asList(
            new CoffeeOrder("Ada", "Latte"),
            new CoffeeOrder("Ben", "Americano")));
        String text = bytes.toString();
        assertTrue(text.contains("1. Ada - Latte"));
        assertTrue(text.contains("2. Ben - Americano"));
        assertTrue(text.indexOf("Ada") < text.indexOf("Ben"));
    }

    public void testReadsLineAndReturnsNullAtEnd() {
        ConsoleView view = new ConsoleView(new Scanner("Ada\n"),
            new PrintStream(new ByteArrayOutputStream()));
        assertEquals("Ada", view.readLine("Name: "));
        assertNull(view.readLine("Name: "));
    }
    public void testMenusAndPrompts() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new Scanner("Ada\n"),
            new PrintStream(bytes));
        view.displayMenu();
        view.displayDrinkMenu(new CoffeeShopModel().getDrinkMenu());
        view.displayMessage("Ready");
        assertEquals("Ada", view.readLine("Customer name: "));
        String output = bytes.toString();
        assertTrue(output.contains("4 Skip/Requeue Next Order"));
        assertTrue(output.contains("1 Espresso"));
        assertTrue(output.contains("4 Americano"));
        assertTrue(output.contains("Ready"));
        assertTrue(output.contains("Customer name: "));
    }

    public void testNullViewArguments() {
        try {
            new ConsoleView(null, new PrintStream(
                new ByteArrayOutputStream()));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertNotNull(exception.getMessage());
        }
        try {
            new ConsoleView(new Scanner(""), null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertNotNull(exception.getMessage());
        }
    }

}


