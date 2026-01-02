<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/cart.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;800&display=swap" rel="stylesheet">
</head>

<body>
<style>
    /* .container {
        margin: 20px;
        width: 100%;
        margin-bottom: 80px;
    } */

    /* === BREADCRUMB === */
    .breadcrumb {
        margin: 20px 40px 10px;
        color: #333;
    }

    .breadcrumb a {
        text-decoration: none;
        color: #000;
    }

    .breadcrumb a:hover {
        text-decoration: underline;
        color: #74512D;
    }

    .breadcrumb .current {
        color: #74512D;
        font-weight: 700;
    }

    /* === TIẾN TRÌNH === */
    .progress {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 80px;
        margin: 20px 0 40px;
    }

    .progress .step {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 10px 18px;
        border: 2px solid;
        border-radius: 20px;
        font-weight: 600;
        color: #999;
        position: relative;
        background-color: #fff;
        transition: all 0.3s ease;
    }

    .progress .step i {
        margin-right: 6px;
    }

    .progress .step.active {
        color: #fff;
        background-color: #ECB176;
    }

    .progress .step::after {
        content: "";
        position: absolute;
        right: -60px;
        top: 50%;
        width: 50px;
        height: 2px;
        background-color: #ddd;
        transform: translateY(-50%);
    }

    .progress .step:last-child::after {
        display: none;
    }

    /* === SẢN PHẨM === */
    .cart-item {
        display: flex;
        align-items: center;
        background: #fff;
        border: 2px solid #eee;
        border-radius: 10px;
        padding: 10px 15px;
        margin-bottom: 15px;
        gap: 15px;
    }

    .cart-item img {
        width: 80px;
        height: 80px;
        border-radius: 8px;
        object-fit: cover;
    }

    .cart-item .item-info {
        flex: 1;
    }

    .cart-item h4 {
        font-size: 15px;
        margin: 3px 0;
        color: #333;
    }

    /* === MÀU SẮC === */
    .color {
        font-size: 13px;
        color: #666;
        margin-bottom: 6px;
        display: flex;
        align-items: center;
        gap: 6px;
    }

    .color-box {
        display: inline-block;
        width: 16px;
        height: 16px;
        border-radius: 50%;
        border: 1px solid #ccc;
    }

    .color-name {
        font-weight: 500;
    }

    /* === GIÁ === */
    .price {
        display: flex;
        align-items: baseline;
        gap: 8px;
    }

    .current-price {
        color: #e67e22;
        font-weight: 700;
        font-size: 15px;
    }

    .old-price {
        color: #aaa;
        font-size: 13px;
        text-decoration: line-through;
    }

    .discount {
        color: #d40004;
        font-weight: 600;
        font-size: 13px;
    }

    /* Nút thanh toán */
    .checkout-btn {
        display: block;
        text-align: center;
        background: #A79277;
        color: #fff;
        text-decoration: none;
        padding: 12px;
        border-radius: 8px;
        font-weight: 550;
        margin-top: 20px;
        transition: background 0.3s;
    }

    .checkout-btn:hover {
        background: #74512D;
    }
</style>

<!-- === HEADER === -->
<jsp:include page="/frontend/header.jsp"/>


<!-- ===== BREADCRUMB / TIẾN TRÌNH ===== -->
<div class="breadcrumb">
    <a href="${pageContext.request.contextPath}/home">Trang chủ</a> &#47;
    <a href="detail.jsp">Chi tiết sản phẩm</a> &#47;
    <span class="current">Giỏ hàng</span>
</div>

<div class="progress">
    <div class="step active"><i class="fa fa-cart-shopping"></i> Giỏ hàng</div>
    <div class="step"><i class="fa fa-credit-card"></i> Thông tin đặt hàng</div>
    <div class="step"><i class="fa fa-check-circle"></i> Hoàn tất</div>
</div>

<!-- ===== KHUNG CHÍNH ===== -->
<%--<div class="cart-container">--%>
<%--    <!-- CỘT TRÁI -->--%>
<%--    <div class="cart-left">--%>
<%--        <div class="select-all">--%>
<%--            <input type="checkbox" id="selectAll">--%>
<%--            <label for="selectAll">Chọn tất cả <span>(0 sản phẩm)</span></label>--%>
<%--        </div>--%>

<%--        <!-- === SẢN PHẨM GIỎ HÀNG === -->--%>
<%--        <div class="cart-list">--%>
<%--            <!-- === SẢN PHẨM 1 === -->--%>
<%--            <div class="cart-item">--%>
<%--                <input type="checkbox" class="item-check">--%>
<%--                <img src="https://product.hstatic.net/200000486527/product/img_4118_3465223127a34592a0671827a04680e8_master.jpg"--%>
<%--                     alt="sp">--%>

<%--                <div class="item-info">--%>
<%--                    <h4>Thảm trải sàn cao cấp Arcus phong cách Bắc Âu</h4>--%>

