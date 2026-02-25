/**
 * Binary Search Tree implementation for managing Tasks.
 */
public class TaskBST {
    private BSTNode root;

    public TaskBST() {
        this.root = null;
    }

    // Public Insert
    public void insert(Task task) {
        root = insertRecursive(root, task);
    }

    private BSTNode insertRecursive(BSTNode current, Task task) {
        if (current == null) {
            return new BSTNode(task);
        }

        // Compare by Task ID (String)
        int comp = task.getId().compareTo(current.data.getId());
        if (comp < 0) {
            current.left = insertRecursive(current.left, task);
        } else if (comp > 0) {
            current.right = insertRecursive(current.right, task);
        }
        // If comp == 0, we don't insert duplicate IDs for BST
        return current;
    }

    // Public Search
    public Task search(String id) {
        BSTNode result = searchRecursive(root, id);
        return (result != null) ? result.data : null;
    }

    private BSTNode searchRecursive(BSTNode current, String id) {
        if (current == null || current.data.getId().equals(id)) {
            return current;
        }

        int comp = id.compareTo(current.data.getId());
        if (comp < 0) {
            return searchRecursive(current.left, id);
        } else {
            return searchRecursive(current.right, id);
        }
    }

    // Public Delete (Standard BST deletion)
    public void delete(String id) {
        root = deleteRecursive(root, id);
    }

    private BSTNode deleteRecursive(BSTNode current, String id) {
        if (current == null) {
            return null;
        }

        int comp = id.compareTo(current.data.getId());
        if (comp < 0) {
            current.left = deleteRecursive(current.left, id);
        } else if (comp > 0) {
            current.right = deleteRecursive(current.right, id);
        } else {
            // Node to delete found
            // Case 1: No child
            if (current.left == null && current.right == null) {
                return null;
            }
            // Case 2: One child
            if (current.left == null)
                return current.right;
            if (current.right == null)
                return current.left;

            // Case 3: Two children
            // Find smallest in right subtree (Successor)
            Task smallestValue = findSmallest(current.right);
            current.data = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue.getId());
        }
        return current;
    }

    private Task findSmallest(BSTNode root) {
        return root.left == null ? root.data : findSmallest(root.left);
    }

    // Helper to calculate height
    public int getHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(BSTNode current) {
        if (current == null)
            return 0;
        return 1 + Math.max(calculateHeight(current.left), calculateHeight(current.right));
    }
}
