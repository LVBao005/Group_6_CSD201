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

@WebServlet(name = "SearchTaskServlet", urlPatterns = "/api/tasks/search")
public class SearchTaskServlet extends HttpServlet {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            respondError(resp, "Thi\u1ebfu tham s\u1ed1 id");
            return;
        }
        int taskId;
        try {
            taskId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            respondError(resp, "Id ph\u1ea3i l\u00e0 s\u1ed1 nguy\u00ean");
            return;
        }

        TaskManager.ensureInitialized();
        Task task = TaskManager.findById(taskId);
        if (task == null) {
            respondError(resp, "Kh\u00f4ng t\u00ecm th\u1ea5y task #" + taskId);
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> payload = new HashMap<>();
        payload.put("success", true);
        payload.put("task", task);
        resp.getWriter().write(gson.toJson(payload));
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
}
