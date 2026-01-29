import java.util.LinkedList;
import java.util.Queue;

/**
 * NotificationQueue - Queue implementation for system notifications.
 * Fulfills the "Queue" requirement of Module 1.
 */
public class NotificationQueue {
    private Queue<String> notifications;

    public NotificationQueue() {
        this.notifications = new LinkedList<>();
    }

    /**
     * Add a notification to the queue.
     * Null-safe implementation.
     */
    public void enqueue(String message) {
        if (message != null) {
            notifications.add(message);
        }
    }

    /**
     * Remove and return the oldest notification.
     * Returns null if queue is empty (no NullPointerException).
     */
    public String dequeue() {
        return notifications.poll();
    }

    /**
     * Display all pending notifications.
     */
    public void displayNotifications() {
        System.out.println("\n--- SYSTEM NOTIFICATIONS (QUEUE) ---");
        if (notifications.isEmpty()) {
            System.out.println("[No notifications]");
            return;
        }
        for (String msg : notifications) {
            System.out.println(" • " + msg);
        }
    }

    /**
     * Check if queue is empty.
     */
    public boolean isEmpty() {
        return notifications.isEmpty();
    }
}