<%--                    <div class="price">--%>
<%--                        <span class="current-price">1.125.000₫</span>--%>
<%--                        <span class="old-price">1.500.000₫</span>--%>
<%--                        <span class="discount">-25%</span>--%>
<%--                    </div>--%>
<%--                </div>--%>

<%--                <div class="quantity">--%>
<%--                    <button>-</button>--%>
<%--                    <span>1</span>--%>
<%--                    <button>+</button>--%>
<%--                </div>--%>
<%--                <button class="delete-btn"><i class="fa fa-trash"></i></button>--%>
<%--            </div>--%>

<%--            <!-- === SẢN PHẨM 2 === -->--%>
<%--            <div class="cart-item">--%>
<%--                <input type="checkbox" class="item-check">--%>
<%--                <img src="https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m14zw7atgqred4" alt="sp">--%>

<%--                <div class="item-info">--%>
<%--                    <h4>Đồ chơi sang trọng dài tay ngôi sao dễ thương</h4>--%>

<%--                    <div class="color">Màu sắc:--%>
<%--                        <span class="color-box" style="background-color: #000;"></span>--%>
<%--                        <span class="color-name">Đen</span>--%>
<%--                    </div>--%>

<%--                    <div class="price">--%>
<%--                        <span class="current-price">159.000₫</span>--%>
<%--                    </div>--%>
<%--                </div>--%>

<%--                <div class="quantity">--%>
<%--                    <button>-</button>--%>
<%--                    <span>1</span>--%>
<%--                    <button>+</button>--%>
<%--                </div>--%>
<%--                <button class="delete-btn"><i class="fa fa-trash"></i></button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

<%--    <!-- CỘT PHẢI -->--%>
<%--    <div class="cart-right">--%>
<%--        <h3>THÔNG TIN THANH TOÁN</h3>--%>
<%--        <hr class="divider">--%>

<%--        <div class="quantity-total">--%>
<%--            <p>Số lượng sản phẩm: </p>--%>
<%--            <span>2</span>--%>
<%--        </div>--%>

<%--        <div class="total">--%>
<%--            <p>Tổng tiền: </p>--%>
<%--            <span>1.284.000₫</span>--%>
<%--        </div>--%>
<%--        <hr class="divider">--%>

<%--        <div class="final-total">--%>
<%--            <p>Tổng thanh toán: </p>--%>
<%--            <span>285.000₫</span>--%>
<%--        </div>--%>

<%--        <a href="pay.jsp" class="checkout-btn">ĐI ĐẾN THANH TOÁN</a>--%>
<%--        --%>
<%--    </div>--%>
<%--</div>--%>
<div class="cart-container">
    <c:choose>
        <%-- TRƯỜNG HỢP 1: CÓ SẢN PHẨM --%>
        <c:when test="${not empty sessionScope.cart}">
            <div class="cart-left">
                <div class="select-all">
                    <input type="checkbox" id="selectAll">
                    <label for="selectAll">Chọn tất cả <span>(${sessionScope.cart.size()} sản phẩm)</span></label>
                </div>

                <div class="cart-list">
                    <c:set var="total" value="0"/>
                    <c:forEach var="entry" items="${sessionScope.cart}">
                        <c:set var="item" value="${entry.value}"/>
                        <%-- Cộng dồn tổng tiền: giá * số lượng --%>
                        <c:set var="total" value="${total + (item.price * item.quantity)}"/>

                        <div class="cart-item">
                            <input type="checkbox" class="item-check">

                            <img src="${item.image}" alt="${item.name}">

                            <div class="item-info">
                                <h4>${item.name}</h4>

                                <div class="color">
                                    <span class="color-name">Màu: ${item.color}</span> |
                                    <span class="size-name">Size: ${item.size}</span>
                                </div>

                                <div class="price">
                                    <span class="current-price">
                                        <fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###"/>₫
                                    </span>
                                </div>
                            </div>

                            <div class="quantity">
                                <a href="cart?action=update&id=${item.variantId}&quantity=${item.quantity - 1}"
                                   class="qty-btn">-</a>
                                <span>${item.quantity}</span>
                                <a href="cart?action=update&id=${item.variantId}&quantity=${item.quantity + 1}"
                                   class="qty-btn">+</a>
                            </div>

                            <a href="cart?action=delete&id=${item.variantId}" class="delete-btn"
                               onclick="return confirm('Xóa sản phẩm này?')">
                                <i class="fa fa-trash"></i>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="cart-right">
                <h3>THÔNG TIN THANH TOÁN</h3>
                <hr class="divider">
                <div class="quantity-total">
                    <p>Số lượng loại sản phẩm: </p>
                    <span>${sessionScope.cart.size()}</span>
                </div>
                <div class="total">
                    <p>Tổng tiền: </p>
                    <span><fmt:formatNumber value="${total}" pattern="#,###"/>₫</span>
                </div>
                <hr class="divider">
                <div class="final-total">
                    <p>Tổng thanh toán: </p>
                    <span style="color: #d40004; font-size: 20px; font-weight: bold;">
                        <fmt:formatNumber value="${total}" pattern="#,###"/>₫
                    </span>
                </div>
                <a href="pay.jsp" class="checkout-btn">ĐI ĐẾN THANH TOÁN</a>
            </div>
        </c:when>

        <%-- TRƯỜNG HỢP 2: TRỐNG --%>
        <c:otherwise>
            <div style="text-align: center; padding: 100px 0; width: 100%;">
                <i class="fa-solid fa-cart-shopping" style="font-size: 64px; color: #eee; margin-bottom: 20px;"></i>
                <h2>Giỏ hàng của bạn đang trống</h2>
                <a href="${pageContext.request.contextPath}/home" class="checkout-btn"
                   style="width: 200px; margin: 20px auto;">MUA SẮM NGAY</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%--    <div class="cart-right">--%>
