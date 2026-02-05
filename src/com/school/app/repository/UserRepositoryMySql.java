package com.school.app.repository;

import com.school.app.model.User;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryMySql implements UserRepository {

    public UserRepositoryMySql() {
        initTable();
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver not found", e);
        }
        return DriverManager.getConnection(DbConfig.dbUrl(), DbConfig.USER, DbConfig.PASS);
    }

    private void initTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT NOT NULL AUTO_INCREMENT,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                full_name VARCHAR(100) NOT NULL,
                PRIMARY KEY (id)
            );
        """;
        try (Connection c = connect(); Statement st = c.createStatement()) {
            st.execute(sql);
            // اضافه کردن کاربر پیش‌فرض اگر جدول خالی است
            addDefaultUserIfEmpty();
        } catch (Exception e) {
            throw new RuntimeException("User table init failed: " + e.getMessage(), e);
        }
    }

    private void addDefaultUserIfEmpty() {
        String checkSql = "SELECT COUNT(*) FROM users";
        String insertSql = "INSERT INTO users (username, password, full_name) VALUES (?, ?, ?)";
        try (Connection c = connect();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ps = c.prepareStatement(insertSql)) {
                    ps.setString(1, "admin");
                    ps.setString(2, "1234");
                    ps.setString(3, "System Administrator");
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.err.println("Could not add default user: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name")
                    ));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("findByUsername failed: " + e.getMessage(), e);
        }
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, password, full_name) VALUES (?, ?, ?)";
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) user.setId(keys.getLong(1));
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("save user failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}