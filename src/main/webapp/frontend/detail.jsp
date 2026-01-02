<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Sản Phẩm - ${product.product_name}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/detail.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;800&display=swap" rel="stylesheet">
</head>
<body>
<!-- HEADER -->
<jsp:include page="header.jsp"/>

<!-- === BREADCRUMB === -->
<div class="breadcrumb">
    <a href="${pageContext.request.contextPath}/frontend/home.jsp">Trang chủ</a> &#47;
    <a href="${pageContext.request.contextPath}/frontend/living.jsp">Sản phẩm</a> &#47;
    <c:if test="${not empty product}">
        <span class="current">${product.product_name}</span>
    </c:if>
</div>

<%-- Kiểm tra xem sản phẩm và biến thể có tồn tại không --%>
<c:if test="${not empty product and not empty product.variants}">
    <c:set var="defaultVariant" value="${product.variants[0]}"/>

    <section class="product-detail">
        <div class="left">
                <%-- 1. ẢNH CHÍNH (Ảnh đại diện từ biến thể) --%>
            <div class="main-img-container">
                <img id="main-product-img" class="main-img"
                     src="${defaultVariant.image_url}"
                     alt="${product.product_name}">
            </div>

                <%-- 2. ẢNH CHI TIẾT (Thumbnails từ bảng product_images) --%>
            <c:if test="${not empty product.images}">
                <div class="thumbs">
                        <%-- Thêm ảnh đại diện vào danh sách thumbnail để người dùng có thể click lại --%>
                    <img src="${defaultVariant.image_url}"
                         class="thumb-item active"
                         onclick="changeImage(this.src, this)"
                         alt="Main thumbnail">

                        <%-- Duyệt danh sách ảnh chi tiết từ database --%>
                    <c:forEach var="img" items="${product.images}">
                        <img src="${img.image_url}"
                             class="thumb-item"
                             onclick="changeImage(this.src, this)"
                             alt="Detail image">
                    </c:forEach>
                </div>
            </c:if>
        </div>

        <!-- RIGHT -->
        <div class="right">
            <h1>${product.product_name}</h1>
            <div class="price-wrapper">
                <span class="price-sale">${defaultVariant.price} ₫</span>
            </div>
            <ul class="info-list">
                <li><b>Danh mục:</b> ${product.category_id}</li>
                <li><b>Màu sắc:</b> ${defaultVariant.color}</li>
                <li><b>Kích thước:</b> ${defaultVariant.size}</li>
            </ul>
            <div class="quantity-box">
                <button class="qty-btn">-</button>
                <input type="number" value="1" min="1">
                <button class="qty-btn">+</button>
            </div>
            <div class="actions">
                <button class="add-cart">Thêm vào giỏ</button>
                <button class="buy-now">Mua ngay</button>
            </div>
        </div>
    </section>

    <!-- === SECTION 2: MÔ TẢ CHI TIẾT === -->
    <section class="detail-section">
        <h2>Mô tả chi tiết</h2>
        <div class="description">
            <strong>Tên sản phẩm:</strong> ${product.product_name} <br>
            <strong>Mã sản phẩm:</strong> ${product.product_code} <br>
            <hr>
            <strong>Mô tả chung:</strong> ${product.description} <br>
            <hr>
            <strong>Thông tin chi tiết phiên bản:</strong><br>
            <strong>Giá bán:</strong> ${defaultVariant.price} ₫ <br>
            <strong>Màu sắc:</strong> ${defaultVariant.color} <br>
            <strong>Phong cách:</strong> ${defaultVariant.style} <br>
            <strong>Chất liệu:</strong> ${defaultVariant.material} <br>
            <strong>Kích thước:</strong> ${defaultVariant.size} <br>


        </div>
    </section>
</c:if>
    <!-- === SECTION 3: SẢN PHẨM TƯƠNG TỰ === -->
    <section class="related-products">
        <h2>Sản phẩm tương tự</h2>
        <div class="slider-container">
            <button class="slide-btn prev" onclick="scrollSlider(-1)"><i class="fa fa-chevron-left"></i></button>
            <div class="slider" id="productSlider">
                <c:forEach var="relatedProduct" items="${relatedProducts}">
                    <c:if test="${not empty relatedProduct.variants}">
                        <c:set var="relatedVariant" value="${relatedProduct.variants[0]}"/>
                        <div class="product">
                            <a href="${pageContext.request.contextPath}/detail?id=${relatedProduct.id}">
                                <img src="${relatedVariant.image_url}" alt="${relatedProduct.product_name}">
                                <h4>${relatedProduct.product_name}</h4>
                                <p class="price">${relatedVariant.price} ₫</p>
                            </a>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
            <button class="slide-btn next" onclick="scrollSlider(1)"><i class="fa fa-chevron-right"></i></button>
        </div>
    </section>

<!-- NÚT LÊN ĐẦU TRANG -->
<button id="backToTop" title="Lên đầu trang">
    <i class="fa-solid fa-arrow-up"></i>
</button>


<%-- Nếu sản phẩm hoặc biến thể không tồn tại, hiển thị thông báo --%>
<c:if test="${empty product or empty product.variants}">
    <div style="text-align: center; padding: 50px;">
        <h2>Rất tiếc, không tìm thấy thông tin chi tiết cho sản phẩm này.</h2>
        <p>Vui lòng kiểm tra lại ID sản phẩm và đảm bảo sản phẩm có ít nhất 1 biến thể trong cơ sở dữ liệu.</p>
    </div>
</c:if>


<!-- FOOTER -->
<jsp:include page="footer.jsp"/>

<script src="${pageContext.request.contextPath}/frontend/js/header.js"></script>
<script src="${pageContext.request.contextPath}/frontend/js/detail.js"></script>
<script>
    function changeImage(src, element) {
        // 1. Thay đổi nguồn ảnh cho ảnh chính
        document.getElementById('main-product-img').src = src;

        // 2. Xử lý hiệu ứng Border (Active) cho Thumbnail
        const thumbs = document.querySelectorAll('.thumb-item');
        thumbs.forEach(thumb => thumb.classList.remove('active'));

        // 3. Thêm class active vào ảnh vừa click
        element.classList.add('active');
    }
</script>
</body>
</html>