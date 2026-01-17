<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Noble Loft Theory - View Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/viewOrders.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>
    <!-- === SIDEBAR === -->
    <%@ include file="common/admin_sidebar.jspf" %>
    <!-- === HEADER === -->
    <%@ include file="common/admin_header.jspf" %>

    <!-- === VIEW ORDERS === -->
    <main class="main-content">
        <c:if test="${empty orderId}">
            <div class="customers-header">
                <h1>Không tìm thấy đơn hàng</h1>
                <p>Đơn hàng bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
            </div>
            <div class="back-to-orders" style="margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/admin/orders">Quay lại danh sách</a>
            </div>
        </c:if>

        <c:if test="${not empty orderId}">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/admin/orders">Đơn hàng</a> &#47;
                <span class="current">Chi tiết đơn hàng</span>
            </div>

            <div class="transaction-wrapper">
                <div class="left-card">
                    <div class="orders-id">
                        <a>Mã đơn: #${orderId}
                            <c:set var="lowerStatus" value="${fn:toLowerCase(orderStatus)}" />
                            <c:choose>
                                <c:when test="${fn:contains(lowerStatus, 'hoàn thành') or fn:contains(lowerStatus, 'đã giao')}">
                                    <span class="status status-completed">${orderStatus}</span>
                                </c:when>
                                <c:when test="${fn:contains(lowerStatus, 'hủy')}">
                                    <span class="status status-cancelled">${orderStatus}</span>
                                </c:when>
                                <c:when test="${fn:contains(lowerStatus, 'xử lý') or fn:contains(lowerStatus, 'chờ')}">
                                    <span class="status status-pending">${orderStatus}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status">${orderStatus}</span>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${not empty orderDate}">
                                <span> | <fmt:formatDate value="${orderDate}" pattern="dd/MM/yyyy HH:mm"/></span>
                            </c:if>
                        </a>
                    </div>

                    <div class="detail-card">
                        <h3>Thông tin khách hàng</h3>
                        <table>
                            <tr>
                                <td>Tên</td>
                                <td>${customerName}</td>
                            </tr>
                            <tr>
                                <td>Email</td>
                                <td>${customerEmail}</td>
                            </tr>
                            <tr>
                                <td>SDT</td>
                                <td>${customerPhone}</td>
                            </tr>
                            <tr>
                                <td>Địa chỉ</td>
                                <td>${customerAddress}</td>
                            </tr>
                            <tr>
                                <td>PT thanh toán</td>
                                <td> ${paymentMethod} </td>
                            </tr>
                        </table>
                    </div>

                    <div class="detail-card">
                        <h3>Lịch sử trạng thái</h3>
                        <table>
                            <tr>
                                <th>Trạng thái</th>
                                <th>Thời gian</th>
                            </tr>
                            <c:forEach var="history" items="${statusHistory}">
                                <tr>
                                    <td>${history.status}</td>
                                    <td><fmt:formatDate value="${history.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>

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
                                <p>Phí ship: <fmt:formatNumber value="${shippingFee}" type="number"/>₫</p>
                                <h4>Tổng cộng: <fmt:formatNumber value="${grandTotal}" type="number"/>₫</h4>
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
