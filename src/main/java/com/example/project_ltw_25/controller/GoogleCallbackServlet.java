package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.util.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(name = "GoogleCallbackServlet", value = "/google-callback")
public class GoogleCallbackServlet extends HttpServlet {
    private static final ResourceBundle rb =
            ResourceBundle.getBundle("application");

    private static final String CLIENT_ID =
            rb.getString("google.client_id");

    private static final String CLIENT_SECRET =
            rb.getString("google.client_secret");

    private static final String REDIRECT_URI =
            rb.getString("google.redirect_uri");


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("===== GOOGLE CALLBACK DEBUG =====");
        System.out.println("code = " + request.getParameter("code"));
        System.out.println("CLIENT_ID = " + CLIENT_ID);
        System.out.println("CLIENT_SECRET = " + CLIENT_SECRET);
        System.out.println("REDIRECT_URI = " + REDIRECT_URI);

        String code = request.getParameter("code");
        if (code == null) {
            response.sendRedirect(request.getContextPath() +"/frontend/login.jsp");
            return;
        }

        String body =
                "code=" + code +
                        "&client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&redirect_uri=" + REDIRECT_URI +
                        "&grant_type=authorization_code";

        String tokenResponse =
                HttpClientUtil.post("https://oauth2.googleapis.com/token", body);

        JsonObject tokenJson =
                JsonParser.parseString(tokenResponse).getAsJsonObject();


        String accessToken =
                tokenJson.get("access_token").getAsString();

        /* 2️⃣ Lấy user info */
        String userInfoUrl =
                "https://www.googleapis.com/oauth2/v2/userinfo"
                        + "?access_token=" + accessToken;

        String userResponse =
                HttpClientUtil.get(userInfoUrl);

        JsonObject userJson =
                JsonParser.parseString(userResponse).getAsJsonObject();

        String email = userJson.get("email").getAsString();
        String name  = userJson.get("name").getAsString();

        /* 3️⃣ Lưu session */
        HttpSession session = request.getSession();
        session.setAttribute("userName", name);
        session.setAttribute("userEmail", email);
        session.setAttribute("loginType", "GOOGLE");

        /* 4️⃣ Redirect home */
        response.sendRedirect(request.getContextPath() +"/frontend/home.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}