/* frontend/js/detail.js */

document.addEventListener('DOMContentLoaded', () => {
  // Kiểm tra dữ liệu có tồn tại không
  if (typeof variantsData !== 'undefined' && variantsData.length > 0) {
    console.log("Dữ liệu biến thể:", variantsData); // Log để kiểm tra
    renderColors();
    renderSizes();
    updateProductState();
  } else {
    console.warn("Không có dữ liệu biến thể (variantsData)");
  }
});

// --- 1. RENDER MÀU SẮC ---
function renderColors() {
  // Lấy danh sách màu duy nhất
  const uniqueColors = [...new Set(variantsData.map(v => v.color))];
  const container = document.getElementById('color-options');

  if (!container) return;

  container.innerHTML = uniqueColors.map(color => {
    const isActive = color === selectedColor ? 'active' : '';
    // Sử dụng onclick truyền tham số chuỗi an toàn
    return `<div class="option ${isActive}" onclick="selectColor('${color}')">${color}</div>`;
  }).join('');
}

// --- 2. RENDER KÍCH THƯỚC ---
function renderSizes() {
  // Chỉ lấy các size CÓ SẴN cho màu đang chọn
  const availableSizes = variantsData
      .filter(v => v.color === selectedColor)
      .map(v => v.size);

  // Lấy tất cả các size để hiển thị (bao gồm cả size không khả dụng - disabled)
  const allSizes = [...new Set(variantsData.map(v => v.size))];

  const container = document.getElementById('size-options');
  if (!container) return;

  container.innerHTML = allSizes.map(size => {
    const isAvailable = availableSizes.includes(size);
    const isActive = size === selectedSize ? 'active' : '';

    // Nếu không khả dụng: thêm class disabled và không gán onclick
    if (isAvailable) {
      return `<div class="option ${isActive}" onclick="selectSize('${size}')">${size}</div>`;
    } else {
      return `<div class="option disabled" style="opacity: 0.5; cursor: not-allowed;">${size}</div>`;
    }
  }).join('');
}

// --- 3. XỬ LÝ CHỌN MÀU ---
function selectColor(color) {
  if (selectedColor === color) return; // Không làm gì nếu click lại màu đang chọn

  selectedColor = color;

  // Khi đổi màu, kiểm tra xem Size hiện tại có hợp lệ với màu mới không
  const validSizes = variantsData
      .filter(v => v.color === color)
      .map(v => v.size);

  // Nếu size hiện tại không có trong màu mới -> Chọn size khả dụng đầu tiên
  if (!validSizes.includes(selectedSize)) {
    selectedSize = validSizes[0];
  }

  updateProductState();
}

// --- 4. XỬ LÝ CHỌN SIZE ---
function selectSize(size) {
  if (selectedSize === size) return;
  selectedSize = size;
  updateProductState();
}

