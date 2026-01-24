<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Sidebar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css"/>

</head>
<body>
<!--  SIDEBAR  -->
<c:set var="uri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>

<div class="sidebar" id="sidebar">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/admin/admin-dashboard">Noble Loft Theory</a>
    </div>

    <ul>
        <c:choose>
            <c:when test="${not empty sessionScope.acc && sessionScope.acc.role == '2'}">
                <li>
                    <a href="${pageContext.request.contextPath}/admin/admin-dashboard"
                       class="${uri.contains('admin-dashboard') ? 'active' : ''}">
                        <i class="fas fa-chart-line"></i> Dashboard
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/banners"
                       class="${uri.contains('banners') ? 'active' : ''}">
                        <i class="fas fa-image"></i> Banner
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/products"
                       class="${uri.contains('products') ? 'active' : ''}">
                        <i class="fas fa-box"></i> Sản phẩm
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/orders"
                       class="${uri.contains('orders') ? 'active' : ''}">
                        <i class="fas fa-cart-shopping"></i> Đơn hàng
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/customers"
                       class="${uri.contains('customers') ? 'active' : ''}">
                        <i class="fas fa-users"></i> Khách hàng
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/notifis"
                       class="${uri.contains('notifis') ? 'active' : ''}">
                        <i class="fas fa-bell"></i> Thông báo
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/account"
                       class="${uri.contains('account') ? 'active' : ''}">
                        <i class="fas fa-gear"></i> Tài khoản
                    </a>
                </li>
            </c:when>
            <c:otherwise>
                <li><a href="${pageContext.request.contextPath}/login"><i class="fas fa-sign-in-alt"></i> Đăng nhập</a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
</body>
</html>
