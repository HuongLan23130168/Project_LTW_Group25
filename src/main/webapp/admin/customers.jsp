<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Noble Loft Theory - Customers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/customers.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="sidebar" id="sidebar">
        <div class="logo">
            <a href="dashboard.jsp">Noble Loft Theory</a>
        </div>
        <ul>
            <li>
                <a href="dashboard.jsp"><i class="fas fa-chart-line"></i> Dashboard</a>
            </li>
            <li>
                <a href="products.jsp"><i class="fas fa-box"></i> Sản phẩm</a>
            </li>
            <li>
                <a href="orders.jsp"><i class="fas fa-cart-shopping"></i> Đơn hàng</a>
            </li>
            <li class="active">
                <a href="${pageContext.request.contextPath}/customers"><i class="fas fa-users"></i> Khách hàng</a>
            </li>
            <li>
                <a href="notifi.jsp"><i class="fas fa-bell"></i> Thông báo</a>
            </li>
            <li>
                <a href="account.jsp"><i class="fas fa-gear"></i> Tài khoản</a>
            </li>
        </ul>
    </div>
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
        <div class="customers-header">
            <h1>Danh sách khách hàng</h1>
            <div class="filter-container">
                <form method="get" action="${pageContext.request.contextPath}/customers">
                    <select name="sortBy" onchange="this.form.submit()">
                        <option value="newest" ${param.sortBy == 'newest' || empty param.sortBy ? 'selected' : ''}>Tài khoản mới nhất</option>
                        <option value="oldest" ${param.sortBy == 'oldest' ? 'selected' : ''}>Tài khoản cũ nhất</option>
                    </select>
                </form>
            </div>
        </div>

        <div class="table-container">
            <table class="customers-table">
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Tên</th>
                        <th>Email</th>
                        <th>SĐT</th>
                        <th>Lịch sử</th>
                        <th>Ngày tạo</th>
                        <th>Chi tiết</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${customers}" var="c" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td>${c.full_name}</td>
                            <td>${c.email}</td>
                            <td>${c.phone}</td>
                            <td>${c.total_orders} đơn</td>
                            <td>${c.created_at}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/customer-detail?id=${c.id}" class="view-btn">Xem</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/admin/js/main.js"></script>
</body>
</html>