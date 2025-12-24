package com.example.demoweb1.dao;

import com.example.demoweb1.model.Contact;
import org.jdbi.v3.core.Jdbi;

public class ContactDAO {

    public static void insert(Contact contact) {

        Jdbi jdbi = DBDAO.get();

        jdbi.useHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO contacts(full_name, email, message, status) " +
                                        "VALUES (:fullName, :email, :message, :status)"
                        )
                        .bind("fullName", contact.getFullName())
                        .bind("email", contact.getEmail())
                        .bind("message", contact.getMessage())
                        .bind("status", contact.getStatus())
                        .execute()
        );
    }
}