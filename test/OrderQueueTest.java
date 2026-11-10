import student.TestCase;
import java.util.NoSuchElementException;

/** Core FIFO and empty-queue tests. */
public class OrderQueueTest extends TestCase {
    public void testAddsPeeksAndRemovesInFifoOrder() {
        OrderQueue queue = new OrderQueue();
        CoffeeOrder ada = new CoffeeOrder("Ada", "Latte");
        CoffeeOrder ben = new CoffeeOrder("Ben", "Americano");
        assertTrue(queue.isEmpty());
        queue.enqueue(ada);
        queue.enqueue(ben);
        assertFalse(queue.isEmpty());
        assertSame(ada, queue.peek());
        assertEquals(2, queue.getOrders().size());
        assertSame(ada, queue.getOrders().get(0));
        assertSame(ben, queue.getOrders().get(1));
        assertSame(ada, queue.dequeue());
        assertSame(ben, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    public void testCanAddAfterRemovingLastOrder() {
        OrderQueue queue = new OrderQueue();
        CoffeeOrder ada = new CoffeeOrder("Ada", "Latte");
        CoffeeOrder chris = new CoffeeOrder("Chris", "Espresso");
        queue.enqueue(ada);
        queue.dequeue();
        queue.enqueue(chris);
        assertSame(chris, queue.peek());
        assertSame(chris, queue.dequeue());
        assertTrue(queue.getOrders().isEmpty());
    }

    public void testRejectsEmptyDequeue() {
        try {
            new OrderQueue().dequeue();
            fail("Expected empty dequeue to be rejected.");
        }
        catch (NoSuchElementException exception) {
            assertNotNull(exception.getMessage());
        }
    }
}
