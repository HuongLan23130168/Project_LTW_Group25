<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.product_name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/detail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;800&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body>
<c:if test="${not empty sessionScope.msg}">
    <script>
        Swal.fire({
            icon: 'success',
            title: 'Tuyệt vời!',
            text: '${sessionScope.msg}',
            showConfirmButton: true,
            confirmButtonText: 'Xem giỏ hàng',
            showCancelButton: true,
            cancelButtonText: 'Tiếp tục mua sắm',
            confirmButtonColor: '#A79277',
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = '${pageContext.request.contextPath}/cart';
            }
        });
    </script>
    <% session.removeAttribute("msg"); %>
</c:if>

<c:if test="${not empty sessionScope.error}">
    <script>
        Swal.fire({
            icon: 'error',
            title: 'Rất tiếc...',
            text: '${sessionScope.error}',
            confirmButtonColor: '#d33'
        });
    </script>
    <% session.removeAttribute("error"); %>
</c:if>

<jsp:include page="/frontend/header.jsp"/>

<c:choose>
    <c:when test="${not empty product}">
        <c:set var="defaultVariant" value="${product.variants[0]}"/>
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a> &#47;
            <a href="${pageContext.request.contextPath}/list-product">Sản phẩm</a> &#47;
            <span class="current">${product.product_name}</span>
        </div>

        <section class="product-detail">
            <div class="left">
                <img id="mainImage" src="${product.image_url}" alt="${product.product_name}" class="main-img">
                <div class="thumbs">
                    <%-- Sửa: Lấy ảnh từ product.images --%>
                    <c:forEach var="img" items="${product.images}">
                        <img src="${img.image_url}" onclick="changeImage(this.src, this)">
                    </c:forEach>
                </div>
            </div>

            <div class="right">
                <h1>${product.product_name}</h1>

                <div class="price-wrapper">
                    <span class="price-sale" id="price-display">
                        <fmt:formatNumber value="${product.discountPercent > 0 ? product.price_new : product.price}" type="currency" currencySymbol=""/>₫
                    </span>
                    <span class="price-old" id="price-old-display" style="${product.discountPercent > 0 ? '' : 'display:none;'}">
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol=""/>₫
                    </span>
                    <span class="discount" id="discount-tag" style="${product.discountPercent > 0 ? '' : 'display:none;'}">
                        -<fmt:formatNumber value="${product.discountPercent}" pattern="#.##"/>%
                    </span>
                </div>

                <div class="select-group">
                    <label>Màu sắc: <span id="color-text" style="font-weight:normal">${defaultVariant.color}</span></label>
                    <div class="option-list" id="color-options"></div>
                </div>

                <div class="select-group">
                    <label>Kích thước: <span id="size-text" style="font-weight:normal">${defaultVariant.size}</span></label>
                    <div class="option-list" id="size-options"></div>
                </div>

                <div class="quantity-box">
                    <button class="qty-btn minus" id="qty-decrease">−</button>
                    <input type="number" id="quantity" value="1" min="1">
                    <button class="qty-btn plus" id="qty-increase">+</button>
                </div>

                <form id="cartForm" action="${pageContext.request.contextPath}/add-to-cart" method="post" style="display:none;">
                    <input type="hidden" name="variantId" id="selected-variant-id" value="${defaultVariant.id}">
                    <input type="hidden" name="quantity" id="form-quantity" value="1">
                    <input type="hidden" name="redirectAction" id="redirectAction" value="add">
                </form>

                <div class="actions">
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
                <hr>
                <br>
                <strong>Hình ảnh chi tiết:</strong><br><br>
                <div class="detail-images">
                    <c:forEach var="img" items="${product.images}">
                        <img src="${img.image_url}" alt="Ảnh chi tiết">
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
                    <a href="${pageContext.request.contextPath}/detail-product?id=${rel.id}" class="product">
                        <div class="img" style="background-image: url('${rel.image_url}')">
                            <c:if test="${rel.discountPercent > 0}">
                                <div class="discount">
                                    <span>-<fmt:formatNumber value="${rel.discountPercent}" pattern="#.##"/>%</span>
                                </div>
                            </c:if>
                        </div>
                        <div class="product-info">
                            <h4>${rel.product_name}</h4>
                            <div class="price-box">
                                <c:choose>
                                    <c:when test="${rel.discountPercent > 0}">
                                        <span class="price"><fmt:formatNumber value="${rel.price_new}" type="number"/>₫</span>
                                        <span class="old-price"><fmt:formatNumber value="${rel.price}" type="number"/>₫</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="price"><fmt:formatNumber value="${rel.price}" type="number"/>₫</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
            <button class="slide-btn next" onclick="scrollSlider(1)"><i class="fa fa-chevron-right"></i></button>
        </div>
    </section>
</c:if>

<button id="backToTop" title="Lên đầu trang">
    <i class="fa-solid fa-arrow-up"></i>
</button>

<script>
    const productDiscountPercent = ${product.discountPercent};
    const variantsData = [
        <c:if test="${not empty product.variants}">
        <c:forEach var="v" items="${product.variants}" varStatus="loop">
        {
            "id": ${v.id},
            "color": "${v.color}".trim(),
            "size": "${v.size}".trim(),
            "price": ${v.price},
            "material": "${v.material}",
            "style": "${v.style}"
        }${!loop.last ? ',' : ''}
        </c:forEach>
        </c:if>
    ];
    let selectedColor = "${defaultVariant.color}".trim();
    let selectedSize = "${defaultVariant.size}".trim();

    function submitCart(type) {
        const qty = document.getElementById('quantity').value;
        document.getElementById('form-quantity').value = qty;
        document.getElementById('redirectAction').value = type;
        document.getElementById('cartForm').submit();
    }
</script>

<jsp:include page="/frontend/footer.jsp"/>
<script src="${pageContext.request.contextPath}/frontend/js/detail.js"></script>
</body>
</html>