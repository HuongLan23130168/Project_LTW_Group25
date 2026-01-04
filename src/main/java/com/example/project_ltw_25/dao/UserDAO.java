package com.example.project_ltw_25.user.dao;

import com.example.project_ltw_25.user.model.User;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class UserDAO {
    private Jdbi jdbi = DBDAO.get();

    // Đăng ký: Lưu mật khẩu đã băm MD5 từ Java
    public boolean register(String name, String email, String hashedPass) {
        try {
            return jdbi.withHandle(handle ->
                    handle.createUpdate("INSERT INTO users (full_name, email, password, role) VALUES (:name, :email, :pass, :role)")
                            .bind("name", name)
                            .bind("email", email)
                            .bind("pass", hashedPass)
                            .bind("role", "2") // Truyền role từ controller vào
                            .execute() > 0
            );
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Đăng nhập truyền thống: Kiểm tra email và pass MD5
    public User login(String email, String hashedPass) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = :email AND password = :pass")
                        .bind("email", email)
                        .bind("pass", hashedPass)
                        .mapToBean(User.class).findFirst().orElse(null)
        );
    }

    // Cập nhật Token duy nhất cho Magic Link
    public void updateToken(String email, String token) {
        jdbi.useHandle(handle ->
                handle.createUpdate("UPDATE users SET token = :token, token_expiry = DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE email = :email")
                        .bind("token", token).bind("email", email).execute()
        );
    }

    // Xác thực token duy nhất
    // Sửa trong UserDAO.java
    public User getUserByToken(String token) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE token = :token")
                        .bind("token", token)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    // Xóa token sau khi dùng (Đảm bảo dùng 1 lần)
    public void clearToken(String email) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE users SET token = NULL WHERE email = :email").bind("email", email).execute());
    }

    // Kiểm tra email có tồn tại trong hệ thống hay không
    public boolean checkEmailExists(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM users WHERE email = :email")
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    // Cập nhật mật khẩu mới (nhận vào chuỗi đã băm MD5 từ Controller)
    public boolean updatePassword(String email, String hashedPass) {
        try {
            return jdbi.withHandle(handle ->
                    handle.createUpdate("UPDATE users SET password = :pass WHERE email = :email")
                            .bind("pass", hashedPass)
                            .bind("email", email)
                            .execute() > 0
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users")
                        .mapToBean(User.class)
                        .list()
        );
    }
}