package com.example.project_ltw_25.dao;

import com.example.project_ltw_25.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductDao {

    private static final Map<Integer, Product> productMap = new HashMap<>();
    private static boolean loaded = false;

    private void loadFromDB() {
        if (loaded) {
            System.out.println(">>> MAP already loaded");
            return;
        }

        System.out.println(">>> Loading products from DB");

        List<Product> list = DBDAO.get().withHandle(h ->
                h.createQuery("""
                                SELECT
                                    p.id,
                                    p.product_name        AS productName,
                                
                                    c.category_name       AS categoryName,
                                    t.type_name           AS productTypeName,
                                
                                    MIN(v.price)          AS price,
                                    MIN(v.image_url)      AS imageUrl
                                
                                FROM products p
                                JOIN categories c ON p.category_id = c.id
                                LEFT JOIN product_types t ON p.product_type_id = t.id
                                LEFT JOIN product_variants v ON v.product_id = p.id
                                
                                GROUP BY
                                    p.id, p.product_name,
                                    c.category_name, t.type_name;
                                """)
                        .mapToBean(Product.class)
                        .list()
        );

        System.out.println(">>> DB RETURNED: " + list.size());

        for (Product p : list) {
            productMap.put(p.getId(), p);
        }

        loaded = true;
    }

    public List<Product> getListProduct() {
        loadFromDB();
        return new ArrayList<>(productMap.values());
    }

    public Product getProduct(int id) {
        loadFromDB();
        return productMap.get(id);
    }

    public List<Product> filterProducts(
            String priceRange,
            String[] roomCodes,      // PHÒNG
            String[] typeCodes,      // CÂY / HOA
            String sort,
            int page,
            int size
    ) {
        StringBuilder sql = new StringBuilder("""
                    SELECT
                        p.id,
                        p.product_name AS productName,
                        c.category_name AS categoryName,
                        t.type_name AS productTypeName,
                        MIN(v.price) AS price,
                        MIN(v.image_url) AS imageUrl
                    FROM products p
                    JOIN categories c ON p.category_id = c.id      -- PHÒNG
                    LEFT JOIN product_types t ON p.product_type_id = t.id  -- LOẠI
                    LEFT JOIN product_variants v ON v.product_id = p.id
                    WHERE 1=1
                """);

        if (roomCodes != null && roomCodes.length > 0)
            sql.append(" AND c.id IN (<rooms>)");

        if (typeCodes != null && typeCodes.length > 0)
            sql.append(" AND t.type_code IN (<types>)");

        sql.append("""
                    GROUP BY p.id, p.product_name, c.category_name, t.type_name
                """);

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "1" -> sql.append(" HAVING MIN(v.price) < 500000");
                case "2" -> sql.append(" HAVING MIN(v.price) BETWEEN 500000 AND 1000000");
                case "3" -> sql.append(" HAVING MIN(v.price) BETWEEN 1000000 AND 3000000");
                case "4" -> sql.append(" HAVING MIN(v.price) > 3000000");
            }
        }

        if ("price-asc".equals(sort))
            sql.append(" ORDER BY price ASC");
        else if ("price-desc".equals(sort))
            sql.append(" ORDER BY price DESC");

        sql.append(" LIMIT :limit OFFSET :offset");

        return DBDAO.get().withHandle(h -> {
            var q = h.createQuery(sql.toString())
                    .bind("limit", size)
                    .bind("offset", (page - 1) * size);

            if (roomCodes != null && roomCodes.length > 0) {
                q.bindList("rooms",
                        Arrays.stream(roomCodes)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList())
                );
            }

            if (typeCodes != null && typeCodes.length > 0)
                q.bindList("types", Arrays.asList(typeCodes));

            return q.mapToBean(Product.class).list();
        });
    }

    public int countProducts(
            String priceRange,
            String[] rooms,
            String[] categories
    ) {
        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(*) FROM (
                        SELECT p.id
                        FROM products p
                        JOIN categories c ON p.category_id = c.id
                        LEFT JOIN product_types t ON p.product_type_id = t.id
                        LEFT JOIN product_variants v ON v.product_id = p.id
                        WHERE 1=1
                """);

        if (rooms != null && rooms.length > 0)
            sql.append(" AND c.id IN (<rooms>)");

        if (categories != null && categories.length > 0)
            sql.append(" AND t.type_code IN (<types>)");

        sql.append(" GROUP BY p.id ");

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "1" -> sql.append(" HAVING MIN(v.price) < 500000");
                case "2" -> sql.append(" HAVING MIN(v.price) BETWEEN 500000 AND 1000000");
                case "3" -> sql.append(" HAVING MIN(v.price) BETWEEN 1000000 AND 3000000");
                case "4" -> sql.append(" HAVING MIN(v.price) > 3000000");
            }
        }

        sql.append(") x");

        return DBDAO.get().withHandle(h -> {
            var q = h.createQuery(sql.toString());

            if (rooms != null && rooms.length > 0) {
                q.bindList("rooms",
                        Arrays.stream(rooms)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList())
                );
            }

            if (categories != null && categories.length > 0)
                q.bindList("types", Arrays.asList(categories));


            return q.mapTo(int.class).one();
        });
    }


}
