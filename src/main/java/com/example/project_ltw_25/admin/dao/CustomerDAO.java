package com.example.project_ltw_25.admin.dao;

import com.example.project_ltw_25.user.dao.DBDAO;
import com.example.project_ltw_25.user.model.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import java.util.List;

public class CustomerDAO {
    // Kết nối CSDL thông qua DBDAO
    private final Jdbi jdbi = DBDAO.get();

    // 1. Lấy danh sách khách hàng và sắp xếp
    public List<User> getAllCustomers(String sortBy, String search) {
        return jdbi.withHandle(handle -> {
            StringBuilder sql = new StringBuilder("SELECT id, full_name AS fullName, birth, gender, email, phone, address, role, created_at FROM users");

            if (search != null && !search.trim().isEmpty()) {
                sql.append(" WHERE full_name LIKE :search OR email LIKE :search");
            }

            String orderBy;
            switch (sortBy) {
                case "oldest":
                    orderBy = " ORDER BY created_at ASC";
                    break;
                case "name_asc":
                    orderBy = " ORDER BY full_name ASC";
                    break;
                case "name_desc":
                    orderBy = " ORDER BY full_name DESC";
                    break;
                case "newest":
                default:
                    orderBy = " ORDER BY created_at DESC";
                    break;
            }
            sql.append(orderBy);

            Query query = handle.createQuery(sql.toString());

            if (search != null && !search.trim().isEmpty()) {
                query.bind("search", "%" + search.trim() + "%");
            }

            return query.mapToBean(User.class).list();
        });
    }

    // 2. Lấy thông tin chi tiết một khách hàng theo ID (PHƯƠNG THỨC BẠN ĐANG THIẾU)
    public User getCustomerById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, full_name AS fullName, birth, gender, email, phone, address, role, created_at FROM users WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    // 3. Xóa khách hàng (Optional - Thêm để dùng nếu cần)
    public void deleteCustomer(int id) {
        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM users WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }
}