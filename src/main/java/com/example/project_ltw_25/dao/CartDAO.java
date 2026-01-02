package com.example.project_ltw_25.dao;

import com.example.project_ltw_25.model.CartItem;
import org.jdbi.v3.core.Jdbi;

import java.sql.*;
import java.util.*;

public class CartDAO {
    private static Jdbi jdbi = DBDAO.get(); // Lấy Jdbi instance từ DBDAO

    // 1. Lấy hoặc tạo giỏ hàng
    public int getOrCreateCart(int userId) {
        return jdbi.withHandle(handle -> {
            Optional<Integer> cartId = handle.createQuery("SELECT id FROM carts WHERE user_id = :userId")
                    .bind("userId", userId)
                    .mapTo(Integer.class)
                    .findFirst();

            if (cartId.isPresent()) return cartId.get();

            return handle.createUpdate("INSERT INTO carts (user_id, created_at) VALUES (:userId, NOW())")
                    .bind("userId", userId)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Integer.class)
                    .one();
        });
    }

    // 2. Thêm hoặc cập nhật (ON DUPLICATE KEY UPDATE)
    public void addItemToCart(int cartId, int variantId, int quantity) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO cart_details (cart_id, variant_id, quantity) VALUES (:cartId, :variantId, :quantity) " +
                            "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)")
                    .bind("cartId", cartId)
                    .bind("variantId", variantId)
                    .bind("quantity", quantity)
                    .execute();
        });
    }

    // Lấy danh sách sản phẩm trong giỏ (Cực kỳ quan trọng để hiển thị)
    public static Map<Integer, CartItem> getCartDetails(int userId) {
        return jdbi.withHandle(handle -> {
            String sql = "SELECT pv.id AS variant_id, pv.color, pv.size, pv.price, pv.image_url, cd.quantity, p.product_name " +
                    "FROM cart_details cd " +
                    "JOIN product_variants pv ON cd.variant_id = pv.id " +
                    "JOIN products p ON pv.product_id = p.id " +
                    "JOIN carts c ON cd.cart_id = c.id " +
                    "WHERE c.user_id = :userId";

            List<CartItem> items = handle.createQuery(sql)
                    .bind("userId", userId)
                    .map((rs, ctx) -> {
                        CartItem item = new CartItem();
                        item.setVariantId(rs.getInt("variant_id"));
                        item.setName(rs.getString("product_name"));
                        item.setPrice(rs.getDouble("price"));
                        item.setQuantity(rs.getInt("quantity"));
                        item.setImage(rs.getString("image_url"));
                        item.setColor(rs.getString("color"));
                        item.setSize(rs.getString("size"));
                        return item;
                    }).list();

            Map<Integer, CartItem> cartMap = new LinkedHashMap<>();
            for (CartItem item : items) {
                cartMap.put(item.getVariantId(), item);
            }
            return cartMap;
        });
    }

    public static void updateQuantity(int userId, int variantId, int newQty) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE cart_details SET quantity = :qty WHERE variant_id = :vId AND cart_id = (SELECT id FROM carts WHERE user_id = :uId)")
                    .bind("qty", newQty)
                    .bind("vId", variantId)
                    .bind("uId", userId)
                    .execute();
        });
    }

    public static void removeItem(int userId, int variantId) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM cart_details WHERE variant_id = :vId AND cart_id = (SELECT id FROM carts WHERE user_id = :uId)")
                    .bind("vId", variantId)
                    .bind("uId", userId)
                    .execute();
        });
    }


}
