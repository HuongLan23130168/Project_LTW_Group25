package com.example.project_ltw_25.user.dao;

import com.example.project_ltw_25.user.model.ProductType;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ProductTypeDAO {
    private static final Jdbi jdbi = DBDAO.get();

    public List<ProductType> getAllTypes() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, type_name FROM product_types ORDER BY type_name")
                        .mapToBean(ProductType.class)
                        .list()
        );
    }
}