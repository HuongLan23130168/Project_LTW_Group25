package com.example.project_ltw_25.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String URL ="jdbc:mysql://localhost:3306/login_web?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
