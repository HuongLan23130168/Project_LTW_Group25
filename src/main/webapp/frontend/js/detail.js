document.addEventListener('DOMContentLoaded', () => {
  if (typeof variantsData !== 'undefined' && variantsData.length > 0) {
    renderColors();
    renderSizes();
    updateProductState(); // Gọi lần đầu để đảm bảo giá được hiển thị đúng
  } else {
    console.warn("Không có dữ liệu biến thể (variantsData)");
  }

  // Các event listener khác
  setupQuantityButtons();
  setupThumbnailClick();
  setupBackToTop();
});

function renderColors() {
  const uniqueColors = [...new Set(variantsData.map(v => v.color))];
  const container = document.getElementById('color-options');
  if (!container) return;

  container.innerHTML = uniqueColors.map(color => {
    const isActive = color === selectedColor ? 'active' : '';
    return `<div class="option ${isActive}" onclick="selectColor('${color}')">${color}</div>`;
  }).join('');
}

function renderSizes() {
  const availableSizes = variantsData.filter(v => v.color === selectedColor).map(v => v.size);
  const allSizes = [...new Set(variantsData.map(v => v.size))];
  const container = document.getElementById('size-options');
  if (!container) return;

  container.innerHTML = allSizes.map(size => {
    const isAvailable = availableSizes.includes(size);
    const isActive = size === selectedSize ? 'active' : '';
    if (isAvailable) {
      return `<div class="option ${isActive}" onclick="selectSize('${size}')">${size}</div>`;
    } else {
      return `<div class="option disabled" style="opacity: 0.4; cursor: not-allowed; border: 1px dashed #ccc;">${size}</div>`;
    }
  }).join('');
}

function selectColor(color) {
  if (selectedColor === color) return;
  selectedColor = color;
  const validSizes = variantsData.filter(v => v.color === color).map(v => v.size);
  if (!validSizes.includes(selectedSize)) {
    selectedSize = validSizes[0];
  }
  updateProductState();
}

function selectSize(size) {
  if (selectedSize === size) return;
  selectedSize = size;
  updateProductState();
}

// --- CORE LOGIC - SỬA LỖI HIỂN THỊ GIÁ ---
function updateProductState() {
  renderColors();
  renderSizes();

  const currentVariant = variantsData.find(v => v.color === selectedColor && v.size === selectedSize);

  if (currentVariant) {
    // Lấy các element
    const priceDisplay = document.getElementById('price-display');
    const priceOldDisplay = document.getElementById('price-old-display');
    const discountTag = document.getElementById('discount-tag');
    const formatter = new Intl.NumberFormat('vi-VN');

    // Lấy giá gốc của biến thể
    const originalPrice = currentVariant.price;

    // Sử dụng productDiscountPercent từ JSP
    if (productDiscountPercent > 0) {
      // Tính giá mới
      const newPrice = originalPrice * (1 - productDiscountPercent / 100);

      // Hiển thị giá mới, giá gốc và tag
      priceDisplay.innerText = formatter.format(newPrice) + '₫';
      priceOldDisplay.innerText = formatter.format(originalPrice) + '₫';
      priceOldDisplay.style.display = 'inline-block';
      discountTag.style.display = 'inline-block';
      discountTag.innerText = `-${Math.round(productDiscountPercent)}%`;
    } else {
      // Không có giảm giá, chỉ hiển thị giá gốc
      priceDisplay.innerText = formatter.format(originalPrice) + '₫';
      priceOldDisplay.style.display = 'none';
      discountTag.style.display = 'none';
    }

    // Cập nhật các thông tin khác
    document.getElementById('color-text').innerText = currentVariant.color;
    document.getElementById('size-text').innerText = currentVariant.size;
    document.getElementById('info-material').innerText = currentVariant.material;
    document.getElementById('info-style').innerText = currentVariant.style;
    document.getElementById('selected-variant-id').value = currentVariant.id;

    // Cập nhật ảnh chính nếu biến thể có ảnh riêng
    const mainImg = document.getElementById('mainImage');
    if (mainImg && currentVariant.image_url) {
        mainImg.src = currentVariant.image_url;
    }
  }
}

function setupQuantityButtons() {
  const qtyInput = document.getElementById('quantity');
  const formQty = document.getElementById('form-quantity');
  const btnInc = document.getElementById('qty-increase');
  const btnDec = document.getElementById('qty-decrease');

  if (btnInc && qtyInput) {
    btnInc.onclick = () => {
      qtyInput.value = parseInt(qtyInput.value) + 1;
      if (formQty) formQty.value = qtyInput.value;
    };
  }
  if (btnDec && qtyInput) {
    btnDec.onclick = () => {
      if (qtyInput.value > 1) {
        qtyInput.value = parseInt(qtyInput.value) - 1;
        if (formQty) formQty.value = qtyInput.value;
      }
    };
  }
}

function submitCart(type) {
  const form = document.getElementById('cartForm');
  if (!form) return;
  document.getElementById('form-quantity').value = document.getElementById('quantity').value;
  document.getElementById('redirectAction').value = type;
  form.submit();
}

function setupThumbnailClick() {
  const thumbImages = document.querySelectorAll('.thumbs img');
  const mainImage = document.getElementById('mainImage');
  thumbImages.forEach(img => {
    img.addEventListener('click', function() {
      thumbImages.forEach(thumb => thumb.classList.remove('active'));
      this.classList.add('active');
      if (mainImage) mainImage.src = this.src;
    });
  });
}

function scrollSlider(direction) {
  const slider = document.getElementById('productSlider');
  if (slider) {
    const scrollAmount = slider.clientWidth;
    slider.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
  }
}

function setupBackToTop() {
  const backToTopBtn = document.getElementById("backToTop");
  if (backToTopBtn) {
    window.onscroll = () => {
      if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
        backToTopBtn.style.opacity = "1";
        backToTopBtn.style.visibility = "visible";
      } else {
        backToTopBtn.style.opacity = "0";
        backToTopBtn.style.visibility = "hidden";
      }
    };
    backToTopBtn.onclick = () => window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}
