<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Noble Loft Theory - Chi tiết đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/viewOrders.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <!-- Sidebar -->
    <%@ include file="common/admin_sidebar.jspf" %>

    <!-- Header -->
    <%@ include file="common/admin_header.jspf" %>

    <main class="main-content">
        <c:if test="${empty order}">
            <div class="customers-header">
                <h1>Không tìm thấy đơn hàng</h1>
                <p>Đơn hàng bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
            </div>
            <div class="back-to-orders" style="margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/admin/orders">Quay lại danh sách</a>
            </div>
        </c:if>

        <c:if test="${not empty order}">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/admin/orders">Đơn hàng</a> &#47;
                <span class="current">Chi tiết đơn hàng</span>
            </div>

            <div class="transaction-wrapper">
                <!-- ===== CỘT BÊN TRÁI ===== -->
                <div class="left-card">
                    <div class="orders-id">
                        <a>Mã đơn: #${order.order_code}
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
                            <c:if test="${not empty order.order_date}">
                                <span> | <fmt:formatDate value="${order.order_date}" pattern="dd/MM/yyyy HH:mm"/></span>
                            </c:if>
                        </a>
                    </div>

                    <div class="detail-card">
                        <h3>Thông tin khách hàng</h3>
                        <table>
                            <tr>
                                <td>Tên</td>
                                <td>${order.customerName}</td>
                            </tr>
                            <tr>
                                <td>Email</td>
                                <td>${order.customerEmail}</td>
                            </tr>
                            <tr>
                                <td>SDT</td>
                                <td>${order.customerPhone}</td>
                            </tr>
                            <tr>
                                <td>Địa chỉ</td>
                                <td>${order.customerAddress}</td>
                            </tr>
                            <tr>
                                <td>PT thanh toán</td>
                                <td>${order.paymentMethod}</td>
                            </tr>
                        </table>
                    </div>


                </div>

                <!-- ===== CỘT BÊN PHẢI ===== -->
                <div class="right-card">
                    <div class="detail-card">
                        <h3>Danh sách sản phẩm</h3>
                        <c:if test="${empty orderItems}">
                            <p>Không có sản phẩm nào trong đơn hàng này.</p>
                        </c:if>
                        <c:if test="${not empty orderItems}">
                            <table>
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Sản phẩm</th>
                                        <th>SL</th>
                                        <th>Giá</th>
                                        <th>Giảm</th>
                                        <th>Tổng</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${orderItems}" varStatus="stt">
                                        <tr>
                                            <td>${stt.count}</td>
                                            <td>${item.name}</td>
                                            <td>${item.quantity}</td>
                                            <td><fmt:formatNumber value="${item.price}" type="number"/>₫</td>
                                            <td>${item.discount}%</td>
                                            <td><fmt:formatNumber value="${item.total}" type="number"/>₫</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="total">
                                <p>Phí ship: <fmt:formatNumber value="${order.shippingFee}" type="number"/>₫</p>
                                <h4>Tổng cộng: <fmt:formatNumber value="${order.grandTotal}" type="number"/>₫</h4>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="back-to-orders">
                <a href="${pageContext.request.contextPath}/admin/orders">Quay lại</a>
            </div>
        </c:if>
    </main>

    <script src="${pageContext.request.contextPath}/admin/js/main.js"></script>
</body>
</html>
