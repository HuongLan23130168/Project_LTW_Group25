package com.example.project_ltw_25.user.services;

import com.example.project_ltw_25.user.dao.ListProductDao;
import com.example.project_ltw_25.user.dao.ProductDAO;
import com.example.project_ltw_25.user.model.Product;

import java.util.List;

public class ProductService {

    private final ListProductDao listProductDao = new ListProductDao();
    private final ProductDAO productDAO = new ProductDAO(); // Thêm ProductDAO

    // Quy định số sản phẩm trên 1 trang (Ví dụ: 9 hoặc 12 sản phẩm)
    private static final int PAGE_SIZE = 9;

    // =========================================================
    // 1. LẤY TẤT CẢ (Dùng cho Trang chủ / Không phân trang)
    // =========================================================
    public List<Product> getAllProducts(int page, int size) {
        // Sửa: Gọi filterProducts với tham số mặc định
        return listProductDao.filterProducts(null, null, null, null, page, size);
    }

    // =========================================================
    // 2. LỌC VÀ PHÂN TRANG (Dùng cho trang Danh sách sản phẩm)
    // =========================================================
    public List<Product> filterProducts(String priceRange, String[] roomIds, String[] typeIds, String sort, int page) {
        // Gọi DAO để lấy danh sách sản phẩm theo trang
        return listProductDao.filterProducts(priceRange, roomIds, typeIds, sort, page, PAGE_SIZE);
    }

    // =========================================================
    // 3. TÍNH TỔNG SỐ TRANG (Để hiển thị phân trang 1, 2, 3...)
    // =========================================================
    public int getTotalPages(String priceRange, String[] roomIds, String[] typeIds) {
        int totalProducts = listProductDao.countProducts(priceRange, roomIds, typeIds);

        // Công thức tính tổng số trang: (Total + Size - 1) / Size
        return (int) Math.ceil((double) totalProducts / PAGE_SIZE);
    }

    // =========================================================
    // 4. CHI TIẾT SẢN PHẨM
    // =========================================================
    public Product getProduct(int id) {
        // Sửa: Sử dụng ProductDAO để lấy chi tiết sản phẩm
        return productDAO.getById(id);
    }

    // =========================================================
    // 5. SẢN PHẨM LIÊN QUAN
    // =========================================================
    public List<Product> getRelatedProducts(String categoryId, int currentId) {
        return productDAO.getRelatedProducts(categoryId, currentId);
    }
}
