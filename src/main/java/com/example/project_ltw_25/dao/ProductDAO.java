package com.example.project_ltw_25.user.dao;

import com.example.project_ltw_25.user.model.Product;
import com.example.project_ltw_25.user.model.Product_image;
import com.example.project_ltw_25.user.model.Product_variant;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class ProductDAO {
    private static final Jdbi jdbi = DBDAO.get();

    public Product getById(int id) {
        return jdbi.withHandle(handle -> {
            String sqlProduct = """
                SELECT
                    p.*,
                    t.type_name,
                    (SELECT image_url FROM product_images WHERE product_id = p.id ORDER BY id LIMIT 1) AS image_url,
                    (
                        SELECT MAX(d.discount_percent)
                        FROM discounts d
                        WHERE
                            (
                                d.id IN (SELECT discount_id FROM discount_categories WHERE category_id = p.category_id) OR
                                d.id IN (SELECT discount_id FROM discount_product_types WHERE product_type_id = p.product_type_id)
                            )
                            AND NOW() BETWEEN d.start_date AND d.end_date
                    ) AS discount_percent
                FROM products p
                LEFT JOIN product_types t ON p.product_type_id = t.id
                WHERE p.id = :id
            """;

            Optional<Product> productOpt = handle.createQuery(sqlProduct)
                    .bind("id", id)
                    .map((rs, ctx) -> {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setProduct_name(rs.getString("product_name"));
                        p.setProduct_code(rs.getString("product_code"));
                        p.setDescription(rs.getString("description"));
                        p.setImage_url(rs.getString("image_url"));
                        p.setCategory_id(rs.getString("category_id"));
                        // Lấy discount percent, nếu null thì trả về 0
                        p.setDiscountPercent(rs.getObject("discount_percent") != null ? rs.getDouble("discount_percent") : 0.0);

                        String tags = rs.getString("tags");
                        if (tags != null) {
                            String t = tags.toLowerCase();
                            p.setNewProduct(t.contains("new"));
                            p.setBestSeller(t.contains("best"));
                        }
                        return p;
                    }).findFirst();

            if (productOpt.isPresent()) {
                Product product = productOpt.get();

                List<Product_variant> variants = getVariantsByProductId(id);
                product.setVariants(variants);

                product.setImages(handle.createQuery("SELECT * FROM product_images WHERE product_id = :id")
                        .bind("id", id).mapToBean(Product_image.class).list());

                if (!variants.isEmpty()) {
                    OptionalDouble minPriceOpt = variants.stream()
                            .mapToDouble(v -> v.getPrice().doubleValue())
                            .min();

                    if (minPriceOpt.isPresent()) {
                        double minPrice = minPriceOpt.getAsDouble();
                        product.setPrice(minPrice);

                        if (product.getDiscountPercent() > 0) {
                            double newPrice = minPrice * (1 - (product.getDiscountPercent() / 100.0));
                            product.setPrice_new(newPrice);
                        } else {
                            product.setPrice_new(minPrice);
                        }
                    }
                }
                return product;
            }
            return null;
        });
    }

    public List<Product> getRelatedProducts(String categoryId, int currentId) {
        return jdbi.withHandle(handle -> {
            String sql = """
                SELECT
                    p.id, p.product_name,
                    (SELECT image_url FROM product_images WHERE product_id = p.id LIMIT 1) AS image_url,
                    (SELECT MIN(price) FROM product_variants WHERE product_id = p.id) as base_origin_price,
                    p.tags,
                    (
                        SELECT MAX(d.discount_percent)
                        FROM discounts d
                        WHERE
                            (
                                d.id IN (SELECT discount_id FROM discount_categories WHERE category_id = p.category_id) OR
                                d.id IN (SELECT discount_id FROM discount_product_types WHERE product_type_id = p.product_type_id)
                            )
                            AND NOW() BETWEEN d.start_date AND d.end_date
                    ) AS discount_percent
                FROM products p
                WHERE p.category_id = :categoryId AND p.id != :currentId
                ORDER BY RAND()
                LIMIT 4
            """;

            return handle.createQuery(sql)
                    .bind("categoryId", categoryId)
                    .bind("currentId", currentId)
                    .map((rs, ctx) -> {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setProduct_name(rs.getString("product_name"));
                        p.setImage_url(rs.getString("image_url"));

                        double basePrice = rs.getDouble("base_origin_price");
                        p.setPrice(basePrice);

                        double discountPercent = rs.getObject("discount_percent") != null ? rs.getDouble("discount_percent") : 0.0;
                        p.setDiscountPercent(discountPercent);

                        if (discountPercent > 0) {
                            double newPrice = basePrice * (1 - (discountPercent / 100.0));
                            p.setPrice_new(newPrice);
                        } else {
                            p.setPrice_new(basePrice);
                        }

                        String tags = rs.getString("tags");
                        if (tags != null) {
                            String t = tags.toLowerCase();
                            p.setNewProduct(t.contains("new"));
                            p.setBestSeller(t.contains("best"));
                        }
                        return p;
                    }).list();
        });
    }

    public List<Product_variant> getVariantsByProductId(int productId) {
        return jdbi.withHandle(handle -> {
            String sql = """
                SELECT
                    pv.id, pv.variant_code, pv.product_id, pv.style,
                    pv.color, pv.size, pv.material, pv.price,
                    COALESCE(i.stock_quantity, 0) as stock_quantity
                FROM product_variants pv
                LEFT JOIN inventories i ON pv.id = i.variant_id
                WHERE pv.product_id = :pid
            """;

            return handle.createQuery(sql)
                    .bind("pid", productId)
                    .mapToBean(Product_variant.class)
                    .list();
        });
    }
}