<%--        <h3>THÔNG TIN THANH TOÁN</h3>--%>
<%--        <hr class="divider">--%>

<%--        <div class="quantity-total">--%>
<%--            <p>Số lượng loại sản phẩm: </p>--%>
<%--            <span>${sessionScope.cart.size()}</span>--%>
<%--        </div>--%>

<%--        <div class="total">--%>
<%--            <p>Tổng tiền: </p>--%>
<%--            <span><fmt:formatNumber value="${total}" type="currency" currencySymbol=""/>₫</span>--%>
<%--        </div>--%>
<%--        <hr class="divider">--%>

<%--        <div class="final-total">--%>
<%--            <p>Tổng thanh toán: </p>--%>
<%--            <span style="color: #d40004; font-size: 20px; font-weight: bold;">--%>
<%--            <fmt:formatNumber value="${total}" type="currency" currencySymbol=""/>₫--%>
<%--        </span>--%>
<%--        </div>--%>

<%--        <a href="pay.jsp" class="checkout-btn">ĐI ĐẾN THANH TOÁN</a>--%>
<%--    </div>--%>
</div>
<!-- === FOOTER === -->
<footer class="footer">
    <div class="container">
        <div class="footer-columns">
            <!-- Cột 1: Giới thiệu -->
            <div class="footer-col">
                <h3>Giới thiệu</h3>
                <p>Chào mừng bạn đến với <strong>Noble Loft Theory</strong> — không gian dành cho những ai yêu thích
                    cái đẹp và nghệ thuật trang trí nội thất.</p>
                <p>Chúng tôi mang đến các sản phẩm decor trang trí nhà với phong cách hiện đại, tối giản nhưng vẫn
                    giữ được sự tinh tế trong từng chi tiết.</p>

            </div>

            <!-- Cột 2: Liên kết -->
            <div class="footer-col">
                <h3>Liên kết</h3>
                <ul>
                    <li><a>Chính sách đổi trả hoàn hàng</a></li>
                    <li><a>Chính sách bảo mật mật khẩu</a></li>
                    <li><a>Hướng dẫn mua hàng, sản phẩm</a></li>
                    <li><a>Chính sách kiểm hàng hóa vận chuyển</a></li>
                    <li><a>Chính sách giao hàng tận nơi</a></li>
                    <li><a>Hướng dẫn thanh toán đơn hàng</a></li>
                </ul>

            </div>

            <!-- Cột 3: Thông tin liên hệ -->
            <div class="footer-col">
                <h3>Thông tin liên hệ</h3>
                <p><i class="fa fa-map-marker"></i>Khu phố 33, P.Linh Xuân, TP.HCM</p>
                <p><i class="fa fa-map-marker"></i> Đại học Nông Lâm TP.Hồ Chí Minh</p>
                <p><i class="fa fa-phone"></i> Liên hệ: 03751841444 - 03381776315 </p>

                <p><i class="fa fa-envelope"></i> <a
                        href="mailto:NLT@noblelofttheory.com">NLT@noblelofttheory.com</a></p>
            </div>


            <!-- Cột 4: Fanpage -->
            <div class="footer-col">
                <h3>Fanpage</h3>
                <div class="fanpage-box">
                    <p>Liên hệ ngay trang chủ của shop Noble Loft Theory.</p>
                    <p>Nếu bạn đang có thắc mắc gì ở sản phẩm.</p>
                    <p>Fanpage, Youtube và Instagram.</p>


                    <div class="social-box">
                        <div class="social-icons">
                            <!-- mấy cái # này là chưa có link liên kết nào có gắn v -->
                            <a href="https://www.facebook.com/share/1HP5fZGNqb/?mibextid=wwXIfr">
                                <i class="fa-brands fa-facebook"></i></a>
                            <a href="https://www.instagram.com/nltnoblelofttheory/">
                                <i class="fa-brands fa-instagram"></i></a>
                            <a href="https://www.youtube.com/channel/UC931-4vCWPGos5fSNQ8Rh-g">
                                <i class="fa-brands fa-youtube"></i></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="footer-bottom">
        <p>Copyright © 2025 NLT Noble Loft Theory. Powered by NLT </p>
    </div>
    </div>
</footer>

</body>

</html>