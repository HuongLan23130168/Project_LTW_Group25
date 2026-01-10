<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Noble Loft Theory - Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/orders.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<!-- === SIDEBAR === -->
<%@ include file="common/admin_sidebar.jspf" %>
<%@ include file="common/admin_header.jspf" %>

<!-- === MAIN CONTENT === -->
<main class="main-content">
    <div class="customers-header">
        <h1>Danh sách đơn hàng</h1>
        <div class="filter-container">
            <form method="get" action="${pageContext.request.contextPath}/admin/orders">
                <select name="sortBy" onchange="this.form.submit()">
                    <option value="newest" ${param.sortBy == 'newest' || empty param.sortBy ? 'selected' : ''}>Mới nhất</option>
                    <option value="oldest" ${param.sortBy == 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                </select>
            </form>
        </div>
    </div>

    <div class="table-container">
        <table class="customers-table">
            <thead>
                <tr>
                    <th>Mã đơn</th>
                    <th>Khách hàng</th>
                    <th>Ngày đặt</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>#${order.order_code}</td>
                        <td>${order.recipient_name}</td>
                        <td><fmt:formatDate value="${order.order_date}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td><fmt:formatNumber value="${order.total_price}" type="number"/>₫</td>
                        <td>
                            <c:set var="lowerStatus" value="${fn:toLowerCase(order.status)}" />
                            <c:choose>
                                <c:when test="${fn:contains(lowerStatus, 'hoàn thành') or fn:contains(lowerStatus, 'đã giao')}">
                                    <span class="status status-completed">${order.status}</span>
                                </c:when>
                                <c:when test="${fn:contains(lowerStatus, 'hủy')}">
                                    <span class="status status-cancelled">${order.status}</span>
                                </c:when>
                                <c:when test="${fn:contains(lowerStatus, 'xử lý') or fn:contains(lowerStatus, 'chờ')}">
                                    <span class="status status-pending">${order.status}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status">${order.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/viewOrder?orderId=${order.id}" class="view-btn">Xem</a>
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
