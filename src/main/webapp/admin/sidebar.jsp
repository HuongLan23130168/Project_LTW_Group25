<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Sidebar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css"/>

</head>
<body>
<!-- === SIDEBAR === -->
<div class="sidebar" id="sidebar">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/admin/admin-dashboard">Noble Loft Theory</a>
    </div>

    <ul>
        <c:choose>
            <c:when test="${not empty sessionScope.acc && sessionScope.acc.role == '2'}">
                <li>
                    <a href="${pageContext.request.contextPath}/admin/admin-dashboard"><i class="fas fa-chart-line"></i>
                        Dashboard</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/products"><i class="fas fa-box"></i> Sản phẩm</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/orders"><i class="fas fa-cart-shopping"></i> Đơn
                        hàng</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/customers"><i class="fas fa-users"></i> Khách hàng</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/notifis"><i class="fas fa-bell"></i> Thông báo</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/account"><i class="fas fa-gear"></i> Tài khoản</a>
                </li>
            </c:when>
            <c:otherwise>
                <li><a href="${pageContext.request.contextPath}/login"><i class="fas fa-sign-in-alt"></i> Đăng nhập để
                    tiếp tục</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
</body>
</html>
