
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sidebar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css"/>

</head>
<body>
<!-- === SIDEBAR === -->
<div class="sidebar" id="sidebar">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Noble Loft Theory</a>
    </div>

    <ul>
        <li>
            <a href="${pageContext.request.contextPath}/login.jsp"><i class="fas fa-chart-line"></i> Dashboard</a>
        </li>
        <li>
            <a href="#"><i class="fas fa-box"></i> Sản phẩm</a>
        </li>
        <li>
            <a href="#"><i class="fas fa-cart-shopping"></i> Đơn hàng</a>
        </li>
        <li>
            <a href="#"><i class="fas fa-users"></i> Khách hàng</a>
        </li>
        <li>
            <a href="#"><i class="fas fa-bell"></i> Thông báo</a>
        </li>
        <li>
            <a href="#"><i class="fas fa-gear"></i> Tài khoản</a>
        </li>
    </ul>
</div>
</body>
</html>
