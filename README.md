# SipSync - First Prototype

SipSync is a console app for managing coffee orders. Employees can add an order, view waiting orders, and complete the oldest order first. Each order contains one customer name and one drink.

## Running the Application

This project uses Eclipse. Source files are in `src`, test files are in `test`, and the course library belongs in `lib`.

1. Place the course-provided `student.jar` in `lib`.
2. Open Eclipse and select **File > Import > General > Existing Projects into Workspace**.
3. Select the project folder and finish importing.
4. Right-click `SipSyncApp.java` and select **Run As > Java Application**.

The main menu provides these options:

- `1` - Add Order
- `2` - View Orders
- `3` - Complete Next Order
- `0` - Exit

The drink menu contains Espresso, Latte, Cappuccino, and Americano.

Invalid input displays an error and allows another attempt. Ending input exits the application without adding an incomplete order.

### Example

Enter each value on a separate line:

```text
1
Ada
2
1
Ben
4
2
3
0
```

This adds Ada's Latte and Ben's Americano. Viewing orders shows Ada first. Completing the next order removes Ada and displays Ben as the only remaining order.

## Running the Tests

The test classes extend `student.TestCase`.

In Eclipse, right-click the `test` folder and select **Run As > JUnit Test**. To run one test class, right-click that file instead.

To exercise the model without a view or controller, run:

- `CoffeeOrderTest`
- `OrderQueueTest`
- `CoffeeShopModelTest`

These tests create and use model objects directly. The included suite contains 12 tests and does not cover the entire Specification test plan.

## Design

| Class | Responsibility |
| --- | --- |
| CoffeeOrder | Stores a customer's name and drink. |
| OrderNode | Stores one order and a link to the next node. |
| OrderQueue | Implements a custom singly linked FIFO queue. |
| CoffeeShopModel | Validates order details and manages the queue. |
| ConsoleView | Reads console input and displays menus, messages, and orders. |
| CoffeeShopController | Handles choices, calls the model, and refreshes the display. |
| SipSyncApp | Creates the objects and starts the application. |

New orders enter at the tail, and completed orders leave from the head. Adding and removing an order take O(1). Creating or displaying the full order list takes O(n).

## Reflection

### What changed from the specification?

The implementation keeps the seven classes and the public method signatures listed in the Specification. It uses the console menu described in the Specification instead of the buttons described in the original Scope.

The Specification did not provide every view method. We added `displayMenu()`, `displayDrinkMenu()`, `readLine()`, and `displayMessage()` to keep input and output inside `ConsoleView`.

An additional `ConsoleView` constructor accepts a `Scanner` and `PrintStream`. This allows console tests to supply input and capture output without using the keyboard.

Names and drink text are trimmed when stored. `getOrders()` returns a separate list, and `getDrinkMenu()` returns an array copy. These choices prevent callers from changing the queue or fixed menu through returned data.

Validation stays in the specified layers. The controller parses console input, the model checks customer names and drink choices, and the queue rejects null orders and empty removal operations.

### Did the model stand alone?

Yes. The model can add, list, and complete orders without constructing a view or controller. Model classes do not read console input or print messages.

The controller gets the current order list from the model and passes it to the view. After a successful addition or completion, it requests and displays the updated list.

### What happened to the unknowns?

The Scope identified connecting actions to the model, refreshing the list, and testing FIFO as open questions.

The controller now connects menu choices to model methods. It refreshes the list after successful changes. The queue tests check FIFO by adding Ada before Ben and checking that Ada is removed first.

Connecting graphical buttons remains open because this prototype uses a console interface.

### What does not work yet?

The single-drink MVP is implemented. Multiple drinks per order, completing individual drinks within an order, a graphical interface, and skip/requeue are not implemented.

Orders are stored only while the application is running.

Some automated tests are still missing:

| Area | Additional tests needed | Owner |
| --- | --- | --- |
| CoffeeOrder | Null name, null or blank drink, and trimming | Parin |
| OrderQueue | Null enqueue without changing existing orders, empty peek, and independence of returned lists | Parin |
| CoffeeShopModel | Null and blank names, special characters, valid apostrophes and hyphens, invalid drink numbers, empty completion, and menu-copy protection | Monya |
| CoffeeShopController | Invalid menu and drink input, recovery after errors, empty completion, end of input at each prompt, and adding after clearing the queue | Mohammed |
| ConsoleView | Main menu, drink menu, prompts, and null constructor arguments | Nie |

## Next Steps

### Remaining MVP Work

The next steps focus on finishing test coverage and checking integration:

1. Parin will add the missing order and queue tests.
2. Monya will add the remaining model validation and empty-completion tests.
3. Mohammed will extend controller tests for invalid input, recovery, and end of input.
4. Nie will extend view tests and check the console demo.
5. Mohammed will lead a complete integration run. Any failures found during these checks will be fixed before adding the next feature.

### Deliverable 4 Change Feature

We plan to add **Skip/Requeue Next Order**. This action will move the oldest order to the back without completing it.

For example, if the queue contains Ada followed by Ben, skipping the next order will change it to Ben followed by Ada. This changes model state and will be available through the controller and view.

| Class or method | Predicted change | Owner |
| --- | --- | --- |
| CoffeeShopModel.skipNextOrder() | Add a method that removes the oldest order, adds it to the tail, and returns it. An empty queue throws NoSuchElementException. | Monya |
| CoffeeShopController.start() | Add menu option 4 and connect it to a skipOrder helper. | Mohammed |
| CoffeeShopController.skipOrder() | Call the model, display a confirmation, refresh the list, and handle an empty queue. | Mohammed |
| ConsoleView.displayMenu() | Display the new Skip/Requeue option. | Nie |
| CoffeeShopModelTest | Test two-order reordering, a single order, and an empty queue. | Monya |
| CoffeeShopControllerTest | Test the new menu action, refreshed output, empty skip, and completion after a skip. | Mohammed |
| ConsoleViewTest | Check that the new option appears. | Nie |
| OrderQueueTest | Check the dequeue-then-enqueue behavior used by requeue. | Parin |

`CoffeeOrder`, `OrderNode`, `OrderQueue`, and `SipSyncApp` should not need implementation changes. The existing queue operations already support removing from the front and adding at the back. `ConsoleView.displayOrders()` should also remain unchanged because it already displays orders in the supplied sequence.

### Stretch Goals

We will implement skip/requeue for Deliverable 4.

We are leaving multiple drinks per order and individual-drink completion out of the Deliverable 4 plan because they require larger changes to order data and completion rules. A graphical interface is also outside the next deliverable's planned scope.

## Work Attribution

File responsibilities follow the division of work in the Specification.

| Class or test file | Member | Responsibility |
| --- | --- | --- |
| CoffeeOrder.java | Parin | Order data and constructor validation |
| OrderNode.java | Parin | Linked-node structure |
| OrderQueue.java | Parin | FIFO queue operations and links |
| CoffeeOrderTest.java | Parin | Constructor and getter tests |
| OrderQueueTest.java | Parin | FIFO and queue-state tests |
| CoffeeShopModel.java | Monya | Order validation and model operations |
| CoffeeShopModelTest.java | Monya | Model validation and completion tests |
| CoffeeShopController.java | Mohammed | Menu handling, error recovery, and display refresh |
| SipSyncApp.java | Mohammed | Application startup and integration |
| CoffeeShopControllerTest.java | Mohammed | Console flow and integration tests |
| ConsoleView.java | Nie | Console input, menus, messages, and order display |
| ConsoleViewTest.java | Nie | View input and output tests |
| README.md | Nie | Project instructions, reflection, and next-step coordination |