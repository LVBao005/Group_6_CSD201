package com.minijira;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TaskRepository {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public TaskRepository() {
        Properties props = new Properties();
        try (InputStream in = TaskRepository.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
            String driverClass = props.getProperty("driverClass", "com.mysql.cj.jdbc.Driver");
            Class.forName(driverClass);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Unable to load DB configuration, falling back to embedded defaults: " + e.getMessage());
        }
        jdbcUrl = props.getProperty("jdbcUrl", "jdbc:mysql://localhost:3307/jiradb");
        username = props.getProperty("username", "root");
        password = props.getProperty("password", "123456");
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT id, title, description, status, priority, estimate_hours, depends_on FROM tasks";
        try (Connection conn = connection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String status = rs.getString("status");
                String priority = rs.getString("priority");
                int estimate = rs.getInt("estimate_hours");
                Integer depends = rs.getObject("depends_on") == null ? null : rs.getInt("depends_on");
                tasks.add(new Task(id, title, description, status, priority, estimate, depends));
            }
        } catch (SQLException e) {
            System.err.println("Failed to query tasks: " + e.getMessage());
        }
        return tasks;
    }

    public boolean updateStatus(int taskId, String status) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (Connection conn = connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, taskId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to update task status: " + e.getMessage());
            return false;
        }
    }

    public int nextId() {
        String query = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM tasks";
        try (Connection conn = connection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        } catch (SQLException e) {
            System.err.println("Failed to compute next id: " + e.getMessage());
        }
        return 1;
    }

    public boolean insert(Task task) {
        String sql = "INSERT INTO tasks (id, title, description, status, priority, estimate_hours, depends_on) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, task.getId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getStatus());
            ps.setString(5, task.getPriority());
            ps.setInt(6, task.getEstimateHours());
            if (task.getDependsOn() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, task.getDependsOn());
            }
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to insert task: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to delete task: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, priority = ?, estimate_hours = ?, depends_on = ? WHERE id = ?";
        try (Connection conn = connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getPriority());
            ps.setInt(5, task.getEstimateHours());
            if (task.getDependsOn() == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, task.getDependsOn());
            }
            ps.setInt(7, task.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Failed to update task: " + e.getMessage());
            return false;
        }
    }
}
