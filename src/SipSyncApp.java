/** Starts the console application. */
public class SipSyncApp {
    /** Builds the model, view, and controller. */
    public static void main(String[] args) {
        CoffeeShopModel model = new CoffeeShopModel();
        ConsoleView view = new ConsoleView();
        CoffeeShopController controller =
            new CoffeeShopController(model, view);
        controller.start();
    }
}
