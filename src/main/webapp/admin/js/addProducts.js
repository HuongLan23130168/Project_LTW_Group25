/* ====================================================== */
/* 1. QUẢN LÝ BIẾN THỂ (VARIANTS)                         */
/* ====================================================== */

/**
 * Thêm một dòng biến thể mới
 * Logic: Clone dòng cuối cùng, reset giá trị input về rỗng
 */
function addVariantRow() {
    const container = document.getElementById('variant-list');
    const rows = container.getElementsByClassName('variant-row');

    // Lấy dòng mẫu (dòng đầu tiên hoặc cuối cùng đều được)
    const sampleRow = rows[0];

    // Clone node (true để copy cả con bên trong)
    const newRow = sampleRow.cloneNode(true);

    // Reset dữ liệu trong dòng mới
    const inputs = newRow.querySelectorAll('input');
    inputs.forEach(input => {
        input.value = ''; // Xóa nội dung cũ

        // Mặc định tồn kho là 10 (nếu muốn)
        if (input.name === 'stocks') {
            input.value = '10';
        }
    });

    // Thêm dòng mới vào container
    container.appendChild(newRow);
}

/**
 * Xóa một dòng biến thể
 * Logic: Kiểm tra nếu còn > 1 dòng thì cho xóa, ngược lại báo lỗi
 */
function removeVariantRow(btn) {
    const container = document.getElementById('variant-list');
    const rows = container.getElementsByClassName('variant-row');

    if (rows.length > 1) {
        // Tìm element cha có class .variant-row và xóa nó
        btn.closest('.variant-row').remove();
    } else {
        alert("Sản phẩm phải có ít nhất 1 phiên bản (Màu sắc/Size)!");
    }
}

/* ====================================================== */
/* 2. QUẢN LÝ HÌNH ẢNH (GALLERY)                          */
/* ====================================================== */

/**
 * Preview ảnh chính (Thumbnail) ngay khi paste link
 * (Hàm này thường được gọi trực tiếp từ oninput trong HTML,
 * nhưng viết ở đây để fallback hoặc mở rộng xử lý lỗi)
 */
function previewMainImage(input) {
    const imgPreview = document.getElementById('previewMain');
    const defaultImg = '/admin/img/no-image.png'; // Đường dẫn ảnh mặc định

    if (input.value && input.value.trim() !== "") {
        imgPreview.src = input.value;
    } else {
        imgPreview.src = defaultImg;
    }

    // Xử lý khi link ảnh bị lỗi (404)
    imgPreview.onerror = function() {
        this.src = defaultImg;
    };
}

/**
 * Thêm ô nhập link ảnh phụ (Gallery)
 */
function addGalleryInput() {
    const container = document.getElementById('gallery-container');

    // Tạo thẻ div bao bọc
    const div = document.createElement('div');

    // Style trực tiếp bằng JS để khớp với CSS (hoặc dùng class nếu có Bootstrap)
    div.style.display = 'flex';
    div.style.gap = '10px';
    div.style.marginBottom = '10px';
    div.style.alignItems = 'center';

    // Nội dung HTML của dòng input mới
    div.innerHTML = `
        <input type="text" name="other_images" class="form-control" 
               placeholder="Dán URL ảnh phụ vào đây..." 
               style="flex:1; padding: 10px; border: 1px solid #ccc; border-radius: 5px;">
               
        <button type="button" class="btn-remove-url" onclick="this.parentElement.remove()">
            <i class="fa-solid fa-xmark"></i>
        </button>
    `;

    container.appendChild(div);
}

/* ====================================================== */
/* 3. VALIDATION CƠ BẢN (Tùy chọn)                        */
/* ====================================================== */

document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById('addProductForm');

    if(form) {
        form.addEventListener('submit', function(e) {
            // Kiểm tra xem có dòng biến thể nào chưa nhập giá không?
            const prices = document.getElementsByName('prices');
            let valid = true;

            for(let i = 0; i < prices.length; i++) {
                if(!prices[i].value || parseFloat(prices[i].value) < 0) {
                    valid = false;
                    prices[i].style.borderColor = "red";
                } else {
                    prices[i].style.borderColor = "#ccc";
                }
            }

            if(!valid) {
                e.preventDefault();
                alert("Vui lòng nhập giá bán hợp lệ cho tất cả các biến thể!");
            }
        });
    }
});