// --- 5. CẬP NHẬT GIAO DIỆN (QUAN TRỌNG) ---
function updateProductState() {
  // Render lại để cập nhật class .active
  renderColors();
  renderSizes();

  // Tìm variant khớp với Color & Size hiện tại
  const current = variantsData.find(v => v.color === selectedColor && v.size === selectedSize);

  if (current) {
    console.log("Variant đang chọn:", current);

    // A. Cập nhật Text Màu/Size
    const colorText = document.getElementById('color-text');
    if(colorText) colorText.innerText = current.color;

    const sizeText = document.getElementById('size-text');
    if(sizeText) sizeText.innerText = current.size;

    // B. Cập nhật Giá & Giảm giá
    const formatter = new Intl.NumberFormat('vi-VN');
    const priceDisplay = document.getElementById('price-display');
    const priceOld = document.querySelector('.price-old'); // Chọn class
    const discountTag = document.querySelector('.discount'); // Chọn class

    if(priceDisplay) {
      priceDisplay.innerText = formatter.format(current.price) + '₫';
    }

    // Logic hiển thị giá cũ: Chỉ hiện nếu Price Old > Price
    if (current.price_old > current.price) {
      if(priceOld) {
        priceOld.style.display = 'block'; // Hiện
        priceOld.innerText = formatter.format(current.price_old) + '₫';
      }
      if(discountTag) {
        discountTag.style.display = 'block'; // Hiện
        discountTag.innerText = '-' + current.discount + '%';
      }
    } else {
      // Ẩn đi nếu không giảm giá
      if(priceOld) priceOld.style.display = 'none';
      if(discountTag) discountTag.style.display = 'none';
    }

    // C. Cập nhật Ảnh
    const mainImg = document.getElementById('mainImage');
    if(mainImg && current.image_url) {
      mainImg.src = current.image_url;
    }

    // D. Cập nhật thông số kỹ thuật (nếu có)
    const infoMat = document.getElementById('info-material');
    if(infoMat) infoMat.innerText = current.material;

    const infoStyle = document.getElementById('info-style');
    if(infoStyle) infoStyle.innerText = current.style;

    // E. Cập nhật Input ẩn cho Form Cart
    const inputId = document.getElementById('selected-variant-id');
    if(inputId) inputId.value = current.id;
  }
}

// --- 6. XỬ LÝ SỐ LƯỢNG ---
const qtyInput = document.getElementById('quantity');
const formQty = document.getElementById('form-quantity');
const btnInc = document.getElementById('qty-increase');
const btnDec = document.getElementById('qty-decrease');

if(btnInc && qtyInput) {
  btnInc.onclick = () => {
    qtyInput.value = parseInt(qtyInput.value) + 1;
    if(formQty) formQty.value = qtyInput.value;
  };
}

if(btnDec && qtyInput) {
  btnDec.onclick = () => {
    if(qtyInput.value > 1) {
      qtyInput.value = parseInt(qtyInput.value) - 1;
      if(formQty) formQty.value = qtyInput.value;
    }
  };
}

// --- 7. SUBMIT GIỎ HÀNG ---
function submitCart(type) {
  const form = document.getElementById('cartForm');
  if (!form) return;

  // Cập nhật số lượng lần cuối trước khi gửi
  if(qtyInput && formQty) formQty.value = qtyInput.value;

  if(type === 'buy') {
    form.action = form.action.replace('/cart/add', '/checkout');
  }
  form.submit();
}

// --- 8. ĐỔI ẢNH THUMBNAIL ---
function changeImage(src, el) {
  const mainImg = document.getElementById('mainImage');
  if(mainImg) mainImg.src = src;

  // Xóa active cũ
  document.querySelectorAll('.thumbs img').forEach(img => img.classList.remove('active'));
  // Thêm active mới (nếu bạn có CSS cho class này)
  if(el) el.classList.add('active');
}

// --- 9. SLIDER ---
function scrollSlider(direction) {
  const slider = document.getElementById('productSlider');
  if(slider) {
    const scrollAmount = 300;
    slider.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
  }
}

// --- 10. BACK TO TOP ---
const backToTopBtn = document.getElementById("backToTop");
if(backToTopBtn) {
  window.onscroll = function() {
    if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
      backToTopBtn.classList.add("show");
    } else {
      backToTopBtn.classList.remove("show");
    }
  };
  backToTopBtn.onclick = function() {
    window.scrollTo({top: 0, behavior: 'smooth'});
  };
}
function scrollSlider(direction) {
  const slider = document.getElementById('productSlider');

  // Lấy chính xác chiều rộng hiển thị của khung slider
  const scrollAmount = slider.clientWidth;

  // Cộng thêm gap (24px) để khi trượt nó không bị lẹm mất mép
  // Tuy nhiên slider.clientWidth thường đã bao gồm không gian nhìn thấy
  // Thử trượt đúng bằng slider.clientWidth trước

  slider.scrollBy({
    left: direction * scrollAmount,
    behavior: 'smooth'
  });
}