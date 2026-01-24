<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Tra cứu đơn hàng</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;800&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/footer.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/tracking.css">
</head>

<body>

<jsp:include page="/frontend/header.jsp"/>

<main class="tracking-main-content">
    <div class="breadcrumb">
        <a href="home.jsp">Trang chủ</a> &#47;
        <span class="current">Tra cứu đơn hàng </span>
    </div>

    <div class="search-section">
        <h2 class="page-title">TRA CỨU ĐƠN HÀNG</h2>
        <form action="${pageContext.request.contextPath}/tracking" method="get" class="order-tracking-search-box">
            <input type="text" name="orderCode"
                   placeholder="Nhập mã đơn hàng (VD: NLT@...)"
                   required
                   value="${param.orderCode}">
            <button type="submit">
                <i class="fa fa-search"></i> TRA CỨU
            </button>
        </form>
    </div>

    <c:if test="${not empty error}">
        <div class="tracking-result-container error-message">
            <p>${error}</p>
        </div>
    </c:if>

    <c:if test="${not empty order}">
        <div class="tracking-result-container">

            <div class="order-left">
                <h3 class="block-title">THÔNG TIN ĐƠN HÀNG</h3>

                <p class="section-title">Danh sách sản phẩm</p>
                <div class="card-list">
                    <c:forEach items="${orderItems}" var="d">
                        <div class="cart-item">
                            <c:set var="imgSrc" value="${pageContext.request.contextPath}/uploads/products/${d.imageUrl}" />
                            <c:if test="${empty d.imageUrl}">
                                <c:set var="imgSrc" value="${pageContext.request.contextPath}/uploads/products/${d.variantId}.jpg" />
                            </c:if>

                            <img src="${imgSrc}"
                                 alt="Sản phẩm"
                                 onerror="this.src='https://i.postimg.cc/5t4yq9qJ/logo-ltw.jpg'">

                            <div class="item-info">
                                <h4>${d.name}</h4>
                                <c:if test="${not empty d.color or not empty d.size}">
                                    <p style="font-size: 0.9em; color: #666;">Phân loại: ${d.color} - ${d.size}</p>
                                </c:if>
                                <div class="price">
                                    <div class="current-price">
                                        <fmt:formatNumber value="${d.price}" type="currency" currencySymbol="₫"/>
                                    </div>
                                </div>
                                <p>Số lượng: ${d.quantity}</p>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="order-summary">
                    <table>
                        <tr><td>Mã đơn hàng</td><td><strong>#${order.orderCode}</strong></td></tr>
                        <tr><td>Người nhận</td><td>${order.recipientName}</td></tr>
                        <tr><td>Số điện thoại</td><td>${order.recipientPhone}</td></tr>
                        <tr><td>Địa chỉ giao hàng</td><td>${order.shippingAddress}</td></tr>
                        <tr class="divider"><td colspan="2"></td></tr>
                        <tr>
                            <td>Tiền hàng</td>
                            <td><fmt:formatNumber value="${order.totalPrice - order.shipping.shippingFee}" type="currency" currencySymbol="₫"/></td>
                        </tr>
                        <tr>
                            <td>Phí vận chuyển</td>
                            <td><fmt:formatNumber value="${order.shipping.shippingFee}" type="currency" currencySymbol="₫"/></td>
                        </tr>
                    </table>
                    <div class="total">
                        <span>TỔNG THANH TOÁN</span>
                        <span class="price">
                            <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>
                </div>
            </div>

            <div class="order-right">
                <h3 class="block-title">TÌNH TRẠNG ĐƠN HÀNG</h3>
                <div class="order-summary">
                    <table>
                        <tr>
                            <td>Trạng thái</td>
                            <td>
                                <c:choose>
                                    <%-- 1. Nếu đơn Đã Hủy -> Hiện màu đỏ --%>
                                    <c:when test="${order.status == 'Đã hủy'}">
                                        <span class="status-badge cancel">Đã hủy</span>
                                    </c:when>

                                    <%-- 2. Nếu chưa hủy -> Hiện trạng thái vận chuyển --%>
                                    <c:otherwise>
                                        <c:set var="statusClass" value="wait" />

                                        <c:if test="${order.shipping.shippingStatus.contains('giao') || order.shipping.shippingStatus.contains('Vận chuyển')}">
                                            <c:set var="statusClass" value="shipping" />
                                        </c:if>
                                        <c:if test="${order.shipping.shippingStatus.contains('thành công') || order.shipping.shippingStatus.contains('Đã giao')}">
                                            <c:set var="statusClass" value="success" />
                                        </c:if>

                                        <span class="status-badge ${statusClass}">
                                                ${order.shipping.shippingStatus}
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td>Mã vận chuyển</td>
                            <td>${not empty order.shipping.trackingNumber ? order.shipping.trackingNumber : 'Chưa có'}</td>
                        </tr>
                        <tr>
                            <td>Ghi chú</td>
                            <td>${not empty order.note ? order.note : 'Không có ghi chú'}</td>
                        </tr>
                    </table>
                </div>
            </div>

        </div>
    </c:if>

    <p class="footer-text">
        Quý khách vui lòng kiểm tra lại mã đơn hoặc gọi CSKH:
        <strong>0375 1841 444</strong>
    </p>
</main>

<jsp:include page="/frontend/footer.jsp"/>

</body>
</html>