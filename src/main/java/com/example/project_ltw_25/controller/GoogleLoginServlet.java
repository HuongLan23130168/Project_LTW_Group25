package com.example.project_ltw_25.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;


@WebServlet(name = "GoogleLoginServlet", value = "/google-login")
public class GoogleLoginServlet extends HttpServlet {
    private static final ResourceBundle rb =
            ResourceBundle.getBundle("application");

    private static final String CLIENT_ID =
            rb.getString("google.client_id");

    private static final String REDIRECT_URI =
            rb.getString("google.redirect_uri");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8)
                + "&response_type=code"
                + "&scope=email profile";

        response.sendRedirect(loginUrl);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}