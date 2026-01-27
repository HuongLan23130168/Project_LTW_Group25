<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Thông báo</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/notifi.css">
</head>
<body>
<jsp:include page="sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="header.jsp"/>

    <div class="content" style="padding-top: 20px;">
        <h2 class="page-title"><i class="fa-regular fa-bell"></i> Thông báo</h2>

        <div class="notifi-controls">
            <select class="filter-select" onchange="location.href='${pageContext.request.contextPath}/admin/notifications?filter=' + this.value">
                <option value="all"     ${param.filter == 'all' ? 'selected' : ''}>Tất cả</option>
                <option value="unread"  ${param.filter == 'unread' ? 'selected' : ''}>Chưa xem</option>
                <option value="read"    ${param.filter == 'read' ? 'selected' : ''}>Đã xem</option>
                <option value="order"   ${param.filter == 'order' ? 'selected' : ''}>Đơn hàng</option>
                <option value="product" ${param.filter == 'product' ? 'selected' : ''}>Sản phẩm</option>
                <option value="account" ${param.filter == 'account' ? 'selected' : ''}>Tài khoản</option>
            </select>

            <a href="${pageContext.request.contextPath}/admin/notifications?action=markAllRead" class="btn">
                <i class="fa-solid fa-check-double"></i> Đã đọc tất cả
            </a>

            <a href="${pageContext.request.contextPath}/admin/notifications?action=deleteAll" class="btn danger"
               onclick="return confirm('Bạn có chắc chắn muốn xóa toàn bộ thông báo?');">
                <i class="fa-solid fa-trash-can"></i> Xóa tất cả
            </a>
        </div>

        <ul class="notifi-list">
            <c:forEach var="n" items="${notifications}">
                <%-- 1. Xử lý Logic Redirect URL --%>
                <c:set var="redirectUrl" value=""/>
                <c:if test="${n.type == 'order'}">
                    <%-- Đổi từ /view-order thành /viewOrder nếu đó là tên thực tế của servlet --%>
                    <c:set var="redirectUrl" value="/admin/viewOrder?orderId=${n.entityId}"/>
                </c:if>

                <c:if test="${n.type == 'product' || n.type == 'inventory'}">
                    <%-- Kiểm tra xem tham số là 'id' hay 'productId' --%>
                    <c:set var="redirectUrl" value="/admin/editProduct?id=${n.entityId}"/>
                </c:if>
                <%--                <c:if test="${n.type == 'account'}">--%>
                <%--                    <c:set var="redirectUrl" value="${pageContext.request.contextPath}/admin/customers"/>--%>
                <%--                </c:if>--%>

                <c:if test="${n.type == 'system'}">
                    <c:set var="redirectUrl" value="#"/>
                </c:if>

                <%-- 2. Xử lý Logic CSS Class --%>
                <c:set var="statusClass" value="${n.status == 'unread' ? 'unread' : 'read'}"/>
                <c:set var="typeClass" value="${(n.type == 'system') ? 'system' : 'user'}"/>

                <%-- ITEM START --%>
                <li class="notifi-item ${statusClass} ${typeClass}">

                        <%-- Tạo URL an toàn cho việc đánh dấu đã đọc --%>
                    <c:url var="markReadUrl" value="/admin/mark-notification-read">
                        <c:param name="id" value="${n.id}"/>
                        <c:param name="redirectUrl" value="${redirectUrl}"/>
                    </c:url>

                    <a href="${markReadUrl}" class="notifi-link-wrapper">

                        <div class="notifi-icon">
                            <c:choose>
                                <c:when test="${n.type == 'order'}"><i class="fa-solid fa-cart-shopping"></i></c:when>
                                <c:when test="${n.type == 'account'}"><i class="fa-solid fa-user-group"></i></c:when>
                                <c:when test="${n.type == 'product'}"><i class="fa-solid fa-box-open"></i></c:when>
                                <c:when test="${n.type == 'system'}"><i
                                        class="fa-solid fa-triangle-exclamation"></i></c:when>
                                <c:otherwise><i class="fa-regular fa-envelope"></i></c:otherwise>
                            </c:choose>
                        </div>

                        <div class="notifi-text">
                            <h4>
                                    ${n.title}
                                <c:if test="${n.status == 'unread'}">
                                    <span class="notify-badge-inline">Mới</span>
                                </c:if>
                            </h4>
                            <p>${n.content}</p>
                            <div class="notifi-time">
                                <i class="fa-regular fa-clock"></i>
                                <fmt:formatDate value="${n.createdAt}" pattern="HH:mm - dd/MM/yyyy"/>
                                &nbsp;|&nbsp;
                                <span>
                                    <c:choose>
                                        <c:when test="${n.type == 'order'}">Đơn hàng</c:when>
                                        <c:when test="${n.type == 'product'}">Sản phẩm</c:when>
                                        <c:when test="${n.type == 'account'}">Tài khoản</c:when>
                                        <c:when test="${n.type == 'system'}">Hệ thống</c:when>
                                        <c:otherwise>Khác</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                    </a>

                        <%-- Vùng Action: Nút Thùng Rác --%>
                    <div class="notifi-actions">
                        <a href="${pageContext.request.contextPath}/admin/delete-notification?id=${n.id}"
                           class="action-btn delete"
                           title="Xóa thông báo này"
                           onclick="return confirm('Bạn muốn xóa thông báo này?');">
                            <i class="fa-solid fa-trash-can"></i>
                        </a>
                    </div>
                </li>
                <%-- ITEM END --%>
            </c:forEach>

            <%-- Trạng thái trống --%>
            <c:if test="${empty notifications}">
                <li class="notifi-item"
                    style="justify-content: center; opacity: 1; border: 2px dashed #ccc; background: none; box-shadow: none;">
                    <div style="text-align: center; color: #999; padding: 30px;">
                        <i class="fa-solid fa-inbox" style="font-size: 40px; margin-bottom: 15px; color: #dcdcdc;"></i>
                        <p style="font-size: 15px;">Không tìm thấy thông báo nào.</p>
                    </div>
                </li>
            </c:if>
        </ul>
    </div>
</div>
</body>
</html>