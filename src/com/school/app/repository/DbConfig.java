package com.school.app.repository;

public class DbConfig {
    public static final String HOST = "localhost";
    public static final int PORT = 3306;
    public static final String DB_NAME = "school_app";

    public static final String USER = "root";
    public static final String PASS = "8093";

    public static String serverUrl() {
        return "jdbc:mysql://" + HOST + ":" + PORT +
                "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    public static String dbUrl() {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME +
                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }
}
