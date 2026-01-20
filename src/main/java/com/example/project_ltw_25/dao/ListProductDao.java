package com.example.project_ltw_25.user.dao;

import com.example.project_ltw_25.user.model.Discount;
import com.example.project_ltw_25.user.model.Product;
import java.util.*;

public class ListProductDao {

    private static final Map<Integer, Product> productMap = new HashMap<>();
    private static boolean loaded = false;

    // --- HÀM MAPPING CHUNG ---
    private Product mapRowToProduct(java.sql.ResultSet rs) throws java.sql.SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setProduct_name(rs.getString("product_name"));
        p.setCategory_name(rs.getString("category_name"));
        p.setType_name(rs.getString("type_name"));
        p.setPrice(rs.getDouble("price"));
        p.setImage_url(rs.getString("image_url"));

        // Lấy d_id từ kết quả query (đã dùng COALESCE trong SQL)
        int discountId = rs.getInt("d_id");
        if (discountId > 0) {
            Discount d = new Discount();
            d.setId(discountId);
            d.setDiscount_percent(rs.getBigDecimal("d_percent"));
            d.setStart_date(rs.getTimestamp("d_start"));
            d.setEnd_date(rs.getTimestamp("d_end"));
            p.setDiscount(d);
        }
        return p;
    }

    // --- 1. LOAD TẤT CẢ (DÙNG CHO TRANG CHỦ / CACHE) ---
    private void loadFromDB() {
        if (loaded) return;
        List<Product> list = DBDAO.get().withHandle(h ->
                h.createQuery("""
                    SELECT 
                        p.id, p.product_name, c.category_name, t.type_name,
                        MIN(v.price) AS price, MIN(v.image_url) AS image_url,
                        COALESCE(dt.discount_id, dc.discount_id) AS d_id,
                        COALESCE(d2.discount_percent, d1.discount_percent) AS d_percent,
                        COALESCE(d2.start_date, d1.start_date) AS d_start,
                        COALESCE(d2.end_date, d1.end_date) AS d_end
                    FROM products p
                    JOIN categories c ON p.category_id = c.id
                    LEFT JOIN product_types t ON p.product_type_id = t.id
                    LEFT JOIN product_variants v ON v.product_id = p.id
                    LEFT JOIN discount_categories dc ON c.id = dc.category_id
                    LEFT JOIN discounts d1 ON dc.discount_id = d1.id
                    LEFT JOIN discount_product_types dt ON t.id = dt.product_type_id
                    LEFT JOIN discounts d2 ON dt.discount_id = d2.id
                    GROUP BY p.id, c.category_name, t.type_name, d_id, d_percent, d_start, d_end
                """)
                        .map((rs, ctx) -> mapRowToProduct(rs))
                        .list()
        );
        for (Product p : list) productMap.put(p.getId(), p);
        loaded = true;
    }

    // --- 2. FILTER (DÙNG CHO TRANG DANH SÁCH + BỘ LỌC) ---
    public List<Product> filterProducts(String priceRange, String[] roomCodes, String[] typeCodes, String sort, int page, int size) {
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    p.id, p.product_name, c.category_name, t.type_name,
                    MIN(v.price) AS price, MIN(v.image_url) AS image_url,
                    COALESCE(dt.discount_id, dc.discount_id) AS d_id,
                    COALESCE(d2.discount_percent, d1.discount_percent) AS d_percent,
                    COALESCE(d2.start_date, d1.start_date) AS d_start,
                    COALESCE(d2.end_date, d1.end_date) AS d_end
                FROM products p
                JOIN categories c ON p.category_id = c.id
                LEFT JOIN product_types t ON p.product_type_id = t.id
                LEFT JOIN product_variants v ON v.product_id = p.id
                LEFT JOIN discount_categories dc ON c.id = dc.category_id
                LEFT JOIN discounts d1 ON dc.discount_id = d1.id
                LEFT JOIN discount_product_types dt ON t.id = dt.product_type_id
                LEFT JOIN discounts d2 ON dt.discount_id = d2.id
                WHERE 1=1
                """);

        if (roomCodes != null && roomCodes.length > 0) sql.append(" AND c.id IN (<rooms>) ");
        if (typeCodes != null && typeCodes.length > 0) sql.append(" AND t.type_code IN (<types>) ");

        sql.append(" GROUP BY p.id, c.category_name, t.type_name, d_id, d_percent, d_start, d_end ");

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "1" -> sql.append(" HAVING MIN(v.price) < 500000 ");
                case "2" -> sql.append(" HAVING MIN(v.price) BETWEEN 500000 AND 1000000 ");
                case "3" -> sql.append(" HAVING MIN(v.price) BETWEEN 1000000 AND 3000000 ");
                case "4" -> sql.append(" HAVING MIN(v.price) > 3000000 ");
            }
        }

        if ("price-asc".equals(sort)) sql.append(" ORDER BY price ASC ");
        else if ("price-desc".equals(sort)) sql.append(" ORDER BY price DESC ");
        else sql.append(" ORDER BY p.id DESC ");

        sql.append(" LIMIT :limit OFFSET :offset ");

        return DBDAO.get().withHandle(h -> {
            var q = h.createQuery(sql.toString()).bind("limit", size).bind("offset", (page - 1) * size);
            if (roomCodes != null && roomCodes.length > 0)
                q.bindList("rooms", Arrays.stream(roomCodes).map(Integer::parseInt).toList());
            if (typeCodes != null && typeCodes.length > 0)
                q.bindList("types", Arrays.asList(typeCodes));
            return q.map((rs, ctx) -> mapRowToProduct(rs)).list();
        });
    }

    // --- 3. COUNT (ĐỂ PHÂN TRANG CHÍNH XÁC) ---
    public int countProducts(String priceRange, String[] rooms, String[] categories) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*) FROM (
                    SELECT p.id
                    FROM products p
                    JOIN categories c ON p.category_id = c.id
                    LEFT JOIN product_types t ON p.product_type_id = t.id
                    LEFT JOIN product_variants v ON v.product_id = p.id
                    WHERE 1=1
                """);
        if (rooms != null && rooms.length > 0) sql.append(" AND c.id IN (<rooms>) ");
        if (categories != null && categories.length > 0) sql.append(" AND t.type_code IN (<types>) ");

        sql.append(" GROUP BY p.id ");

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "1" -> sql.append(" HAVING MIN(v.price) < 500000 ");
                case "2" -> sql.append(" HAVING MIN(v.price) BETWEEN 500000 AND 1000000 ");
                case "3" -> sql.append(" HAVING MIN(v.price) BETWEEN 1000000 AND 3000000 ");
                case "4" -> sql.append(" HAVING MIN(v.price) > 3000000 ");
            }
        }
        sql.append(") AS temp_table");

        return DBDAO.get().withHandle(h -> {
            var q = h.createQuery(sql.toString());
            if (rooms != null && rooms.length > 0) q.bindList("rooms", Arrays.stream(rooms).map(Integer::parseInt).toList());
            if (categories != null && categories.length > 0) q.bindList("types", Arrays.asList(categories));
            return q.mapTo(Integer.class).one();
        });
    }

    public List<Product> getListProduct() { loadFromDB(); return new ArrayList<>(productMap.values()); }
    public Product getProduct(int id) { loadFromDB(); return productMap.get(id); }
}