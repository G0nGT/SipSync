# SipSync - Second Prototype + Tests

SipSync is a console app for managing coffee orders. Employees can add orders, view pending orders, complete the oldest order, and move the oldest order to the back using Skip/Requeue. Each order contains one customer name and one drink.

## Running the Application

Source files are in `src`, tests are in `test`, and the JUnit 5 library is in `lib`.

### VS Code

1. Open the project folder.
2. Open `src/SipSyncApp.java`.
3. Click **Run** above `main`.

To run tests, use **Run Tests** on the `test` folder or use the Testing sidebar.

### Eclipse

1. Import the folder through **File > Import > General > Existing Projects into Workspace**.
2. Right-click `SipSyncApp.java` → **Run As > Java Application**.
3. For tests, right-click `test` → **Run As > JUnit Test**, using JUnit 5.

### Command Line

From the project root in Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -d bin src/*.java
java -cp bin SipSyncApp
```

Compile and run the tests:

```powershell
javac -cp "lib/junit-platform-console-standalone-1.14.4.jar" -d bin src/*.java test/*.java
java -jar lib/junit-platform-console-standalone-1.14.4.jar execute --class-path bin --scan-class-path
```

The JUnit jar must be in `lib`, and `.classpath` must reference it. These commands were checked with JDK 21.

## Using SipSync

| Choice | Action |
| --- | --- |
| 1 | Add Order |
| 2 | View Orders |
| 3 | Complete Next Order |
| 4 | Skip/Requeue Next Order |
| 0 | Exit |

Drink choices are Espresso, Latte, Cappuccino, and Americano.

Names must contain a letter and may include spaces, apostrophes, and hyphens. Names longer than 50 characters are rejected. Invalid input displays a message and allows another attempt. Ending input exits without adding an incomplete order.

For example, if Ada is before Ben, choosing Skip/Requeue changes the order to Ben followed by Ada. Completing the next order then removes Ben. Empty completion and skip operations display a message without crashing.

## Design and Tests

| Class | Responsibility |
| --- | --- |
| CoffeeOrder | Stores one customer's name and drink. |
| OrderNode | Stores an order and the next-node link. |
| OrderQueue | Implements a singly linked FIFO queue. |
| CoffeeShopModel | Validates orders and manages adding, completing, and skipping. |
| ConsoleView | Reads input and displays menus, messages, and order lists. |
| CoffeeShopController | Connects user choices to the model and refreshes the view. |
| SipSyncApp | Creates the objects and starts the program. |

Adding, completing, and skipping take O(1). Creating or displaying the order list takes O(n).

The suite contains 43 tests. The latest command-line run passed all 43 with no failures.

The tests cover normal operations, invalid names and drink choices, empty queues, null inputs, FIFO order, interleaved queue operations, skip behavior, input recovery, and list/menu protection.

`CoffeeOrderTest`, `OrderQueueTest`, and `CoffeeShopModelTest` exercise the model without a view or controller. Controller and view tests check console flow and output.

## Reflection

### How did the design absorb the change?

We implemented the feature named in our D3 plan: **Skip/Requeue Next Order**.

| D3 prediction | Actual result |
| --- | --- |
| Add `CoffeeShopModel.skipNextOrder()`. | Uses dequeue followed by enqueue and returns the skipped order. An empty queue throws `NoSuchElementException`. |
| Add option 4 and `skipOrder()` in the controller. | Calls the model, displays confirmation, refreshes the list, and handles an empty queue. |
| Update `ConsoleView.displayMenu()`. | Displays the new option. |
| Add model, controller, view, and queue tests. | Tests reordering, single/empty queues, output, and completion after skip. |
| Keep CoffeeOrder, OrderNode, OrderQueue, and SipSyncApp unchanged. | No implementation changes were needed for skip. |
| Keep `ConsoleView.displayOrders()` unchanged. | It already displays the supplied list in order. |

The feature touched CoffeeShopModel, CoffeeShopController, ConsoleView, their tests, and OrderQueueTest. CoffeeOrderTest also received validation tests. All five test classes were converted to JUnit 5.

### Did the change cross the model/view line?

The layers stayed separate. The model changes the queue without console input or output. The controller handles the action and passes the updated list to the view.

The view does not keep its own queue or need to understand the linked nodes. The model still runs independently.

### What changed from the D3 plan?

We kept the planned skip feature and its expected behavior.

We added more boundary and bad-input tests. The 50-character name limit moved into the model because the earlier controller warning did not prevent an order from being added.

The model also has tested `hasPendingOrders()` and `getOrderCount()` methods. These currently take O(n) because they use an order-list copy.

The tests now use JUnit 5, following HokieConnect. This avoids the Security Manager startup problem encountered with `student.TestCase`. The test imports, annotations, and build path changed accordingly.

### What does not work yet?

The single-drink console MVP and skip feature are implemented. Multiple drinks per order, individual-drink completion, a GUI, and saving orders between runs are not implemented.

The current tests pass, but final checks still include the editor test runner and any remaining cases from the Scope and Specification.

A future improvement could store a count in OrderQueue to make count queries O(1). It is not needed for the current feature.

## Next Steps

### Remaining Work

| Order | Task | Owner |
| --- | --- | --- |
| 1 | Review order and queue tests against the Scope and Specification. | Parin |
| 2 | Review name validation and skip boundaries; add any missing model tests. | Monya |
| 3 | Check the program and tests in the grading environment and fix integration issues. | Mohammed |
| 4 | Check menus, run instructions, and the console demonstration. | Nie |
| 5 | Run the complete test suite after final changes. | Mohammed |
| 6 | Verify file attribution and prepare explanations of assigned code. | All members; Nie coordinates |

We are keeping Skip/Requeue. Multiple drinks per order, individual-drink completion, a GUI, and file persistence are outside the current final-build scope.

### Demo Plan

Nie will demonstrate adding Ada and Ben, viewing the queue, skipping Ada, and completing Ben. The demonstration will also show invalid-input recovery, an empty-queue action, passing tests, and the model/controller/view responsibilities.

## Work Attribution

| File | Responsible member | Responsibility |
| --- | --- | --- |
| CoffeeOrder.java | Parin | Order data and constructor validation |
| OrderNode.java | Parin | Linked-node structure |
| OrderQueue.java | Parin | FIFO operations and links |
| CoffeeOrderTest.java | Parin | Order validation and getter tests |
| OrderQueueTest.java | Parin | FIFO, invalid operations, requeue, and list-protection tests |
| CoffeeShopModel.java | Monya | Validation and model operations, including skip |
| CoffeeShopModelTest.java | Monya | Model validation, completion, and skip tests |
| CoffeeShopController.java | Mohammed | Menu actions, recovery, skip handling, and display refresh |
| SipSyncApp.java | Mohammed | Application startup |
| CoffeeShopControllerTest.java | Mohammed | Console flow, recovery, and integration tests |
| ConsoleView.java | Nie | Console input, menus, messages, and order display |
| ConsoleViewTest.java | Nie | View input and output tests |
| README.md | Nie | Instructions, reflection, and coordination |
## GenAI Note

We used GenAI to help troubleshoot problems running our JUnit tests. We did not understand the lower-level details of Java's Security Manager, classpaths, or how the test library interacted with our Java version. GenAI helped us interpret the error messages and identify that the tests were failing during the initialization of `student.TestCase`, before the test methods could run.

Several suggested configuration changes did not resolve the problem. We then used our earlier HokieConnect project as a reference. GenAI helped convert the tests to JUnit 5 and update the library references in `.classpath`. After these changes, all 43 tests passed in a command-line run.