package com.example.project_ltw_25.dao;

import com.example.project_ltw_25.model.Banner;
import com.example.project_ltw_25.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BannerDAO {

    public List<Banner> getAllBanners() {
        List<Banner> bannerList = new ArrayList<>();
        String sql = "SELECT * FROM banners ORDER BY display_order ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Banner banner = new Banner();
                banner.setId(rs.getInt("id"));
                banner.setTitle(rs.getString("title"));
                banner.setDescription(rs.getString("description"));
                banner.setImage_url(rs.getString("image_url"));
                banner.setLink(rs.getString("link"));
                banner.setDisplay_order(rs.getInt("display_order"));
                banner.setIs_active(rs.getBoolean("is_active"));
                banner.setCreated_at(rs.getTimestamp("created_at"));
                bannerList.add(banner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bannerList;
    }

    public Banner getBannerById(int id) {
        String sql = "SELECT * FROM banners WHERE id = ?";
        Banner banner = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                banner = new Banner();
                banner.setId(rs.getInt("id"));
                banner.setTitle(rs.getString("title"));
                banner.setDescription(rs.getString("description"));
                banner.setImage_url(rs.getString("image_url"));
                banner.setLink(rs.getString("link"));
                banner.setDisplay_order(rs.getInt("display_order"));
                banner.setIs_active(rs.getBoolean("is_active"));
                banner.setCreated_at(rs.getTimestamp("created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return banner;
    }

    public void addBanner(Banner banner) {
        String updateSql = "UPDATE banners SET display_order = display_order + 1 WHERE display_order >= ?";
        String insertSql = "INSERT INTO banners (title, description, image_url, link, display_order, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, banner.getDisplay_order());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, banner.getTitle());
                ps.setString(2, banner.getDescription());
                ps.setString(3, banner.getImage_url());
                ps.setString(4, banner.getLink());
                ps.setInt(5, banner.getDisplay_order());
                ps.setBoolean(6, banner.isIs_active());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void updateBanner(Banner banner) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int oldOrder = -1;
            String getOldOrderSql = "SELECT display_order FROM banners WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(getOldOrderSql)) {
                ps.setInt(1, banner.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    oldOrder = rs.getInt("display_order");
                }
            }

            if (oldOrder != -1 && oldOrder != banner.getDisplay_order()) {
                int newOrder = banner.getDisplay_order();
                if (newOrder < oldOrder) {
                    String sql = "UPDATE banners SET display_order = display_order + 1 WHERE display_order >= ? AND display_order < ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, newOrder);
                        ps.setInt(2, oldOrder);
                        ps.executeUpdate();
                    }
                } else {
                    String sql = "UPDATE banners SET display_order = display_order - 1 WHERE display_order > ? AND display_order <= ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, oldOrder);
                        ps.setInt(2, newOrder);
                        ps.executeUpdate();
                    }
                }
            }

            String updateSql = "UPDATE banners SET title = ?, description = ?, image_url = ?, link = ?, display_order = ?, is_active = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, banner.getTitle());
                ps.setString(2, banner.getDescription());
                ps.setString(3, banner.getImage_url());
                ps.setString(4, banner.getLink());
                ps.setInt(5, banner.getDisplay_order());
                ps.setBoolean(6, banner.isIs_active());
                ps.setInt(7, banner.getId());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void deleteBanner(int id) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int orderToDelete = -1;
            String getOrderSql = "SELECT display_order FROM banners WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(getOrderSql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    orderToDelete = rs.getInt("display_order");
                }
            }

            String deleteSql = "DELETE FROM banners WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // 3. Cập nhật lại thứ tự của các banner phía sau
            if (orderToDelete != -1) {
                String updateSql = "UPDATE banners SET display_order = display_order - 1 WHERE display_order > ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, orderToDelete);
                    ps.executeUpdate();
                }
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
