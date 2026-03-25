package com.minijira;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "MoveTaskServlet", urlPatterns = "/api/tasks/move")
public class MoveTaskServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        MoveCommand command;
        try {
            command = gson.fromJson(req.getReader(), MoveCommand.class);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("error", "Malformed JSON")));
            return;
        }
        if (command == null || command.taskId <= 0 || command.targetColumn == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("error", "taskId and targetColumn are required")));
            return;
        }

        String destination = TaskManager.normalize(command.targetColumn);
        if ("DOING".equals(destination)) {
            Set<Integer> blockers = TaskManager.blockingParents(command.taskId);
            if (!blockers.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write(gson.toJson(Map.of(
                        "success", false,
                        "message", "Kh\u00f4ng th\u1ec3 chuy\u1ec3n task n\u00e0y v\u00ec c\u00e1c task ti\u1ec1n \u0111\u1ec1 ch\u01b0a xong: " + blockers)));
                return;
            }
        }
        boolean moved = TaskManager.moveTask(command.taskId, command.targetColumn);
        resp.setStatus(moved ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
        Map<String, Object> response = new HashMap<>();
        response.put("success", moved);
        if (!moved) {
            response.put("message", "Unable to move task, check the ID and target column");
        }
        resp.getWriter().write(gson.toJson(response));
    }

    private static class MoveCommand {
        int taskId;
        String targetColumn;
    }
}
