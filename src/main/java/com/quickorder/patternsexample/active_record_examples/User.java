package com.quickorder.patternsexample.active_record_examples;

import java.sql.*;

//
//          Active Record Example #1
//

public class User {
    public int id;
    public String name;
    public String email;

    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    static {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            conn.createStatement().executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    email VARCHAR(255)
                )
            """);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (this.id == 0) {
                String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, this.name);
                stmt.setString(2, this.email);
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    this.id = keys.getInt(1);
                }
                stmt.close();
            } else {
                String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.name);
                stmt.setString(2, this.email);
                stmt.setInt(3, this.id);
                stmt.executeUpdate();
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        if (this.id == 0) return;
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
            stmt.close();
            this.id = 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User findById(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.id = rs.getInt("id");
                u.name = rs.getString("name");
                u.email = rs.getString("email");
                rs.close();
                stmt.close();
                return u;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}


