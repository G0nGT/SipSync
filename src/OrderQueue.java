import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/** A singly linked FIFO queue of coffee orders. */
public class OrderQueue {
    private OrderNode head;
    private OrderNode tail;

    /** Creates an empty queue. */
    public OrderQueue() {
        head = null;
        tail = null;
    }

    /** Adds an order at the back. */
    public void enqueue(CoffeeOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required.");
        }
        OrderNode node = new OrderNode(order);
        if (isEmpty()) {
            head = node;
        }
        else {
            tail.next = node;
        }
        tail = node;
    }

    /** Removes and returns the oldest order. */
    public CoffeeOrder dequeue() {
        CoffeeOrder order = peek();
        head = head.next;
        if (head == null) {
            tail = null;
        }
        return order;
    }

    /** Returns the oldest order without removing it. */
    public CoffeeOrder peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("No pending orders.");
        }
        return head.order;
    }

    /** Returns whether the queue has no orders. */
    public boolean isEmpty() {
        return head == null;
    }

    /** Returns a separate list in FIFO order. */
    public List<CoffeeOrder> getOrders() {
        List<CoffeeOrder> orders = new ArrayList<CoffeeOrder>();
        OrderNode current = head;
        while (current != null) {
            orders.add(current.order);
            current = current.next;
        }
        return orders;
    }
}
