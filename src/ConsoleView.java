import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/** Reads complete lines and displays text; stores no order state. */
public class ConsoleView {
    private final Scanner input;
    private final PrintStream output;

    /** Uses the normal console. */
    public ConsoleView() {
        this(new Scanner(System.in), System.out);
    }

    /** Allows tests to supply input and capture output. */
    public ConsoleView(Scanner input, PrintStream output) {
        if (input == null || output == null) {
            throw new IllegalArgumentException(
                "Input and output are required.");
        }
        this.input = input;
        this.output = output;
    }

    /** Shows the main actions. */
    public void displayMenu() {
        output.println("\nSipSync");
        output.println("1 Add Order");
        output.println("2 View Orders");
        output.println("3 Complete Next Order");
        output.println("0 Exit");
    }

    /** Shows the numbered drinks. */
    public void displayDrinkMenu(String[] drinks) {
        for (int i = 0; i < drinks.length; i++) {
            output.println((i + 1) + " " + drinks[i]);
        }
    }

    /** Returns a line, or null when input ends. */
    public String readLine(String prompt) {
        output.print(prompt);
        output.flush();
        if (!input.hasNextLine()) {
            return null;
        }
        return input.nextLine();
    }

    /** Shows a message from the controller. */
    public void displayMessage(String message) {
        output.println(message);
    }

    /** Shows every pending order in the supplied order. */
    public void displayOrders(List<CoffeeOrder> orders) {
        if (orders.isEmpty()) {
            output.println("No pending orders.");
            return;
        }
        output.println("Pending orders:");
        for (int i = 0; i < orders.size(); i++) {
            CoffeeOrder order = orders.get(i);
            output.println((i + 1) + ". " + order.getCustomerName()
                + " - " + order.getDrinkName());
        }
    }
}
