import java.util.NoSuchElementException;

/** Connects console actions to the independent model. */
public class CoffeeShopController {
    private final CoffeeShopModel model;
    private final ConsoleView view;

    /** Stores the two collaborators. */
    public CoffeeShopController(CoffeeShopModel model, ConsoleView view) {
        if (model == null || view == null) {
            throw new IllegalArgumentException("Model and view are required.");
        }
        this.model = model;
        this.view = view;
    }

    /** Runs until Exit is selected or input ends. */
    public void start() {
        while (true) {
            view.displayMenu();
            String line = view.readLine("Choice: ");
            if (line == null) {
                return;
            }
            int choice;
            try {
                choice = Integer.parseInt(line.trim());
            }
            catch (NumberFormatException exception) {
                view.displayMessage("Choose 0, 1, 2, or 3.");
                continue;
            }
            switch (choice) {
                case 0:
                    return;
                case 1:
                    if (!addOrder()) {
                        return;
                    }
                    break;
                case 2:
                    view.displayOrders(model.getPendingOrders());
                    break;
                case 3:
                    completeOrder();
                    break;
                default:
                    view.displayMessage("Choose 0, 1, 2, or 3.");
                    break;
            }
        }
    }

    // A failed entry starts again with the name, as specified.
    private boolean addOrder() {
        while (true) {
            String name = view.readLine("Customer name: ");
            if (name == null) {
                return false;
            }
            view.displayDrinkMenu(model.getDrinkMenu());
            String drink = view.readLine("Drink choice: ");
            if (drink == null) {
                return false;
            }
            int choice;
            try {
                choice = Integer.parseInt(drink.trim());
            }
            catch (NumberFormatException exception) {
                view.displayMessage("Enter a whole-number drink choice.");
                continue;
            }
            try {
                model.addOrder(name, choice);
            }
            catch (IllegalArgumentException exception) {
                view.displayMessage(exception.getMessage());
                continue;
            }
            view.displayMessage("Order added.");
            view.displayOrders(model.getPendingOrders());
            return true;
        }
    }

    private void completeOrder() {
        try {
            CoffeeOrder order = model.completeNextOrder();
            view.displayMessage("Completed: " + order.getCustomerName()
                + " - " + order.getDrinkName());
            view.displayOrders(model.getPendingOrders());
        }
        catch (NoSuchElementException exception) {
            view.displayMessage("No pending orders to complete.");
        }
    }
}
