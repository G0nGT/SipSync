import java.util.List;

/** Validates and manages orders without console dependencies. */
public class CoffeeShopModel {
    private final OrderQueue queue;
    private final String[] drinkMenu;

    /** Creates an empty shop with the four specified drinks. */
    public CoffeeShopModel() {
        queue = new OrderQueue();
        drinkMenu = new String[] {
            "Espresso", "Latte", "Cappuccino", "Americano"
        };
    }

    /** Validates all details before changing the queue. */
    public void addOrder(String customerName, int drinkChoice) {
        if (customerName == null) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        String name = customerName.trim();
        if (!name.matches("[\\p{L} '-]+")
            || !name.matches(".*\\p{L}.*")) {
            throw new IllegalArgumentException(
                "Use letters, spaces, apostrophes, or hyphens in the name."
                + " Include at least one letter.");
        }
        if (drinkChoice < 1 || drinkChoice > drinkMenu.length) {
            throw new IllegalArgumentException("Choose a drink from 1 to 4.");
        }
        queue.enqueue(new CoffeeOrder(name, drinkMenu[drinkChoice - 1]));
    }

    /** Completes the oldest order; throws if there are none. */
    public CoffeeOrder completeNextOrder() {
        return queue.dequeue();
    }

    /** Returns a separate list of pending orders. */
    public List<CoffeeOrder> getPendingOrders() {
        return queue.getOrders();
    }

    /** Returns a copy so callers cannot change the fixed menu. */
    public String[] getDrinkMenu() {
        return drinkMenu.clone();
    }
}
