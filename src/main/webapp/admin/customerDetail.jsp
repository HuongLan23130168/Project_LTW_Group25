<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Chi tiết khách hàng - ${customer.full_name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/customerDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>
<!-- === SIDEBAR === -->
<div class="sidebar" id="sidebar">
    <div class="logo">
        <a href="dashboard.jsp">Noble Loft Theory</a>
    </div>
    <ul>
        <li>
            <a href="dashboard.jsp"><i class="fas fa-chart-line"></i> Dashboard</a>
        </li>
        <li class="active">
            <a href="products.jsp"><i class="fas fa-box"></i> Sản phẩm</a>
        </li>
        <li>
            <a href="orders.jsp"><i class="fas fa-cart-shopping"></i> Đơn hàng</a>
        </li>
        <li>
            <a href="customers.jsp"><i class="fas fa-users"></i> Khách hàng</a>
        </li>
        <li>
            <a href="notifi.jsp"><i class="fas fa-bell"></i> Thông báo</a>
        </li>
        <li>
            <a href="account.jsp"><i class="fas fa-gear"></i> Tài khoản</a>
        </li>
    </ul>
</div>

<!-- === HEADER === -->
<header class="header">
    <div class="header-left">
        <div class="search-container">
            <i class="fa-solid fa-magnifying-glass" style="color: #74512d;"></i>
            <input type="text" placeholder="Tìm kiếm" class="search-input" />
        </div>
    </div>

    <div class="header-right">
        <!-- Nút thông báo -->
        <div class="notify-wrapper">
            <a href="notifi.jsp" class="icon-button">
                <i class="fa-solid fa-bell"></i>
                <span id="notifyCount" class="notify-badge">3</span>
            </a>
        </div>

        <!-- Hồ sơ người dùng -->
        <div class="profile-dropdown">
            <button class="icon-button user-btn">
                <i class="fa-solid fa-user"></i>
            </button>

            <div class="dropdown-menu">
                <a href="account.jsp"><i class="fas fa-user"></i> Tài khoản</a>
                <a href="index.jsp"><i class="fas fa-right-from-bracket"></i> Đăng xuất</a>
            </div>
        </div>
    </div>
</header>

    <main class="main-content">
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/customers">Khách hàng</a> /
            <span class="current">${customer.full_name}</span>
        </div>

        <c:if test="${not empty customer}">
            <div class="customer-detail-container">
                <div class="detail-card-left">
                    <div class="title-cus">
                        <h2>Thông tin cá nhân</h2>
                    </div>
                    <div class="customer-info">
                        <p><strong>Họ và tên:</strong> ${customer.full_name}</p>
                        <p><strong>Email:</strong> ${customer.email}</p>
                        <p><strong>Số điện thoại:</strong> ${customer.phone}</p>
                        <p><strong>Giới tính:</strong> ${not empty customer.gender ? customer.gender : 'Chưa cập nhật'}</p>
                        <p><strong>Ngày sinh:</strong> ${not empty customer.birth ? customer.birth : 'Chưa cập nhật'}</p>
                        <p><strong>Địa chỉ:</strong> ${not empty customer.address ? customer.address : 'Chưa cập nhật'}</p>
                    </div>
                </div>

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
                                <p>#${latestOrder.id} - <fmt:formatDate value="${latestOrder.order_date}" pattern="dd/MM/yyyy" /></p>
                            </c:if>
                            <c:if test="${empty latestOrder}">
                                <p>Chưa có</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

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
                                    <td><fmt:formatDate value="${order.order_date}" pattern="dd/MM/yyyy HH:mm" /></td>
                                    <td>
                                        <fmt:formatNumber value="${order.total_price}" type="currency" currencySymbol="₫" />
                                    </td>
                                    <td>
                                            <%-- 1. Đặt mặc định là màu xám (secondary) cho mỗi vòng lặp --%>
                                        <c:set var="statusClass" value="secondary" />

                                            <%-- 2. Chuyển status về chữ thường để so sánh chính xác --%>
                                        <c:set var="statusLower" value="${fn:toLowerCase(order.status)}" />

                                            <%-- 3. Kiểm tra logic --%>
                                        <c:choose>
                                            <%-- contains: dùng để kiểm tra chứa chuỗi, an toàn hơn so với so sánh bằng tuyệt đối --%>
                                            <c:when test="${fn:contains(statusLower, 'đã giao') || fn:contains(statusLower, 'thành công')}">
                                                <c:set var="statusClass" value="success" />
                                            </c:when>

                                            <c:when test="${fn:contains(statusLower, 'đang xử lý') || fn:contains(statusLower, 'chờ')}">
                                                <c:set var="statusClass" value="warning" />
                                            </c:when>

                                            <c:when test="${fn:contains(statusLower, 'đã hủy') || fn:contains(statusLower, 'thất bại')}">
                                                <c:set var="statusClass" value="danger" />
                                            </c:when>
                                        </c:choose>

                                            <%-- 4. Hiển thị --%>
                                        <span class="status ${statusClass}">
                                                ${order.status}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>
        <c:if test="${empty customer}">
             <p>Không tìm thấy thông tin khách hàng với ID được cung cấp.</p>
        </c:if>

        <div class="back-to-customers">
            <a href="${pageContext.request.contextPath}/customers">Quay lại</a>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/admin/js/main.js"></script>
</body>
</html>