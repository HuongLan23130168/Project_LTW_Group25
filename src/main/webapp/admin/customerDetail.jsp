<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Chi tiết khách hàng - ${customer.fullName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/customerDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>
    <!-- Sidebar -->
    <div class="sidebar" id="sidebar">
        <!-- Sidebar content -->
    </div>

    <!-- Header -->
    <header class="header">
        <!-- Header content -->
    </header>

    <main class="main-content">
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/customers">Khách hàng</a> /
            <span class="current">${customer.fullName}</span>
        </div>

        <div class="customer-detail-container">
            <!-- Left Card for Personal Info -->
            <div class="detail-card-left">
                <div class="title-cus">
                    <h2>Thông tin cá nhân</h2>
                </div>
                <div class="customer-info">
                    <p><strong>Họ và tên:</strong> ${customer.fullName}</p>
                    <p><strong>Email:</strong> ${customer.email}</p>
                    <p><strong>Số điện thoại:</strong> ${customer.phone}</p>
                    <p><strong>Giới tính:</strong> ${not empty customer.gender ? customer.gender : 'Chưa cập nhật'}</p>
                    <p><strong>Ngày sinh:</strong> ${not empty customer.birth ? customer.birth : 'Chưa cập nhật'}</p>
                    <p><strong>Địa chỉ:</strong> ${not empty customer.address ? customer.address : 'Chưa cập nhật'}</p>
                </div>
            </div>

            <!-- Right Card for Stats -->
            <div class="detail-card-right">
                <div class="title-cus">
                    <h2>Thống kê</h2>
                </div>
                <div class="customer-stats">
                    <div class="stat-card">
                        <h3>Tổng đơn hàng</h3>
                        <p>${orderList.size()}</p>
                    </div>
                    <div class="stat-card">
                        <h3>Tổng tiền đã mua</h3>
                        <p><fmt:formatNumber value="${totalSpent}" type="currency" currencySymbol="₫" /></p>
                    </div>
                    <div class="stat-card">
                        <h3>Đơn hàng gần nhất</h3>
                        <c:if test="${not empty latestOrder}">
                            <p>#${latestOrder.id} - <fmt:formatDate value="${latestOrder.orderDate}" pattern="dd/MM/yyyy" /></p>
                        </c:if>
                        <c:if test="${empty latestOrder}">
                            <p>Chưa có</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Order History Section -->
        <div class="order-history-card">
            <div class="title-cus">
                <h2>Lịch sử mua hàng</h2>
            </div>
            <div class="recent-orders">
                <table>
                    <thead>
                        <tr>
                            <th>Mã đơn</th>
                            <th>Ngày đặt</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${empty orderList}">
                            <tr>
                                <td colspan="4" style="text-align: center; padding: 20px;">Chưa có đơn hàng nào.</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${orderList}" var="order">
                            <tr>
                                <td>#${order.id}</td>
                                <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy" /></td>
                                <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫" /></td>
                                <td>
                                    <span class="status ${order.status.toLowerCase()}">${order.status}</span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="back-to-customers">
            <a href="${pageContext.request.contextPath}/customers">Quay lại</a>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/admin/js/main.js"></script>
</body>
</html>