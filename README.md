# SipSync - First Prototype

SipSync is a console app for single-drink coffee orders. Employees can add
an order, view pending orders, and complete the oldest order first.
The Deliverable 2 Specification takes priority over the original Scope.

## Run the app

Use a JDK 8 or newer. From the project folder, run:

```text
javac -d bin src/*.java
java -cp bin SipSyncApp
```

Menu: `1` Add Order, `2` View Orders, `3` Complete Next Order, `0` Exit.
Drinks: `1` Espresso, `2` Latte, `3` Cappuccino, `4` Americano.
End of input also exits, including during order entry.

For a short demo, enter each of these on a separate line:

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

Ada's Latte appears before Ben's Americano. Completing the next order
removes Ada and displays Ben as the only pending order.

## Run the model and tests

This is an Eclipse Java project. Main classes are in src, tests are in
test, dependencies are in lib, and compiled files go in bin.

Place your course-provided student.jar in lib before running the tests.
The project already references lib/student.jar and Eclipse's JUnit 4
container. All test classes extend student.TestCase and use public
test-prefixed methods, without JUnit annotations.

In Eclipse:

1. Choose File > Import > General > Existing Projects into Workspace.
2. Select the folder containing .project and finish the import.
3. Right-click SipSyncApp.java and choose Run As > Java Application.
4. Right-click test and choose Run As > JUnit Test to run all tests.

To exercise the model without a view, run CoffeeOrderTest,
OrderQueueTest, and CoffeeShopModelTest separately as JUnit tests.
All classes use the default package.

The main classes compile for Java 8 compatibility. The previous test
suite passed all 12 tests before this project was converted. The updated
student.TestCase suite must also be run with the actual course library
in Eclipse; that library could not be copied into this download.

## Design

- `CoffeeOrder` stores an immutable customer name and drink.
- `OrderNode` and `OrderQueue` implement a custom singly linked FIFO queue.
- `CoffeeShopModel` validates names and drink numbers and owns the queue.
- `ConsoleView` reads lines and displays text.
- `CoffeeShopController` parses menu input, calls the model, and refreshes
  the displayed orders after a successful change.
- `SipSyncApp` creates the objects and starts the controller.

The queue uses head and tail links, not a library queue. Enqueue, dequeue,
peek, and isEmpty take O(1). Getting or displaying all orders takes O(n).
The ArrayList returned by getOrders is only a snapshot for display.

## Reflection

### What changed from the specification?

The seven classes and all public signatures listed in the Specification
are kept. The earlier Scope's buttons are replaced by the console menu,
as already decided in the Specification.

The Specification did not list every ConsoleView method. The prototype
adds displayMenu, displayDrinkMenu, readLine, and displayMessage to keep
printing and reading inside the view. A second ConsoleView constructor
accepts a Scanner and PrintStream so console tests can supply input and
capture output. This adds an output field to the specified input field.

Names and drink text are trimmed when stored. getOrders returns a new
list, and getDrinkMenu returns an array copy. These small details keep
outside code from changing the queue or fixed menu through returned data.
Validation remains in the layers specified: parsing in the controller,
order rules in the model, and null/empty protection in the queue.

### Did the model stand alone?

Yes. CoffeeOrderTest, OrderQueueTest, and CoffeeShopModelTest call the
model directly. They do not create a ConsoleView or controller. No model
class reads input, prints output, or knows about the view. The controller
asks for the current orders and passes them to displayOrders.

### What happened to the unknowns?

Connecting actions to the model is handled by start's menu choices.
Refreshing the list is handled after successful add and complete actions.
FIFO is checked by adding Ada before Ben and asserting that Ada leaves
first. Wiring graphical buttons remains unexplored because this
prototype uses the console.

### What does not work yet?

The specified single-drink MVP is implemented. Multiple drinks per order,
partial completion of a multi-drink order, a GUI, and skip/requeue are
not implemented. Orders are only stored while the app is running.

The included test suite is intentionally an initial subset of the
Specification's test plan. It contains 12 tests, rather than complete
coverage. No empty test methods or deliberately failing tests are used.
Missing automated cases include:

