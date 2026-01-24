package com.example.project_ltw_25.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import com.example.project_ltw_25.model.Product;
import com.example.project_ltw_25.model.Product_image;
import com.example.project_ltw_25.model.Product_variant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductDao {
    private static final Jdbi jdbi = DBDAO.get();

    // Row Mappers để đọc dữ liệu
    private static class ProductMapper implements RowMapper<Product> {
        @Override
        public Product map(ResultSet rs, StatementContext ctx) throws SQLException {
            Product p = new Product();
            p.setId(rs.getInt("id"));
            p.setProduct_code(rs.getString("product_code"));
            p.setProduct_name(rs.getString("product_name"));
            p.setProduct_type_id(rs.getInt("product_type_id"));
            p.setCategory_id(rs.getInt("category_id"));
            p.setDescription(rs.getString("description"));
            return p;
        }
    }

    private static class ProductVariantMapper implements RowMapper<Product_variant> {
        @Override
        public Product_variant map(ResultSet rs, StatementContext ctx) throws SQLException {
            Product_variant pv = new Product_variant();
            pv.setId(rs.getInt("id"));
            pv.setProduct_id(rs.getInt("product_id"));
            pv.setStyle(rs.getString("style"));
            pv.setColor(rs.getString("color"));
            pv.setSize(rs.getString("size"));
            pv.setMaterial(rs.getString("material"));
            pv.setPrice(rs.getDouble("price"));
            pv.setImage_url(rs.getString("image_url"));
            return pv;
        }
    }

    private static class ProductImageMapper implements RowMapper<Product_image> {
        @Override
        public Product_image map(ResultSet rs, StatementContext ctx) throws SQLException {
            Product_image pi = new Product_image();
            pi.setId(rs.getInt("id"));
            pi.setProduct_id(rs.getInt("product_id"));
            pi.setImage_url(rs.getString("image_url"));
            return pi;
        }
    }

    public boolean addProduct(Product product, Product_variant variant, List<Product_image> images) {
        try {
            return jdbi.inTransaction(handle -> {
                String productQuery = "INSERT INTO products (product_code, product_name, product_type_id, category_id, description) VALUES (:product_code, :product_name, :product_type_id, :category_id, :description)";
                int productId = handle.createUpdate(productQuery)
                        .bind("product_code", product.getProduct_code())
                        .bind("product_name", product.getProduct_name())
                        .bind("product_type_id", product.getProduct_type_id())
                        .bind("category_id", product.getCategory_id())
                        .bind("description", product.getDescription())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one();

                String variantQuery = "INSERT INTO product_variants (product_id, style, color, size, material, price, image_url) VALUES (:product_id, :style, :color, :size, :material, :price, :image_url)";
                handle.createUpdate(variantQuery)
                        .bind("product_id", productId)
                        .bind("style", variant.getStyle())
                        .bind("color", variant.getColor())
                        .bind("size", variant.getSize())
                        .bind("material", variant.getMaterial())
                        .bind("price", variant.getPrice())
                        .bind("image_url", variant.getImage_url())
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

    public Product getById(int id) {
        return jdbi.withHandle(handle -> {
            Optional<Product> productOptional = handle.createQuery("SELECT * FROM products WHERE id = :id")
                    .bind("id", id)
                    .map(new ProductMapper())
                    .findFirst();

            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                List<Product_variant> variants = handle.createQuery("SELECT * FROM product_variants WHERE product_id = :id")
                        .bind("id", id)
                        .map(new ProductVariantMapper())
                        .list();
                product.setVariants(variants);

                List<Product_image> images = handle.createQuery("SELECT * FROM product_images WHERE product_id = :id")
                        .bind("id", id)
                        .map(new ProductImageMapper())
                        .list();
                product.setImages(images);
                return product;
            }
            return null;
        });
    }

    public List<Product> getAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM products").map(new ProductMapper()).list());
    }

    public List<Product> getByCategory(int categoryId) {
        return jdbi.withHandle(handle -> {
            List<Product> products = handle.createQuery("SELECT * FROM products WHERE category_id = :categoryId")
                    .bind("categoryId", categoryId)
                    .map(new ProductMapper())
                    .list();

            for (Product product : products) {
                List<Product_variant> variants = handle.createQuery("SELECT * FROM product_variants WHERE product_id = :id LIMIT 1")
                        .bind("id", product.getId())
                        .map(new ProductVariantMapper())
                        .list();
                product.setVariants(variants);
            }
            return products;
        });
    }
}
