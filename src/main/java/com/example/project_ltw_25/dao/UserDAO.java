package com.example.project_ltw_25.user.dao;

import com.example.project_ltw_25.user.model.User;
import org.jdbi.v3.core.Jdbi;
import java.util.List;

public class UserDAO {
    private Jdbi jdbi = DBDAO.get();

    public boolean register(String name, String email, String hashedPass) {
        try {
            return jdbi.withHandle(handle ->
                    handle.createUpdate("INSERT INTO users (full_name, email, password, role) VALUES (:name, :email, :pass, :role)")
                            .bind("name", name)
                            .bind("email", email)
                            .bind("pass", hashedPass)
                            .bind("role", "1")
                            .execute() > 0
            );
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public User login(String email, String hashedPass) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = :email AND password = :pass")
                        .bind("email", email)
                        .bind("pass", hashedPass)
                        .mapToBean(User.class).findFirst().orElse(null)
        );
    }

    public void updateToken(String email, String token) {
        jdbi.useHandle(handle ->
                handle.createUpdate("UPDATE users SET token = :token, token_expiry = DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE email = :email")
                        .bind("token", token).bind("email", email).execute()
        );
    }

    public User getUserByToken(String token) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE token = :token AND token_expiry > NOW()")
                        .bind("token", token)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public void clearToken(String email) {
        jdbi.useHandle(handle -> handle.createUpdate("UPDATE users SET token = NULL WHERE email = :email").bind("email", email).execute());
    }

    // kt email có tồn tại trong hệ thống ko
    public boolean checkEmailExists(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM users WHERE email = :email")
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }


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

    public boolean updateAdminProfile(String email, String fullName, String phone, String address) {
        try {
            return jdbi.withHandle(handle ->
                    handle.createUpdate("UPDATE users SET full_name = :name, phone = :phone, address = :addr WHERE email = :email")
                            .bind("name", fullName)
                            .bind("phone", phone)
                            .bind("addr", address)
                            .bind("email", email)
                            .execute() > 0
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserWithAddress(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = :email")
                        .bind("email", email)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public boolean updateUserInfo(int userId, String fullName, String phone) {
        try {
            return jdbi.withHandle(handle ->
                    handle.createUpdate("UPDATE users SET full_name = :name, phone = :phone WHERE id = :id")
                            .bind("name", fullName)
                            .bind("phone", phone)
                            .bind("id", userId)
                            .execute() > 0
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByEmail(String email) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, fullName, email, role FROM users WHERE email = :email")
                        .bind("email", email)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }
}