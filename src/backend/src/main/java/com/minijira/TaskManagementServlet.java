package com.minijira;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "TaskManagementServlet", urlPatterns = "/api/tasks/manage")
public class TaskManagementServlet extends HttpServlet {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final TaskRepository repository = new TaskRepository();

    private static class CrudRequest {
        String action;
        TaskPayload task;
        Integer taskId;
    }

    private static class TaskPayload {
        Integer id;
        String title;
        String description;
        String status;
        String priority;
        Integer estimateHours;
        Integer dependsOn;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CrudRequest command = gson.fromJson(req.getReader(), CrudRequest.class);
        if (command == null || command.action == null) {
            respondError(resp, "Y\u00eau c\u1ea7u kh\u00f4ng h\u1ee3p l\u1ec7");
            return;
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        switch (command.action.trim().toLowerCase()) {
            case "create":
                handleCreate(command.task, resp);
                break;
            case "delete":
                handleDelete(command.taskId, resp);
                break;
            case "update":
                handleUpdate(command.task, resp);
                break;
            default:
                respondError(resp, "Ch\u01b0a h\u1ed7 tr\u1ee3 h\u00e0nh \u0111\u1ed9ng: " + command.action);
        }
    }

    private void handleCreate(TaskPayload payload, HttpServletResponse resp) throws IOException {
        if (payload == null || payload.title == null || payload.title.isBlank()) {
            respondError(resp, "Thi\u1ebfu ti\u00eau \u0111\u1ec1 nhi\u1ec7m v\u1ee5");
            return;
        }
        String status = normalizeStatus(payload.status);
        String priority = payload.priority == null ? "Trung b\u00ecnh" : payload.priority.trim();
        int estimate = payload.estimateHours == null ? 2 : Math.max(1, payload.estimateHours);
        Integer depends = payload.dependsOn;
        int nextId = repository.nextId();
        Task created = new Task(nextId, payload.title.trim(), payload.description == null ? "" : payload.description.trim(), status, priority, estimate, depends);
        boolean inserted = repository.insert(created);
        if (!inserted) {
            respondError(resp, "Kh\u00f4ng th\u1ec3 th\u00eam nhi\u1ec7m v\u1ee5 m\u1edbi");
            return;
        }
        TaskManager.reload();
        respondSuccess(resp, Map.of("task", created));
    }

    private void handleDelete(Integer taskId, HttpServletResponse resp) throws IOException {
        if (taskId == null) {
            respondError(resp, "Thi\u1ebfu taskId \u0111\u1ec3 x\u00f3a");
            return;
        }
        boolean deleted = repository.delete(taskId);
        if (!deleted) {
            respondError(resp, "Kh\u00f4ng t\u00ecm th\u1ea5y nhi\u1ec7m v\u1ee5 \u0111\u1ec3 x\u00f3a");
            return;
        }
        TaskManager.reload();
        respondSuccess(resp, Map.of("deleted", taskId));
    }

    private void handleUpdate(TaskPayload payload, HttpServletResponse resp) throws IOException {
        if (payload == null || payload.title == null || payload.title.isBlank() || payload.estimateHours == null || payload.id == null) {
            respondError(resp, "Thi\u1ebfu th\u00f4ng tin c\u1eadp nh\u1eadt");
            return;
        }
        Task updating = new Task(payload.id, payload.title.trim(), payload.description == null ? "" : payload.description.trim(),
                normalizeStatus(payload.status), payload.priority == null ? "Trung b\u00ecnh" : payload.priority.trim(),
                Math.max(1, payload.estimateHours), payload.dependsOn);
        boolean updated = repository.update(updating);
        if (!updated) {
            respondError(resp, "C\u1eadp nh\u1eadt th\u1ea5t b\u1ea1i");
            return;
        }
        TaskManager.reload();
        respondSuccess(resp, Map.of("task", updating));
    }

    private String normalizeStatus(String raw) {
        if (raw == null) {
            return "TODO";
        }
        String candidate = raw.trim().toUpperCase();
        return (candidate.equals("DOING") || candidate.equals("DONE")) ? candidate : "TODO";
    }

    private void respondError(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> payload = new HashMap<>();
        payload.put("success", false);
        payload.put("message", message);
        resp.getWriter().write(gson.toJson(payload));
    }

    private void respondSuccess(HttpServletResponse resp, Map<String, Object> payload) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.putAll(payload);
        resp.getWriter().write(gson.toJson(result));
    }
}
