/**
 * Node for Binary Search Tree storing Tasks.
 */
public class BSTNode {
    Task data;
    BSTNode left, right;

    public BSTNode(Task data) {
        this.data = data;
        this.left = this.right = null;
    }
}
