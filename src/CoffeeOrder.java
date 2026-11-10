/** One customer's single-drink order. */
public class CoffeeOrder {
    private final String customerName;
    private final String drinkName;

    /** Creates an order with a nonblank name and drink. */
    public CoffeeOrder(String customerName, String drinkName) {
        if (customerName == null || customerName.trim().isEmpty()
            || drinkName == null || drinkName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Name and drink are required.");
        }
        this.customerName = customerName.trim();
        this.drinkName = drinkName.trim();
    }

    /** Returns the customer name. */
    public String getCustomerName() {
        return customerName;
    }

    /** Returns the drink name. */
    public String getDrinkName() {
        return drinkName;
    }
}