//package com.example.demoweb1.controller;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//import com.example.demoweb1.dao.ContactDAO;
////import jdk.internal.org.jline.reader.History;
//
//@WebServlet("/contact")
//public class ContactServlet extends HttpServlet {
//
//        @Override
//        protected void doPost(HttpServletRequest request, HttpServletResponse response)
//                throws ServletException, IOException {
//
//            request.setCharacterEncoding("UTF-8");
//
//            String name = request.getParameter("name");
//            String email = request.getParameter("email");
//            String message = request.getParameter("message");
//
//            ContactDAO.save(name, email, message);
//
            // quay lại contact.jsp + báo thành công
//            response.sendRedirect(
//                    request.getContextPath() + "/frontend/contact.jsp?success=true"
//            );
//        }
//    }

package com.example.demoweb1.controller;

import com.example.demoweb1.dao.ContactDAO;
import com.example.demoweb1.model.Contact;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String fullName = req.getParameter("full_name");
        String email = req.getParameter("email");
        String message = req.getParameter("message");

        Contact contact = new Contact();
        contact.setFullName(fullName);
        contact.setEmail(email);
        contact.setMessage(message);
        contact.setStatus("NEW"); // mặc định

        ContactDAO.insert(contact);



       // resp.sendRedirect("contact.jsp?success=true");

       //  quay lại contact.jsp + báo thành công
        resp.sendRedirect(
                req.getContextPath() + "/frontend/contact.jsp?success=true"
        );

    }
}

