<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <%@ include file="common/header.jspf" %>

    <main class="tracking-main-content">
        <!-- Breadcrumb -->
        <div class="breadcrumb">
            <a href="home.jsp">Trang chủ</a> &#47;
            <span class="current">Tra cứu đơn hàng</span>
        </div>

        <!-- Search Section -->
        <div class="search-section">
            <h2 class="page-title">TRA CỨU ĐƠN HÀNG</h2>
            <form action="tracking" method="get" class="order-tracking-search-box">
                <input type="text" name="orderCode" placeholder="Nhập mã đơn hàng của bạn" required value="${param.orderCode}">
                <button type="submit"><i class="fa fa-search"></i> TRA CỨU</button>
            </form>
        </div>

        <!-- Result Section -->
        <c:if test="${not empty error}">
            <div class="tracking-result-container error-message">
                <p>${error}</p>
            </div>
        </c:if>

        <c:if test="${not empty order}">
            <div class="tracking-result-container">
                <!-- Left Column: Order Details -->
                <div class="order-left">
                    <h3 class="block-title">THÔNG TIN ĐƠN HÀNG</h3>

                    <!-- Product List -->
                    <p class="section-title">Danh sách sản phẩm</p>
                    <div class="card-list">
                        <c:forEach items="${order.details}" var="d">
                            <div class="cart-item">
                                <%-- Giả sử bạn có thể lấy ảnh sản phẩm từ variantId --%>
                                <img src="path/to/product/image/${d.variantId}.jpg" alt="sp">
                                <div class="item-info">
                                    <h4>Tên sản phẩm (ID: ${d.variantId})</h4>
                                    <div class="price">
                                        <div class="current-price"><fmt:formatNumber value="${d.unitPrice}" type="currency" currencySymbol="₫" /></div>
                                    </div>
                                    <p>Số lượng: ${d.quantity}</p>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Order Summary -->
                    <div class="order-summary">
                        <table>
                            <tr>
                                <td>Mã đơn hàng</td>
                                <td>${order.orderCode}</td>
                            </tr>
                            <tr>
                                <td>Tên người nhận</td>
                                <td>${order.recipientName}</td>
                            </tr>
                            <tr>
                                <td>Số điện thoại</td>
                                <td>${order.recipientPhone}</td>
                            </tr>
                            <tr>
                                <td>Địa chỉ</td>
                                <td>${order.shippingAddress}</td>
                            </tr>
                            <tr>
                                <td>Ngày đặt hàng</td>
                                <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                            </tr>
                            <tr class="divider"><td colspan="2"></td></tr>
                            <tr>
                                <td>Tổng tiền hàng</td>
                                <td><fmt:formatNumber value="${order.totalPrice - order.shipping.shippingFee}" type="currency" currencySymbol="₫" /></td>
                            </tr>
                            <tr>
                                <td>Phí vận chuyển</td>
                                <td><fmt:formatNumber value="${order.shipping.shippingFee}" type="currency" currencySymbol="₫" /></td>
                            </tr>
                            <tr class="divider"><td colspan="2"></td></tr>
                        </table>
                        <div class="total">
                            <span>TỔNG THANH TOÁN</span>
                            <span class="price"><fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫" /></span>
                        </div>
                    </div>
                </div>

                <!-- Right Column: Shipping Status -->
                <div class="order-right">
                    <h3 class="block-title">TÌNH TRẠNG ĐƠN HÀNG</h3>
                    <div class="order-summary">
                        <table>
                            <tr>
                                <td>Trạng thái</td>
                                <td><span class="status status-${order.shipping.shippingStatus.toLowerCase()}">${order.shipping.shippingStatus}</span></td>
                            </tr>
                            <tr>
                                <td>Mã vận chuyển</td>
                                <td>${order.shipping.trackingNumber}</td>
                            </tr>
                            <tr>
                                <td>Ghi chú</td>
                                <td>${order.note}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>

        <p class="footer-text">
            Quý khách vui lòng kiểm tra lại mã đơn hoặc gọi CSKH: <strong> 0375 1841 444 </strong>
        </p>
    </main>

    <%@ include file="common/footer.jspf" %>
</body>
</html>