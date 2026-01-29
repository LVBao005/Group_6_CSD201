/**
 * Unit Testing for Mini Jira Stage 1.
 * Tests all edge cases to ensure NO NullPointerException occurs.
 * 
 * This fulfills the requirement: "Viết bộ Unit Test để chứng minh
 * code AI viết không bị lỗi NullPointerException"
 */
public class MiniJiraSystem {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  MINI JIRA STAGE 1 - UNIT TEST SUITE     ║");
        System.out.println("║  Testing: Linked List, Stack, Queue       ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        int testsPassed = 0;
        int totalTests = 8;

        try {
            // TEST 1: Empty List Operations (NPE Prevention)
            System.out.print("Test 1: Empty list operations safety... ");
            JiraColumn emptyCol = new JiraColumn("Empty");
            emptyCol.removeFirst(); // Should not throw NPE
            emptyCol.removeLast(); // Should not throw NPE
            Task result = emptyCol.findAndRemoveTask("NON-EXISTENT");
            if (result == null) {
                System.out.println("✓ PASSED");
                testsPassed++;
            } else {
                System.out.println("✗ FAILED");
            }

            // TEST 2: Null Task Input
            System.out.print("Test 2: Adding null task (null safety)... ");
            JiraColumn col = new JiraColumn("Test");
            col.addTask(null); // Should not throw NPE
            System.out.println("✓ PASSED");
            testsPassed++;

            // TEST 3: Finding Non-existent Task
            System.out.print("Test 3: Finding non-existent task ID... ");
            col.addTask(new Task("ID-1", "Task 1", "Low"));
            Task found = col.findAndRemoveTask("MISSING-ID");
            if (found == null) {
                System.out.println("✓ PASSED");
                testsPassed++;
            } else {
                System.out.println("✗ FAILED");
            }

            // TEST 4: Null Task ID Search
            System.out.print("Test 4: Searching with null task ID... ");
            Task nullSearch = col.findAndRemoveTask(null);
            if (nullSearch == null) {
                System.out.println("✓ PASSED");
                testsPassed++;
            } else {
                System.out.println("✗ FAILED");
            }

            // TEST 5: Task Movement Between Columns
            System.out.print("Test 5: Moving task between columns... ");
            JiraColumn source = new JiraColumn("Source");
            JiraColumn dest = new JiraColumn("Destination");
            source.addTask(new Task("MOVE-1", "Movable Task", "High"));
            Task moved = source.findAndRemoveTask("MOVE-1");
            if (moved != null && moved.getId().equals("MOVE-1")) {
                dest.addTask(moved);
                System.out.println("✓ PASSED");
                testsPassed++;
            } else {
                System.out.println("✗ FAILED");
            }

            // TEST 6: Stack Empty State
            System.out.print("Test 6: Stack pop on empty stack... ");
            ActionStack stack = new ActionStack();
            String popped = stack.popAction();
            if (popped == null) {
                System.out.println("✓ PASSED");
                testsPassed++;
            } else {
                System.out.println("✗ FAILED");
            }

            // TEST 7: Queue Empty State
            System.out.print("Test 7: Queue dequeue on empty queue... ");
            NotificationQueue queue = new NotificationQueue();
            String dequeued = queue.dequeue();
            if (dequeued == null) {
                System.out.println("✓ PASSED");
                testsPassed++;
            } else {
                System.out.println("✗ FAILED");
            }

            // TEST 8: Stack and Queue with Null Values
            System.out.print("Test 8: Stack/Queue null input handling... ");
            stack.pushAction(null); // Should not throw NPE
            queue.enqueue(null); // Should not throw NPE
            System.out.println("✓ PASSED");
            testsPassed++;

        } catch (NullPointerException npe) {
            System.out.println("\n\n✗✗✗ CRITICAL FAILURE ✗✗✗");
            System.out.println("NullPointerException detected!");
            npe.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n\n✗✗✗ UNEXPECTED ERROR ✗✗✗");
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }

        // Summary
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  TEST SUMMARY                              ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.printf("║  Tests Passed: %d/%d                        ║%n", testsPassed, totalTests);
        if (testsPassed == totalTests) {
            System.out.println("║  Status: ✓ ALL TESTS PASSED               ║");
            System.out.println("║  NO NullPointerException detected!        ║");
        } else {
            System.out.println("║  Status: ✗ SOME TESTS FAILED              ║");
        }
        System.out.println("╚════════════════════════════════════════════╝");
    }
}