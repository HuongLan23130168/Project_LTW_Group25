<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Noble Loft Theory - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

</head>

<body>
<jsp:include page="/admin/header.jsp"/>
<jsp:include page="/admin/sidebar.jsp"/>


<!-- === DASHBOARD === -->
<div class="main-content">
    <div class="cards">
        <div class="card">
            <div class="card-header">
                <i class="fas fa-coins"></i>
                <h4>Doanh thu (tháng)</h4>
            </div>
            <h2><fmt:formatNumber value="${revenue}" type="currency" currencySymbol="₫"/></h2>
            <p>Dữ liệu cập nhật thời gian thực</p>
        </div>

        <div class="card">
            <div class="card-header">
                <i class="fas fa-box-open"></i>
                <h4>Sản phẩm bán chạy</h4>
            </div>
            <h2>24</h2>
            <p>Top: Gương trang trí</p>
        </div>

        <div class="card">
            <div class="card-header">
                <i class="fas fa-warehouse"></i>
                <h4>Tồn kho</h4>
            </div>
            <h2>${totalStock} sp</h2>
            <p style="color: ${lowStock > 0 ? 'red' : 'inherit'}">
                Cần nhập: ${lowStock}
            </p>
        </div>

        <div class="card">
            <div class="card-header">
                <i class="fas fa-shopping-bag"></i>
                <h4>Đơn hàng mới</h4>
            </div>
            <h2>${pendingOrdersCount}</h2>
            <p>Cần xử lý ngay</p>
        </div>
    </div>

    <div class="stats-section">
        <div class="chart-section">
            <h3>Biểu đồ Doanh thu (7 ngày)</h3>
            <div style="height: 300px;">
                <canvas id="revenueChart"></canvas>
            </div>
        </div>

        <div class="best-seller">
            <h3>Sản phẩm bán chạy</h3>
            <ul class="top-products">
                <c:forEach var="p" items="${bestSellers}">
                    <li>
                        <img src="${p.image}" alt="${p.name}">
                        <div>
                            <strong class="prod-title">${p.name}</strong>
                            <p class="muted">
                                <fmt:formatNumber value="${p.price}" type="number"/>₫ • ${p.totalSold} đã bán
                            </p>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>


    <div class="recent-orders">
        <div class="recent-orders-header">
            <h3>Đơn hàng gần đây</h3>
            <div class="search-orders">
                <input type="text" placeholder="Tìm mã / Khách hàng..." class="search-input-orders">
                <i class="fas fa-search search-icon-orders"></i>
            </div>
        </div>

        <div class="recent-orders">
            <table>
                <thead>
                <tr>
                    <th>Mã đơn</th>
                    <th>Khách hàng</th>
                    <th>Sản phẩm</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="o" items="${recentOrders}">
                    <tr>
                        <td><a href="order-detail?id=${o.id}">#${o.orderCode}</a></td>
                        <td>${o.recipientName}</td>
                        <td>${o.orderCode}</td>
                        <td><fmt:formatNumber value="${o.totalPrice}" type="number"/>₫</td>
                        <td>
                            <c:choose>
                                <c:when test="${o.status == 'Delivered'}">
                                    <span class="status delivered">Đã giao</span>
                                </c:when>
                                <c:when test="${o.status == 'Cancelled'}">
                                    <span class="status cancelled">Đã hủy</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status processing">Đang xử lý</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // Truyền dữ liệu từ Server sang Client
    const chartLabels = ${jsonLabels};
    const chartData = ${jsonValues};
</script>
<script src="${pageContext.request.contextPath}/admin/js/main.js"></script>
<script src="${pageContext.request.contextPath}/admin/js/dashboard.js"></script>
</body>

</html>