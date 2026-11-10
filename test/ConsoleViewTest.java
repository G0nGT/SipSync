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
}
