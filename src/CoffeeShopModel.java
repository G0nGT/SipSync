import java.util.List;

/**
 * Validates and manages coffee orders without console dependencies.
 */
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
            throw new IllegalArgumentException(
                "Customer name is required.");
        }

        String name = customerName.trim();

        if (!name.matches("[\\p{L} '-]+")
            || !name.matches(".*\\p{L}.*")) {
            throw new IllegalArgumentException(
                "Invalid name. Please use letters, spaces, "
                    + "apostrophes, and hyphens only.");
        }

        if (drinkChoice < 1 || drinkChoice > drinkMenu.length) {
            throw new IllegalArgumentException(
                "Choose a drink from 1 to 4.");
        }

        queue.enqueue(new CoffeeOrder(
            name, drinkMenu[drinkChoice - 1]));
    }

    /** Completes the oldest order; throws if there are none. */
    public CoffeeOrder completeNextOrder() {
        return queue.dequeue();
    }

    /**
     * Moves the oldest order to the back of the queue.
     * Returns the skipped order.
     * Throws NoSuchElementException if the queue is empty.
     */
    public CoffeeOrder skipNextOrder() {
        CoffeeOrder skipped = queue.dequeue();
        queue.enqueue(skipped);
        return skipped;
    }

    /** Returns a separate list of pending orders. */
    public List<CoffeeOrder> getPendingOrders() {
        return queue.getOrders();
    }

    /** Returns a copy of the fixed drink menu. */
    public String[] getDrinkMenu() {
        return drinkMenu.clone();
    }

    /** Returns true if there are pending orders. */
    public boolean hasPendingOrders() {
        return !queue.getOrders().isEmpty();
    }

    /** Returns the number of pending orders. */
    public int getOrderCount() {
        return queue.getOrders().size();
    }
}