| Area | Tests still needed | Owner |
| --- | --- | --- |
| CoffeeOrder | Null name; null or blank drink; trimming | Parin |
| OrderQueue | Null enqueue leaves existing orders unchanged; empty peek; modifying a returned list does not change the queue | Parin |
| CoffeeShopModel | Null/blank/special-character names; valid apostrophes and hyphens; drink choices 0 and 5; empty completion; menu-copy protection | Monya |
| CoffeeShopController | Invalid main-menu and drink inputs; retry after errors; empty completion; EOF at each prompt; add again after clearing | Mohammed |
| ConsoleView | Main menu and drink menu output; prompts; null constructor arguments | Nie |

## Next Steps

1. Finish the missing queue and model tests above. Parin owns queue/data
   tests, and Monya owns model tests. Fix any failures before changing
   behavior.
2. Add the missing console tests. Mohammed owns controller recovery and
   EOF tests. Nie owns view output tests and a manual run of the demo.
3. Mohammed leads integration; all members run the app and tests together.
   No known single-drink MVP feature remains to implement, but the full
   test plan and team review still need to be finished.
4. Add the Deliverable 4 change feature described below, then test it
   through both the model and the console.

### Change feature: skip and requeue the oldest order

An employee will be able to move the oldest order to the back of the
queue without completing it. This changes queue state and is a new action
reachable through the controller and view. It is not part of this MVP.

| Class/method | Predicted change | Owner |
| --- | --- | --- |
| CoffeeShopModel.skipNextOrder() | Add a method that dequeues the oldest order, enqueues it at the tail, and returns it; empty queue throws NoSuchElementException | Monya |
| CoffeeShopController.start() | Accept menu option 4 and call a new private skipOrder helper | Mohammed |
| CoffeeShopController.skipOrder() | Call the model, show a confirmation, refresh the list, and handle an empty queue | Mohammed |
| ConsoleView.displayMenu() | Add the Skip/Requeue action | Nie |
| CoffeeShopModelTest | Test Ada/Ben becomes Ben/Ada, a single order stays pending, and empty skip throws without changing state | Monya |
| CoffeeShopControllerTest | Test action 4, refreshed order, empty skip, and completing the new head after a skip | Mohammed |
| ConsoleViewTest | Check that the new action is displayed | Nie |
| OrderQueueTest | Review dequeue-then-enqueue coverage needed by the feature | Parin |

CoffeeOrder, OrderNode, OrderQueue, and SipSyncApp should not need
implementation changes. Existing enqueue/dequeue operations already
support moving an order. Existing displayOrders can show the new order
without changes. We will compare these predictions with Deliverable 4.

### Stretch goals

We commit to skip/requeue for Deliverable 4. We defer the original
multiple-drink and individual-drink completion goals because both need
a larger change to CoffeeOrder and completion rules. We also defer a GUI.
These are out of the next deliverable's planned scope.

## Work Attribution

This table assigns ownership and follow-up work using the Specification's
division of work. It does not claim that the assigned work is already
completed. Before submission, update the last column with what each
member actually wrote, revised, reviewed, or tested.

| Class or test file | Assigned owner | Assigned work 
| --- | --- | --- | --- |
| CoffeeOrder.java | Parin | Review order validation and immutable fields |
| OrderNode.java | Parin | Review the order and next links 
| OrderQueue.java | Parin | Review FIFO links and the last-removal case |
| CoffeeOrderTest.java | Parin | Run and extend constructor/getter tests |
| OrderQueueTest.java | Parin | Run FIFO tests and add missing queue cases | 
| CoffeeShopModel.java | Monya | Review name/drink validation and completion | 
| CoffeeShopModelTest.java | Monya | Run and extend model validation tests |
| CoffeeShopController.java | Mohammed | Review menu handling, retries, EOF, and refresh |
| SipSyncApp.java | Mohammed | Review startup wiring and lead integration | 
| CoffeeShopControllerTest.java | Mohammed | Run integration tests and add recovery cases |
| ConsoleView.java | Nie | Review prompts, menus, input, and list display | 
| ConsoleViewTest.java | Nie | Run and extend view tests; check the console demo | 
