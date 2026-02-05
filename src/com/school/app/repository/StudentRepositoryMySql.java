package com.school.app.repository;

import com.school.app.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepositoryMySql implements StudentRepository {

    public StudentRepositoryMySql() {
        initDbAndTable();
    }

    private Connection connectServer() throws SQLException {
        loadDriver();
        return DriverManager.getConnection(DbConfig.serverUrl(), DbConfig.USER, DbConfig.PASS);
    }

    private Connection connectDb() throws SQLException {
        loadDriver();
        return DriverManager.getConnection(DbConfig.dbUrl(), DbConfig.USER, DbConfig.PASS);
    }

    private void loadDriver() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found. Add mysql-connector-j.jar to classpath.", e);
        }
    }

    private void initDbAndTable() {
        // Create database
        String createDbSql = "CREATE DATABASE IF NOT EXISTS " + DbConfig.DB_NAME;
        try (Connection c = connectServer(); Statement st = c.createStatement()) {
            st.execute(createDbSql);
        } catch (Exception e) {
            throw new RuntimeException("DB create failed: " + e.getMessage(), e);
        }

        // Create table (بدون grade)
        String createTableSql = """
            CREATE TABLE IF NOT EXISTS students (
              id BIGINT NOT NULL AUTO_INCREMENT,
              national_code VARCHAR(20) NOT NULL UNIQUE,
              first_name VARCHAR(50) NOT NULL,
              last_name VARCHAR(50) NOT NULL,
              class_name VARCHAR(20) NOT NULL,
              PRIMARY KEY (id)
            );
        """;

        try (Connection c = connectDb(); Statement st = c.createStatement()) {
            st.execute(createTableSql);
        } catch (Exception e) {
            throw new RuntimeException("Table init failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM students ORDER BY id DESC";
        List<Student> out = new ArrayList<>();

        try (Connection c = connectDb();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(map(rs));
            return out;

        } catch (Exception e) {
            throw new RuntimeException("findAll failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> findById(long id) {
        String sql = "SELECT * FROM students WHERE id=?";
        try (Connection c = connectDb(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("findById failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> findByNationalCode(String nationalCode) {
        String sql = "SELECT * FROM students WHERE national_code=?";
        try (Connection c = connectDb(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nationalCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("findByNationalCode failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Student save(Student s) {
        String sql = "INSERT INTO students(national_code, first_name, last_name, class_name) VALUES(?,?,?,?)";
        try (Connection c = connectDb();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getNationalCode());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getClassName());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getLong(1));
            }
            return s;

        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new RuntimeException("nationalCode must be unique");
        } catch (Exception e) {
            throw new RuntimeException("save failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Student update(long id, Student s) {
        String sql = "UPDATE students SET national_code=?, first_name=?, last_name=?, class_name=? WHERE id=?";
        try (Connection c = connectDb(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getNationalCode());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getClassName());
            ps.setLong(5, id);

            int affected = ps.executeUpdate();
            if (affected == 0) throw new RuntimeException("student not found");

            s.setId(id);
            return s;

        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new RuntimeException("nationalCode must be unique");
        } catch (Exception e) {
            throw new RuntimeException("update failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection c = connectDb(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("delete failed: " + e.getMessage(), e);
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(
                rs.getLong("id"),
                rs.getString("national_code"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("class_name")
        );
    }
}
