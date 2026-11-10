/** A link used only inside OrderQueue. */
class OrderNode {
    final CoffeeOrder order;
    OrderNode next;

    OrderNode(CoffeeOrder order) {
        this.order = order;
    }
}
