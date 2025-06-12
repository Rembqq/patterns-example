package com.quickorder.patternsexample.active_record_examples;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//
//          Active Record Example #2
//

public class Article {
    public int id;
    public String title;
    public String content;

    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.createStatement().executeUpdate("""
                CREATE TABLE IF NOT EXISTS articles (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255),
                    content TEXT
                );
                CREATE TABLE IF NOT EXISTS comments (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    article_id INT,
                    text TEXT
                );
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid() {
        return title != null && !title.isBlank();
    }

    public void save() {
        if (!isValid()) {
            throw new IllegalArgumentException("Title is required");
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (this.id == 0) {
                String sql = "INSERT INTO articles(title, content) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, this.title);
                stmt.setString(2, this.content);
                stmt.executeUpdate();
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) this.id = keys.getInt(1);
                stmt.close();
            } else {
                String sql = "UPDATE articles SET title = ?, content = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.title);
                stmt.setString(2, this.content);
                stmt.setInt(3, this.id);
                stmt.executeUpdate();
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // зв’язок 1:N — коментарі до статті
    public List<String> getComments() {
        List<String> comments = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT text FROM comments WHERE article_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(rs.getString("text"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }
}

