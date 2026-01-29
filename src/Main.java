/**
 * Main demonstration class for Mini Jira Stage 1.
 * Demonstrates Linked List, Stack, and Queue functionality.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize Jira Columns (Linked Lists)
        JiraColumn todo = new JiraColumn("To Do");
        JiraColumn doing = new JiraColumn("Doing");
        JiraColumn done = new JiraColumn("Done");

        // Initialize Module 1 structures: Stack and Queue
        ActionStack history = new ActionStack();
        NotificationQueue notifier = new NotificationQueue();

        System.out.println("=== MINI JIRA: STAGE 1 DEMONSTRATION ===");
        System.out.println("Module 1: Linked List + Stack + Queue\n");

        // 1. Adding Tasks to To Do column
        System.out.println("--- Step 1: Adding tasks to 'To Do' ---");
        todo.addTask(new Task("JIRA-101", "Implement Linked List", "High"));
        todo.addTask(new Task("JIRA-102", "Implement Stack", "Medium"));
        todo.addTask(new Task("JIRA-103", "Implement Queue", "Medium"));
        notifier.enqueue("Tasks JIRA-101, 102, 103 created.");
        todo.displayTasks();

        // 2. Moving Task from To Do -> Doing
        System.out.println("\n--- Step 2: Moving JIRA-101 to 'Doing' ---");
        Task t1 = todo.findAndRemoveTask("JIRA-101");
        if (t1 != null) {
            doing.addTask(t1);
            history.pushAction("Moved " + t1.getId() + " from To Do to Doing");
            notifier.enqueue(t1.getId() + " is now in progress.");
            System.out.println("✓ Task moved successfully");
        }

        todo.displayTasks();
        doing.displayTasks();

        // 3. Completing Task: Doing -> Done
        System.out.println("\n--- Step 3: Completing JIRA-101 ---");
        Task tDone = doing.findAndRemoveTask("JIRA-101");
        if (tDone != null) {
            done.addTask(tDone);
            history.pushAction("Moved " + tDone.getId() + " from Doing to Done");
            notifier.enqueue(tDone.getId() + " completed!");
            System.out.println("✓ Task completed");
        }

        doing.displayTasks();
        done.displayTasks();

        // 4. Display Stack and Queue
        history.displayHistory();
        notifier.displayNotifications();

        System.out.println("\n=== STAGE 1 DEMONSTRATION COMPLETE ===");
    }
}