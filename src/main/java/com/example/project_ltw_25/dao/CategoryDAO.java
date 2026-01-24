package com.example.project_ltw_25.user.dao;

import com.example.project_ltw_25.user.model.Category;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class CategoryDAO {
    private static final Jdbi jdbi = DBDAO.get();

    public List<Category> getAllCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, category_name FROM categories ORDER BY category_name")
                        .mapToBean(Category.class)
                        .list()
        );
    }
}