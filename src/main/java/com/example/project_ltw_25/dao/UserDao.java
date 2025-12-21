package com.example.project_ltw_25.dao;

import com.example.project_ltw_25.model.User;

import java.sql.*;

public class UserDao {
    private Connection conn;

    public UserDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/webapp",
                    "root",
                    "123456"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("fullname"),
                        rs.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void register(String fullname, String email, String password) {
        String sql = "INSERT INTO users(fullname,email,password) VALUES(?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
