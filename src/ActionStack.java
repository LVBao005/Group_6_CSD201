import java.util.Stack;

/**
 * ActionStack - Stack implementation for Undo functionality.
 * Fulfills the "Stack" requirement of Module 1.
 */
public class ActionStack {
    private Stack<String> actions;

    public ActionStack() {
        this.actions = new Stack<>();
    }

    /**
     * Push an action onto the stack.
     * Null-safe implementation.
     */
    public void pushAction(String action) {
        if (action != null) {
            actions.push(action);
        }
    }

    /**
     * Pop the most recent action.
     * Returns null if stack is empty (no NullPointerException).
     */
    public String popAction() {
        if (actions.isEmpty()) {
            return null;
        }
        return actions.pop();
    }

    /**
     * Display the action history.
     */
    public void displayHistory() {
        System.out.println("\n--- ACTION HISTORY (STACK) ---");
        if (actions.isEmpty()) {
            System.out.println("[No history recorded]");
            return;
        }
        for (int i = actions.size() - 1; i >= 0; i--) {
            System.out.println(" [" + (actions.size() - i) + "] " + actions.get(i));
        }
    }

    /**
     * Check if stack is empty.
     */
    public boolean isEmpty() {
        return actions.isEmpty();
    }
}
