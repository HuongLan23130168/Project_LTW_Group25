package com.example.project_ltw_25.user.dao;

import com.example.project_ltw_25.user.model.Discount;
import com.example.project_ltw_25.user.model.Product;
import com.example.project_ltw_25.user.model.Product_variant;
import com.example.project_ltw_25.user.model.Product_image;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ProductDAO {
    private static final Jdbi jdbi = DBDAO.get();

    // 1. THÊM SẢN PHẨM 
    public boolean addProduct(Product product, Product_variant variant, List<Product_image> images) {
        try {
            return jdbi.inTransaction(handle -> {
                String productQuery = """
                            INSERT INTO products (product_code, product_name, product_type_id, category_id, description) 
                            VALUES (:product_code, :product_name, :product_type_id, :category_id, :description)
                        """;
                int productId = handle.createUpdate(productQuery)
                        .bindBean(product)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one();

                String variantQuery = """
                            INSERT INTO product_variants (product_id, style, color, size, material, price, image_url) 
                            VALUES (:product_id, :style, :color, :size, :material, :price, :image_url)
                        """;
                handle.createUpdate(variantQuery)
                        .bind("product_id", productId)
                        .bindBean(variant)
                        .execute();

                if (images != null && !images.isEmpty()) {
                    String imageQuery = "INSERT INTO product_images (product_id, image_url) VALUES (:product_id, :image_url)";
                    for (Product_image image : images) {
                        handle.createUpdate(imageQuery)
                                .bind("product_id", productId)
                                .bind("image_url", image.getImage_url())
                                .execute();
                    }
                }
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. LẤY CHI TIẾT SẢN PHẨM 
    public Product getById(int id) {
        return jdbi.withHandle(handle -> {
            String sql = """
                        SELECT p.*, 
                               (SELECT MIN(price) FROM product_variants WHERE product_id = p.id) AS min_price,
                               GREATEST(COALESCE(d1.discount_percent, 0), COALESCE(d2.discount_percent, 0)) AS final_discount_percent,
                               IF(COALESCE(d1.discount_percent, 0) >= COALESCE(d2.discount_percent, 0), d1.id, d2.id) AS d_id,
                               IF(COALESCE(d1.discount_percent, 0) >= COALESCE(d2.discount_percent, 0), d1.discount_name, d2.discount_name) AS d_name,
                               IF(COALESCE(d1.discount_percent, 0) >= COALESCE(d2.discount_percent, 0), d1.start_date, d2.start_date) AS d_start,
                               IF(COALESCE(d1.discount_percent, 0) >= COALESCE(d2.discount_percent, 0), d1.end_date, d2.end_date) AS d_end
                        FROM products p
                        LEFT JOIN discount_categories dc ON dc.category_id = p.category_id
                        LEFT JOIN discounts d1 ON d1.id = dc.discount_id AND (NOW() BETWEEN d1.start_date AND d1.end_date)
                        LEFT JOIN discount_product_types pdt ON pdt.product_type_id = p.product_type_id
                        LEFT JOIN discounts d2 ON d2.id = pdt.discount_id AND (NOW() BETWEEN d2.start_date AND d2.end_date)
                        WHERE p.id = :id  
                        LIMIT 1
                    """;

            return handle.createQuery(sql)
                    .bind("id", id) 
                    .map((rs, ctx) -> {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setProduct_name(rs.getString("product_name"));
                        p.setProduct_code(rs.getString("product_code"));
                        p.setCategory_id(rs.getInt("category_id"));
                        p.setCategory_id(rs.getInt("product_type_id"));
                        p.setDescription(rs.getString("description"));
                        p.setPrice(rs.getDouble("min_price"));

                        if (rs.getBigDecimal("final_discount_percent") != null && rs.getBigDecimal("final_discount_percent").doubleValue() > 0) {
                            Discount disc = new Discount();
                            disc.setId(rs.getInt("d_id"));
                            disc.setDiscount_name(rs.getString("d_name"));
                            disc.setDiscount_percent(rs.getInt("final_discount_percent")); 
                            disc.setStart_date(rs.getTimestamp("d_start"));
                            disc.setEnd_date(rs.getTimestamp("d_end"));
                            p.setDiscount(disc);
                        }
                        return p;
                    })
                    .findOne()
                    .map(p -> {
                        p.setVariants(handle.createQuery("""
                                        SELECT pv.*, COALESCE(i.stock_quantity, 0) as stock 
                                        FROM product_variants pv 
                                        LEFT JOIN inventories i ON pv.id = i.variant_id 
                                        WHERE pv.product_id = :id
                                        """)
                                .bind("id", id).mapToBean(Product_variant.class).list());

                        p.setImages(handle.createQuery("SELECT * FROM product_images WHERE product_id = :id")
                                .bind("id", id).mapToBean(Product_image.class).list());
                        return p;
                    }).orElse(null);
        });
    }

    // 3. LẤY TẤT CẢ SẢN PHẨM 
    public List<Product> getAll() {
        return jdbi.withHandle(handle -> {
            String sql = """
                        SELECT p.*, pv.price, pv.image_url 
                        FROM products p
                        LEFT JOIN product_variants pv ON p.id = pv.product_id
                        GROUP BY p.id
                    """;
            return handle.createQuery(sql)
                    .mapToBean(Product.class)
                    .list();
        });
    }

    // 4. LẤY SẢN PHẨM THEO DANH MỤC 
    public List<Product> getByCategory(int categoryId) {
        return jdbi.withHandle(handle -> {
            String sql = """
                        SELECT p.*, 
                               MIN(pv.price) as price,  -- LUÔN lấy giá nhỏ nhất làm giá đại diện
                               pv.image_url as image_url 
                        FROM products p
                        LEFT JOIN product_variants pv ON p.id = pv.product_id
                        WHERE p.category_id = :categoryId
                        GROUP BY p.id
                    """;
            return handle.createQuery(sql)
                    .bind("categoryId", categoryId)
                    .mapToBean(Product.class)
                    .list();
        });
    }

    // 5. LẤY SẢN PHẨM TƯƠNG TỰ 
    public List<Product> getRelatedProducts(int categoryId, int currentProductId) {
        return jdbi.withHandle(handle -> {
            String sql = """
                    SELECT p.*, pv.price, pv.image_url 
                    FROM products p 
                    LEFT JOIN product_variants pv ON p.id = pv.product_id 
                    WHERE p.category_id = :catId AND p.id != :currentId 
                    GROUP BY p.id 
                    LIMIT 8
                    """;
            return handle.createQuery(sql)
                    .bind("catId", categoryId)
                    .bind("currentId", currentProductId)
                    .mapToBean(Product.class)
                    .list();
        });
    }

    // 6. LẤY THEO TYPE 
    public List<Product> getByProductType(int typeId, int limit) {
        return jdbi.withHandle(handle -> {
            String sql = """
                        SELECT p.*, 
                               MAX(pv.price) as price, 
                               MAX(pv.image_url) as image_url
                        FROM products p 
                        LEFT JOIN product_variants pv ON p.id = pv.product_id 
                        WHERE p.product_type_id = :typeId 
                        GROUP BY p.id 
                        LIMIT :limit
                    """;
            return handle.createQuery(sql)
                    .bind("typeId", typeId)
                    .bind("limit", limit)
                    .mapToBean(Product.class)
                    .list();
        });
    }

    // 7. LẤY SẢN PHẨM MỚI NHẤT
    public List<Product> getNewestProducts(int limit) {
        return jdbi.withHandle(handle -> {
            String sql = """
                        SELECT p.*, 
                               MIN(pv.price) as price,  -- Lấy giá thấp nhất
                               MAX(pv.image_url) as image_url
                        FROM products p
                        INNER JOIN new_products np ON p.id = np.product_id
                        LEFT JOIN product_variants pv ON p.id = pv.product_id 
                        GROUP BY p.id
                        ORDER BY np.added_at DESC 
                        LIMIT :limit
                    """;
            return handle.createQuery(sql)
                    .bind("limit", limit)
                    .map((rs, ctx) -> {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setProduct_name(rs.getString("product_name"));
                        p.setDescription(rs.getString("description"));
                        p.setPrice(rs.getDouble("price"));
                        p.setImage_url(rs.getString("image_url"));
                        return p;
                    })
                    .list();
        });
    }

    // 8. LẤY SẢN PHẨM BÁN CHẠY
    public List<Product> getBestSellerProducts(int limit) {
        return jdbi.withHandle(handle -> {
            String sql = """
            SELECT p.*, 
                   -- PHẢI đặt tên alias là 'price' để khớp với Model Product
                   (SELECT MIN(price) FROM product_variants WHERE product_id = p.id) as price,
                   -- Lấy ảnh từ bảng product_images (vì bảng variants của bạn đang bị NULL)
                   (SELECT image_url FROM product_images WHERE product_id = p.id ORDER BY id ASC LIMIT 1) as image_url,
                   -- Logic tính giảm giá
                   COALESCE((
                        SELECT MAX(d.discount_percent)
                        FROM discounts d
                        WHERE NOW() BETWEEN d.start_date AND d.end_date
                        AND (
                            EXISTS (SELECT 1 FROM discount_product_types dpt WHERE dpt.discount_id = d.id AND dpt.product_type_id = p.product_type_id)
                            OR
                            EXISTS (SELECT 1 FROM discount_categories dc WHERE dc.discount_id = d.id AND dc.category_id = p.category_id)
                        )
                   ), 0) AS final_discount_percent
            FROM products p
            INNER JOIN best_sellers bs ON p.id = bs.product_id
            WHERE p.is_active = 1
            GROUP BY p.id
            ORDER BY bs.sold_quantity DESC
            LIMIT :limit
        """;
            return handle.createQuery(sql)
                    .bind("limit", limit)
                    .map((rs, ctx) -> {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setProduct_name(rs.getString("product_name"));
                        p.setDescription(rs.getString("description"));

                        p.setPrice(rs.getDouble("price"));
                        p.setImage_url(rs.getString("image_url"));

                        return p;
                    })
                    .list();
        });
    }

}
