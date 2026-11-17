import student.TestCase;
import java.util.NoSuchElementException;

/** Tests FIFO order, invalid operations, and list protection. */
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

    public void testNullEnqueueKeepsOrders() {
        OrderQueue queue = new OrderQueue();
        CoffeeOrder ada = new CoffeeOrder("Ada", "Latte");
        queue.enqueue(ada);

        try {
            queue.enqueue(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException exception) {
            assertEquals(1, queue.getOrders().size());
            assertSame(ada, queue.peek());
        }
    }

    public void testEmptyPeek() {
        OrderQueue queue = new OrderQueue();

        try {
            queue.peek();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException exception) {
            assertTrue(queue.isEmpty());
        }
    }

    public void testInterleavedQueueAndListCopy() {
        OrderQueue queue = new OrderQueue();
        CoffeeOrder ada = new CoffeeOrder("Ada", "Latte");
        CoffeeOrder ben = new CoffeeOrder("Ben", "Espresso");
        CoffeeOrder cara = new CoffeeOrder("Cara", "Americano");

        queue.enqueue(ada);
        queue.enqueue(ben);
        assertSame(ada, queue.dequeue());

        queue.enqueue(cara);
        queue.enqueue(ada);
        queue.getOrders().clear();

        assertSame(ben, queue.dequeue());
        assertSame(cara, queue.dequeue());
        assertSame(ada, queue.dequeue());
        assertTrue(queue.isEmpty());
    }
}