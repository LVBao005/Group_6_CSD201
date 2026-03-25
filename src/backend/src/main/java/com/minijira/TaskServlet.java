package com.minijira;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "TaskServlet", urlPatterns = "/api/tasks")
public class TaskServlet extends HttpServlet {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskManager.ensureInitialized();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("columns", TaskManager.snapshot());
        payload.put("dependencies", TaskManager.dependencySnapshot());
        payload.put("cycleDetected", TaskManager.hasDependencyCycle());

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(payload));
    }
}
