package com.example.demoweb1.dao;

import com.example.demoweb1.model.CustomerDetail;
import com.example.demoweb1.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDetailDAO {

    public List<CustomerDetail> getCustomerDetailsById(int customerId) {
        List<CustomerDetail> detailsList = new ArrayList<>();
        String sql = """
            SELECT
                u.id,
                u.full_name,
                u.email,
                u.phone,
                u.created_at,
                u.gender,
                u.birth,
                u.role,
                u.address,
                o.id AS order_id,
                o.order_date,
                o.total_amount AS total_price,
                o.status
            FROM
                users u
            LEFT JOIN
                orders o ON u.id = o.user_id
            WHERE
                u.id = ? AND u.role = 'user'
            ORDER BY o.order_date DESC
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CustomerDetail detail = new CustomerDetail();
                detail.setId(rs.getString("id"));
                detail.setFull_name(rs.getString("full_name"));
                detail.setEmail(rs.getString("email"));
                detail.setPhone(rs.getString("phone"));
                detail.setCreated_at(rs.getString("created_at"));
                detail.setGender(rs.getString("gender"));
                detail.setBirth(rs.getString("birth"));
                detail.setRole(rs.getString("role"));
                detail.setAddress(rs.getString("address"));

                // Chỉ set thông tin đơn hàng nếu có
                if (rs.getObject("order_id") != null) {
                    detail.setOrder_id(rs.getInt("order_id"));
                    detail.setOrder_date(rs.getString("order_date"));
                    detail.setTotal_price(rs.getDouble("total_price"));
                    detail.setStatus(rs.getString("status"));
                }
                
                detailsList.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailsList;
    }
}
