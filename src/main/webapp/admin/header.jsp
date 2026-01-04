<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css"/>

</head>
<body>

<!-- === HEADER === -->
<header class="header">
    <div class="header-left">
        <div class="search-container">
            <i class="fa-solid fa-magnifying-glass" style="color: #74512d;"></i>
            <input type="text" placeholder="Tìm kiếm" class="search-input"/>
        </div>
    </div>

    <div class="header-right">
        <div class="notify-wrapper">
            <a href="notifi.jsp" class="icon-button">
                <i class="fa-solid fa-bell"></i>
                <c:if test="${not empty sessionScope.acc}">
                    <span id="notifyCount" class="notify-badge">3</span>
                </c:if>
            </a>
        </div>

        <div class="profile-dropdown">
            <button class="icon-button user-btn">
                <i class="fa-solid fa-user"></i>
            </button>
            <div class="dropdown-menu">
                <c:choose>
                    <c:when test="${not empty sessionScope.acc}">
                        <%-- Hiển thị tên và Role --%>
                        <a href="#"><i class="fas fa-user-shield"></i> Admin: ${sessionScope.acc.fullName}</a>

                        <%-- Nếu đang ở trang user mà là admin, có thể hiện link quay lại Dashboard --%>
                        <c:if test="${sessionScope.acc.role == '2'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard.jsp"><i class="fas fa-chart-line"></i> Bảng điều khiển</a>
                        </c:if>

                        <a href="account.jsp"><i class="fas fa-gear"></i> Cài đặt</a>
                        <a href="${pageContext.request.contextPath}/logout"><i class="fas fa-right-from-bracket"></i> Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login"><i class="fas fa-sign-in-alt"></i> Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>

</body>
</html>
