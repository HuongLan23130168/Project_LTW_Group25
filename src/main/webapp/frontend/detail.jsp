<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.product_name}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/detail.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;800&display=swap" rel="stylesheet">
</head>

<body>
<jsp:include page="header.jsp"/>

<c:choose>
    <c:when test="${not empty product}">
        <%-- Lấy biến thể đầu tiên làm mặc định --%>
        <c:set var="defaultVariant" value="${product.variants[0]}"/>

        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a> &#47;
            <a href="${pageContext.request.contextPath}/products">Sản phẩm</a> &#47;
            <span class="current">${product.product_name}</span>
        </div>

        <section class="product-detail">
            <div class="left">
                <img id="mainImage" src="${defaultVariant.image_url}" alt="${product.product_name}" class="main-img">
                <div class="thumbs">
                    <c:forEach var="v" items="${product.variants}" begin="0" end="2">
                        <img src="${v.image_url}" onclick="changeImage(this.src, this)">
                    </c:forEach>
                    <c:if test="${not empty product.images}">
                        <c:forEach var="img" items="${product.images}">
                            <img src="${img.image_url}" onclick="changeImage(this.src, this)">
                        </c:forEach>
                    </c:if>
                </div>
            </div>

            <div class="right">
                <h1>${product.product_name}</h1>

                <div class="price-wrapper">
                    <p class="price-sale" id="price-display">
                        <fmt:formatNumber value="${defaultVariant.price}" type="currency" currencySymbol=""/>₫
                    </p>
                    <p class="price-old" style="display:none;">3.200.000₫</p>
                    <p class="discount" style="display:none;">-41%</p>
                </div>

                <div class="select-group">
                    <label>Màu sắc: <span id="color-text" style="font-weight:normal">${defaultVariant.color}</span></label>
                    <div class="option-list" id="color-options">
                    </div>
                </div>

                <div class="select-group">
                    <label>Kích thước: <span id="size-text" style="font-weight:normal">${defaultVariant.size}</span></label>
                    <div class="option-list" id="size-options">
                    </div>
                </div>

                <div class="quantity-box">
                    <button class="qty-btn minus" id="qty-decrease">−</button>
                    <input type="number" id="quantity" value="1" min="1">
                    <button class="qty-btn plus" id="qty-increase">+</button>
                </div>

                <div class="actions">
                    <form id="cartForm" action="${pageContext.request.contextPath}/cart/add" method="post" style="display:none;">
                        <input type="hidden" name="variantId" id="selected-variant-id" value="${defaultVariant.id}">
                        <input type="hidden" name="quantity" id="form-quantity" value="1">
                    </form>

                    <a href="javascript:void(0)" class="add-cart" onclick="submitCart('add')">
                        <i class="fa fa-cart-plus"></i> Thêm vào giỏ hàng
                    </a>
                    <a href="javascript:void(0)" class="buy-now" onclick="submitCart('buy')">
                        Mua ngay
                    </a>
                </div>
            </div>
        </section>

        <section class="detail-section">
            <h2>Mô tả chi tiết</h2>
            <div class="description">
                    ${product.description}

                <hr>
                <strong>Thông số kỹ thuật:</strong><br>
                • Mã sản phẩm: ${product.product_code}<br>
                • Chất liệu: <span id="info-material">${defaultVariant.material}</span><br>
                • Kiểu dáng: <span id="info-style">${defaultVariant.style}</span><br>
                • Xuất xứ: Việt Nam (Project Group 25)<br><br>

                <hr><br>
                <strong>Hình ảnh chi tiết:</strong><br><br>
                <div class="detail-images">
                    <c:forEach var="v" items="${product.variants}">
                        <img src="${v.image_url}" alt="Ảnh chi tiết">
                    </c:forEach>
                </div>
            </div>
        </section>

    </c:when>
    <c:otherwise>
        <div style="text-align: center; padding: 100px;">
            <h2>Sản phẩm không tồn tại</h2>
        </div>
    </c:otherwise>
</c:choose>

<c:if test="${not empty relatedProducts}">
    <section class="related-products">
        <h2>Sản phẩm tương tự</h2>
        <div class="slider-container">
            <button class="slide-btn prev" onclick="scrollSlider(-1)"><i class="fa fa-chevron-left"></i></button>
            <div class="slider" id="productSlider">

                <c:forEach var="rel" items="${relatedProducts}">
                    <c:if test="${not empty rel.variants}">
                        <a href="${pageContext.request.contextPath}/detail?id=${rel.id}" class="product">
                            <img src="${rel.variants[0].image_url}" alt="${rel.product_name}">
                            <h4>${rel.product_name}</h4>
                            <p class="price">
                                <fmt:formatNumber value="${rel.variants[0].price}" type="currency" currencySymbol=""/>₫
                            </p>
                        </a>
                    </c:if>
                </c:forEach>

            </div>
            <button class="slide-btn next" onclick="scrollSlider(1)"><i class="fa fa-chevron-right"></i></button>
        </div>
    </section>
</c:if>

<button id="backToTop" title="Lên đầu trang">
    <i class="fa-solid fa-arrow-up"></i>
</button>

<jsp:include page="footer.jsp"/>

<script>
    // 1. CHUẨN BỊ DỮ LIỆU TỪ SERVER (JAVA) SANG CLIENT (JS)
    const variantsData = [
        <c:if test="${not empty product.variants}">
        <c:forEach var="v" items="${product.variants}" varStatus="loop">
        {
            "id": ${v.id},
            "color": "${v.color}".trim(), // Loại bỏ khoảng trắng thừa
            "size": "${v.size}".trim(),
            "price": ${v.price},
            "price_old": ${v.price_old},
            // Tính % giảm giá trực tiếp tại đây để đảm bảo có số liệu
            "discount": ${v.price_old > v.price ? Math.round((v.price_old - v.price) / v.price_old * 100) : 0},
            "image_url": "${v.image_url}",
            "material": "${v.material}",
            "style": "${v.style}"
        }${!loop.last ? ',' : ''}
        </c:forEach>
        </c:if>
    ];

    // Lấy biến thể mặc định (đầu tiên)
    let selectedColor = "${defaultVariant.color}".trim();
    let selectedSize = "${defaultVariant.size}".trim();
</script>

<script src="${pageContext.request.contextPath}/frontend/js/detail.js"></script>
<script src="${pageContext.request.contextPath}/frontend/js/header.js"></script>
</body>
</html>