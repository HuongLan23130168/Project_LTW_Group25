package com.example.demoweb1.util;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;

@WebServlet(name = "DBConnection", value = "/DBConnection")
public class DBConnection extends HttpServlet {

        public static Connection getConnection() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/ltw_group25?useUnicode=true&characterEncoding=UTF-8",
                        "root",
                        ""
                );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

    }

}