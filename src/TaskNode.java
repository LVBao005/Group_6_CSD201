/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class TaskNode {
    private Task data;
    private TaskNode next;

    public TaskNode(Task data) {
        this.data = data;
        this.next = null;
    }

    public Task getData() {
        return data;
    }

    public void setData(Task data) {
        this.data = data;
    }

    public TaskNode getNext() {
        return next;
    }

    public void setNext(TaskNode next) {
        this.next = next;
    }
}
