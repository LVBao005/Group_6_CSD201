/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo 3 cột riêng biệt
        JiraColumn todoColumn = new JiraColumn("To Do");
        JiraColumn doingColumn = new JiraColumn("Doing");
        JiraColumn doneColumn = new JiraColumn("Done");

        System.out.println("--- MINI JIRA BOARD ---");

        // 2. Thêm task vào cột To Do
        Task task1 = new Task("J-01", "Design Database", "High");
        Task task2 = new Task("J-02", "Setup API", "Medium");
        todoColumn.addTask(task1);
        todoColumn.addTask(task2);

        // 3. Hiển thị trạng thái bảng lúc đầu
        todoColumn.displayTasks();
        doingColumn.displayTasks();
        doneColumn.displayTasks();

        // 4. DEMO: Di chuyển Task 1 từ To Do sang Doing
        // (Trong Linked List: Xóa ở List này và thêm vào List kia)
        System.out.println("\n>>> Action: Moving J-01 from TO DO to DOING...");
        
        todoColumn.removeFirst(); // Xóa J-01 khỏi To Do
        doingColumn.addTask(task1); // Thêm J-01 vào Doing

        // 5. Hiển thị lại bảng sau khi di chuyển
        System.out.println("\n--- UPDATED JIRA BOARD ---");
        todoColumn.displayTasks();
        doingColumn.displayTasks();
        doneColumn.displayTasks();
    }
}