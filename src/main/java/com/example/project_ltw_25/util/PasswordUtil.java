package com.example.project_ltw_25.util;

import java.security.MessageDigest;

public class PasswordUtil {
    public static byte[] hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(password.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
