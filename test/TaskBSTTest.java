/**
 * Standalone Test Suite for TaskBST.
 * Tests all core functionalities and prints detailed results to console.
 */
public class TaskBSTTest {
    private static int testsPassed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("       TASK BST - UNIT TEST SUITE            ");
        System.out.println("==============================================\n");

        runTest("Empty Tree State", TaskBSTTest::testEmptyTree);
        runTest("Insert and Search", TaskBSTTest::testInsertAndSearch);
        runTest("Insert Duplicate ID", TaskBSTTest::testInsertDuplicateId);
        runTest("Delete Leaf Node", TaskBSTTest::testDeleteLeaf);
        runTest("Delete One Child Node", TaskBSTTest::testDeleteOneChild);
        runTest("Delete Two Children Node", TaskBSTTest::testDeleteTwoChildren);
        runTest("Tree Height Calculation", TaskBSTTest::testGetHeight);

        printSummary();
    }

    private static void runTest(String testName, Runnable testMethod) {
        totalTests++;
        System.out.print("Testing: " + testName + "... ");
        try {
            testMethod.run();
            System.out.println(" [PASSED]");
            testsPassed++;
        } catch (Throwable e) {
            System.out.println(" [FAILED]");
            System.err.println("   Error: " + e.getMessage());
            // Uncomment next line for detailed stack trace during debugging
            // e.printStackTrace();
        }
    }

    private static void printSummary() {
        System.out.println("\n==============================================");
        System.out.println("                TEST SUMMARY                ");
        System.out.println("==============================================");
        System.out.printf("  Total Tests:  %d\n", totalTests);
        System.out.printf("  Passed:       %d\n", testsPassed);
        System.out.printf("  Failed:       %d\n", (totalTests - testsPassed));
        System.out.println("==============================================");
        if (testsPassed == totalTests) {
            System.out.println("  ✓ ALL TESTS PASSED SUCCESSFULLY!          ");
        } else {
            System.out.println("  ✗ SOME TESTS FAILED. CHECK LOGS ABOVE.     ");
        }
        System.out.println("==============================================");
    }

    // --- Assertions Helpers ---

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        throw new RuntimeException(message + " (Expected: " + expected + ", Actual: " + actual + ")");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected == actual)
            return;
        throw new RuntimeException(message + " (Expected: " + expected + ", Actual: " + actual + ")");
    }

    private static void assertNull(Object actual, String message) {
        if (actual == null)
            return;
        throw new RuntimeException(message + " (Expected: null, Actual: " + actual + ")");
    }

    private static void assertNotNull(Object actual, String message) {
        if (actual != null)
            return;
        throw new RuntimeException(message + " (Expected: NOT null)");
    }

    // --- Test Methods ---

    private static void testEmptyTree() {
        TaskBST bst = new TaskBST();
        assertEquals(0, bst.getHeight(), "Height of empty tree should be 0");
        assertNull(bst.search("ANY-ID"), "Search in empty tree should return null");
    }

    private static void testInsertAndSearch() {
        TaskBST bst = new TaskBST();
        Task t1 = new Task("T1", "Task 1", "High");
        Task t2 = new Task("T2", "Task 2", "Medium");
        Task t3 = new Task("T0", "Task 0", "Low");

        bst.insert(t1);
        bst.insert(t2);
        bst.insert(t3);

        assertNotNull(bst.search("T1"), "Should find T1");
        assertNotNull(bst.search("T2"), "Should find T2");
        assertNotNull(bst.search("T0"), "Should find T0");
        assertEquals("Task 1", bst.search("T1").getTitle(), "Incorrect title for T1");
        assertNull(bst.search("T4"), "Should not find non-existent ID");
    }

    private static void testInsertDuplicateId() {
        TaskBST bst = new TaskBST();
        Task t1 = new Task("T1", "Task 1", "High");
        Task t1Dup = new Task("T1", "Task 1 Duplicate", "Low");

        bst.insert(t1);
        bst.insert(t1Dup);

        Task result = bst.search("T1");
        assertEquals("Task 1", result.getTitle(), "BST should keep the original task when duplicate ID is inserted");
    }

    private static void testDeleteLeaf() {
        TaskBST bst = new TaskBST();
        Task t1 = new Task("T1", "Task 1", "High");
        bst.insert(t1);
        bst.delete("T1");
        assertNull(bst.search("T1"), "Task T1 should be deleted");
        assertEquals(0, bst.getHeight(), "Tree height should be 0 after deleting only node");
    }

    private static void testDeleteOneChild() {
        TaskBST bst = new TaskBST();
        Task t2 = new Task("T2", "Task 2", "Medium");
        Task t1 = new Task("T1", "Task 1", "High");
        bst.insert(t2);
        bst.insert(t1);

        bst.delete("T2");
        assertNull(bst.search("T2"), "T2 should be deleted");
        assertNotNull(bst.search("T1"), "T1 should still exist");
        assertEquals(1, bst.getHeight(), "Height should be 1 after deleting root with one child");
    }

    private static void testDeleteTwoChildren() {
        TaskBST bst = new TaskBST();
        Task t2 = new Task("T2", "Task 2", "Medium");
        Task t1 = new Task("T1", "Task 1", "High");
        Task t3 = new Task("T3", "Task 3", "Low");

        bst.insert(t2);
        bst.insert(t1);
        bst.insert(t3);

        bst.delete("T2");
        assertNull(bst.search("T2"), "T2 should be deleted");
        assertNotNull(bst.search("T1"), "T1 should still exist");
        assertNotNull(bst.search("T3"), "T3 should still exist");
        assertEquals(2, bst.getHeight(), "Height should be 2 after deleting root with two children");
    }

    private static void testGetHeight() {
        TaskBST bst = new TaskBST();
        assertEquals(0, bst.getHeight(), "Empty height check");

        bst.insert(new Task("M", "Middle", "Medium"));
        assertEquals(1, bst.getHeight(), "Height with 1 node");

        bst.insert(new Task("A", "Left", "Low"));
        assertEquals(2, bst.getHeight(), "Height with 2 nodes (left biased)");

        bst.insert(new Task("Z", "Right", "High"));
        assertEquals(2, bst.getHeight(), "Height with 3 nodes (balanced)");

        bst.insert(new Task("B", "Left-Right", "Low"));
        assertEquals(3, bst.getHeight(), "Height with 4 nodes (deeper branch)");
    }
}
