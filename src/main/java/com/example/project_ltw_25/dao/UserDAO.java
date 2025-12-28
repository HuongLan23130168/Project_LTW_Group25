package com.example.project_ltw_25.dao;

import com.example.project_ltw_25.model.User;
import org.jdbi.v3.core.Jdbi;

public class UserDAO {
    private Jdbi jdbi = DBDAO.get();

    // Hàm xử lý Đăng nhập
    public User login(String email, String pass) {
        // Sử dụng MD5(?) để băm mật khẩu trước khi so sánh với UNHEX trong DB
        String query = "SELECT * FROM users WHERE email = :email AND password = UNHEX(MD5(:pass))";

        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .bind("email", email)
                        .bind("pass", pass)
                        .map((rs, ctx) -> new User(
                                rs.getInt("id"),
                                rs.getString("full_name"),
                                rs.getDate("birth"),
                                rs.getString("gender"),
                                rs.getString("email"),
                                "********", // Không nên lấy mật khẩu binary ra object User để bảo mật
                                rs.getString("phone"),
                                rs.getString("role"),
                                rs.getString("address")
                        ))
                        .findFirst()
                        .orElse(null)
        );
    }

    // Hàm xử lý Đăng ký
    public boolean register(String name, String email, String pass) {
        try {
            return jdbi.withHandle(handle ->
                    handle.createUpdate("INSERT INTO users (full_name, email, password, role) " +
                                    "VALUES (:name, :email, UNHEX(MD5(:pass)), 'user')")
                            .bind("name", name)
                            .bind("email", email)
                            .bind("pass", pass)
                            .execute() > 0
            );
        } catch (Exception e) {
            return false;
        }
    }
}