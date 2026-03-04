public class TaskBSTTest {
    private static int testsPassed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("========================================================");
        System.out.println("            TASK BST - MANUAL TEST (CHI TIET)");
        System.out.println("========================================================");

        runTest("TC01 - Empty Tree State", TaskBSTTest::testEmptyTree);
        runTest("TC02 - Insert and Search", TaskBSTTest::testInsertAndSearch);
        runTest("TC03 - Insert Duplicate ID", TaskBSTTest::testInsertDuplicateId);
        runTest("TC04 - Delete Leaf Node", TaskBSTTest::testDeleteLeaf);
        runTest("TC05 - Delete Node with One Child", TaskBSTTest::testDeleteOneChild);
        runTest("TC06 - Delete Node with Two Children", TaskBSTTest::testDeleteTwoChildren);
        runTest("TC07 - Height Calculation", TaskBSTTest::testGetHeight);

        printSummary();
    }

    private static void runTest(String testName, Runnable testMethod) {
        totalTests++;
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println(testName);
        System.out.println("--------------------------------------------------------");
        try {
            testMethod.run();
            System.out.println("=> KET LUAN: PASS");
            testsPassed++;
        } catch (Throwable e) {
            System.out.println("=> KET LUAN: FAIL");
            System.out.println("   Ly do: " + e.getMessage());
        }
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("                       SUMMARY");
        System.out.println("========================================================");
        System.out.println("Tong so test: " + totalTests);
        System.out.println("PASS: " + testsPassed);
        System.out.println("FAIL: " + (totalTests - testsPassed));
        System.out.println("========================================================");
    }

    private static void printInput(String description) {
        System.out.println("[Input] " + description);
    }

    private static void printOutput(String description) {
        System.out.println("[Output] " + description);
    }

    private static String describeTask(Task task) {
        return task == null ? "null" : task.toString();
    }

    private static boolean checkEquals(String label, Object expected, Object actual) {
        boolean pass = (expected == null) ? actual == null : expected.equals(actual);
        System.out.println("  - " + label);
        System.out.println("    Expected: " + expected);
        System.out.println("    Actual  : " + actual);
        System.out.println("    Result  : " + (pass ? "PASS" : "FAIL"));
        return pass;
    }

    private static boolean checkNull(String label, Object actual) {
        boolean pass = actual == null;
        System.out.println("  - " + label);
        System.out.println("    Expected: null");
        System.out.println("    Actual  : " + actual);
        System.out.println("    Result  : " + (pass ? "PASS" : "FAIL"));
        return pass;
    }

    private static boolean checkNotNull(String label, Object actual) {
        boolean pass = actual != null;
        System.out.println("  - " + label);
        System.out.println("    Expected: not null");
        System.out.println("    Actual  : " + actual);
        System.out.println("    Result  : " + (pass ? "PASS" : "FAIL"));
        return pass;
    }

    private static void testEmptyTree() {
        TaskBST bst = new TaskBST();
        printInput("BST moi tao, chua insert task nao.");

        int height = bst.getHeight();
        Task found = bst.search("ANY-ID");

        printOutput("getHeight() = " + height);
        printOutput("search(\"ANY-ID\") = " + describeTask(found));

        boolean ok = true;
        ok &= checkEquals("Height of empty tree", 0, height);
        ok &= checkNull("Search in empty tree", found);
        if (!ok) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testInsertAndSearch() {
        TaskBST bst = new TaskBST();
        Task t1 = new Task("T1", "Task 1", "High");
        Task t2 = new Task("T2", "Task 2", "Medium");
        Task t0 = new Task("T0", "Task 0", "Low");

        printInput("Insert lan luot: " + t1 + ", " + t2 + ", " + t0);
        bst.insert(t1);
        bst.insert(t2);
        bst.insert(t0);

        Task r1 = bst.search("T1");
        Task r2 = bst.search("T2");
        Task r0 = bst.search("T0");
        Task r4 = bst.search("T4");

        printOutput("search(\"T1\") = " + describeTask(r1));
        printOutput("search(\"T2\") = " + describeTask(r2));
        printOutput("search(\"T0\") = " + describeTask(r0));
        printOutput("search(\"T4\") = " + describeTask(r4));

        boolean ok = true;
        ok &= checkNotNull("Find T1", r1);
        ok &= checkNotNull("Find T2", r2);
        ok &= checkNotNull("Find T0", r0);
        ok &= checkEquals("Title of T1", "Task 1", r1 == null ? null : r1.getTitle());
        ok &= checkNull("Find non-existing T4", r4);
        if (!ok) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testInsertDuplicateId() {
        TaskBST bst = new TaskBST();
        Task original = new Task("T1", "Task 1", "High");
        Task duplicate = new Task("T1", "Task 1 Duplicate", "Low");

        printInput("Insert original: " + original);
        bst.insert(original);
        printInput("Insert duplicate (same ID): " + duplicate);
        bst.insert(duplicate);

        Task result = bst.search("T1");
        printOutput("search(\"T1\") sau khi insert duplicate = " + describeTask(result));

        boolean ok = true;
        ok &= checkNotNull("T1 still exists", result);
        ok &= checkEquals("Original data is preserved", "Task 1", result == null ? null : result.getTitle());
        if (!ok) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testDeleteLeaf() {
        TaskBST bst = new TaskBST();
        Task t1 = new Task("T1", "Task 1", "High");
        bst.insert(t1);

        printInput("Ban dau co 1 node: " + t1 + ", sau do delete(\"T1\").");
        bst.delete("T1");

        Task found = bst.search("T1");
        int height = bst.getHeight();
        printOutput("search(\"T1\") = " + describeTask(found));
        printOutput("getHeight() = " + height);

        boolean ok = true;
        ok &= checkNull("T1 deleted", found);
        ok &= checkEquals("Height after deleting only node", 0, height);
        if (!ok) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testDeleteOneChild() {
        TaskBST bst = new TaskBST();
        Task t2 = new Task("T2", "Task 2", "Medium");
        Task t1 = new Task("T1", "Task 1", "High");

        printInput("Insert root " + t2 + " va node con trai " + t1 + ", sau do delete(\"T2\").");
        bst.insert(t2);
        bst.insert(t1);
        bst.delete("T2");

        Task rootDeleted = bst.search("T2");
        Task childRemain = bst.search("T1");
        int height = bst.getHeight();

        printOutput("search(\"T2\") = " + describeTask(rootDeleted));
        printOutput("search(\"T1\") = " + describeTask(childRemain));
        printOutput("getHeight() = " + height);

        boolean ok = true;
        ok &= checkNull("T2 deleted", rootDeleted);
        ok &= checkNotNull("T1 remains", childRemain);
        ok &= checkEquals("Height after delete root with one child", 1, height);
        if (!ok) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testDeleteTwoChildren() {
        TaskBST bst = new TaskBST();
        Task t2 = new Task("T2", "Task 2", "Medium");
        Task t1 = new Task("T1", "Task 1", "High");
        Task t3 = new Task("T3", "Task 3", "Low");

        printInput("Insert 3 node: " + t2 + " (root), " + t1 + " (left), " + t3 + " (right); delete(\"T2\").");
        bst.insert(t2);
        bst.insert(t1);
        bst.insert(t3);
        bst.delete("T2");

        Task deleted = bst.search("T2");
        Task left = bst.search("T1");
        Task right = bst.search("T3");
        int height = bst.getHeight();

        printOutput("search(\"T2\") = " + describeTask(deleted));
        printOutput("search(\"T1\") = " + describeTask(left));
        printOutput("search(\"T3\") = " + describeTask(right));
        printOutput("getHeight() = " + height);

        boolean ok = true;
        ok &= checkNull("T2 deleted", deleted);
        ok &= checkNotNull("T1 remains", left);
        ok &= checkNotNull("T3 remains", right);
        ok &= checkEquals("Height after delete root with two children", 2, height);
        if (!ok) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }

    private static void testGetHeight() {
        TaskBST bst = new TaskBST();
        printInput("Insert theo thu tu: M, A, Z, B va theo doi chieu cao moi buoc.");

        int h0 = bst.getHeight();
        bst.insert(new Task("M", "Middle", "Medium"));
        int h1 = bst.getHeight();
        bst.insert(new Task("A", "Left", "Low"));
        int h2 = bst.getHeight();
        bst.insert(new Task("Z", "Right", "High"));
        int h3 = bst.getHeight();
        bst.insert(new Task("B", "Left-Right", "Low"));
        int h4 = bst.getHeight();

        printOutput("Height ban dau = " + h0);
        printOutput("Height sau insert M = " + h1);
        printOutput("Height sau insert A = " + h2);
        printOutput("Height sau insert Z = " + h3);
        printOutput("Height sau insert B = " + h4);

        boolean ok = true;
        ok &= checkEquals("Empty tree height", 0, h0);
        ok &= checkEquals("Height with 1 node", 1, h1);
        ok &= checkEquals("Height with 2 nodes (left)", 2, h2);
        ok &= checkEquals("Height with 3 nodes (balanced)", 2, h3);
        ok &= checkEquals("Height with deeper branch", 3, h4);
        if (!ok) {
            throw new RuntimeException("Co dieu kien khong dat.");
        }
    }
}
