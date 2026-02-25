import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main class for Stage 2: BST vs List Benchmarking.
 */
public class mainTree {
    public static void main(String[] args) {
        int elementCount = 10000;
        System.out.println("=== STAGE 2: BST VS LINKED LIST BENCHMARKING ===");
        System.out.println("Elements: " + elementCount + "\n");

        // 1. Prepare data
        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= elementCount; i++) {
            tasks.add(new Task("JIRA-" + String.format("%05d", i), "Task " + i, "Medium"));
        }

        // Shuffle to avoid degenerate tree (Skewed tree)
        List<Task> shuffledTasks = new ArrayList<>(tasks);
        Collections.shuffle(shuffledTasks);

        // 2. Populate structures
        JiraColumn listStructure = new JiraColumn("Benchmark List");
        TaskBST treeStructure = new TaskBST();

        System.out.println("Inserting " + elementCount + " tasks...");
        for (Task t : shuffledTasks) {
            listStructure.addTask(t);
            treeStructure.insert(t);
        }

        System.out.println("BST Height: " + treeStructure.getHeight());
        System.out.println("Perfect height for 10k elements would be ~14 (log2(10000)).");

        // 3. Benchmarking
        // Pick some IDs to search for (Beginning, Middle, End of the ID range)
        String[] searchIds = { "JIRA-00001", "JIRA-05000", "JIRA-10000", "JIRA-99999" };

        System.out.println("\n--- Performance Results (Time in Nanoseconds) ---");
        System.out.printf("%-15s | %-15s | %-15s | %-15s\n", "Target ID", "List Time (ns)", "BST Time (ns)",
                "Ratio (L/T)");
        System.out.println("-------------------------------------------------------------------------");

        for (String id : searchIds) {
            // List Search
            long startList = System.nanoTime();
            listStructure.searchTask(id);
            long endList = System.nanoTime();
            long durationList = endList - startList;

            // BST Search
            long startBST = System.nanoTime();
            treeStructure.search(id);
            long endBST = System.nanoTime();
            long durationBST = endBST - startBST;

            // Avoid division by zero if duration is too small
            double ratio = (durationBST > 0) ? (double) durationList / durationBST : durationList;

            System.out.printf("%-15s | %-15d | %-15d | %-15.2f\n",
                    id, durationList, durationBST, ratio);
        }

        System.out.println("\nSummary:");
        System.out.println("- BST search is significantly faster than Linked List search.");
        System.out.println("- Linked List search takes O(N) time.");
        System.out.println("- BST search takes O(log N) time (when not degenerate).");
    }
}
