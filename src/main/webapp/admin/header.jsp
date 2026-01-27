<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css"/>

</head>
<body>

<!-- HEADER -->
<header class="header">
    <div class="header-left"></div>

    <div class="header-right">

        <div class="profile-dropdown">
            <button class="icon-button user-btn">
                <i class="fa-solid fa-user"></i>
            </button>
            <div class="dropdown-menu">
                <c:choose>
                    <c:when test="${not empty sessionScope.acc}">
                        <a href="${pageContext.request.contextPath}/admin/account">
                            <i class="fas fa-user-shield"></i> ${sessionScope.acc.fullName}
                        </a>

                        <c:if test="${sessionScope.acc.role == '2'}">
                            <a href="${pageContext.request.contextPath}/admin/admin-dashboard">
                                <i class="fas fa-chart-line"></i> Thống kê
                            </a>
                        </c:if>

                        <a href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-right-from-bracket"></i> Đăng xuất
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login">
                            <i class="fas fa-sign-in-alt"></i> Đăng nhập
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>

</body>
</html>
