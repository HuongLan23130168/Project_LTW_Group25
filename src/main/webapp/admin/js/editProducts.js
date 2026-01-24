
    // 1. Thêm biến thể mới (QUAN TRỌNG: Cần reset variant_ids về rỗng)
    function addVariantRow() {
    const container = document.getElementById('variant-list');
    const rows = container.getElementsByClassName('variant-row');
    const sampleRow = rows[0]; // Clone dòng mẫu

    const newRow = sampleRow.cloneNode(true);
    const inputs = newRow.querySelectorAll('input');

    inputs.forEach(input => {
    input.value = ''; // Xóa dữ liệu cũ

    // QUAN TRỌNG: Reset ID về rỗng để Backend biết đây là INSERT MỚI
    if (input.name === 'variant_ids') {
    input.value = '';
}
    if (input.name === 'stocks') {
    input.value = '10'; // Mặc định kho
}
});

    // Đảm bảo nút xóa hoạt động
    const btnRemove = newRow.querySelector('.btn-remove-row');
    if(btnRemove) {
    btnRemove.style.display = 'flex'; // Hiện lại nếu bị ẩn
}

    container.appendChild(newRow);
}

    // 2. Xóa biến thể
    function removeVariantRow(btn) {
    const container = document.getElementById('variant-list');
    const rows = container.getElementsByClassName('variant-row');

    // Logic: Nếu dòng này đã có ID (đã tồn tại trong DB),
    // tốt nhất là ẩn nó đi và thêm 1 input hidden delete_ids để backend xóa.
    // Tuy nhiên, để đơn giản UI: nếu chỉ còn 1 dòng thì không cho xóa.
    if (rows.length > 1) {
    btn.closest('.variant-row').remove();

    // (Optional) Nếu muốn xóa cứng trong DB, bạn cần xử lý thêm logic:
    // Lấy variant_ids của dòng bị xóa, append vào một hidden input name="deleted_variant_ids"
} else {
    alert("Sản phẩm phải có ít nhất 1 phiên bản!");
}
}

    // 3. Thêm ảnh gallery
    function addGalleryInput() {
    const container = document.getElementById('gallery-container');
    const div = document.createElement('div');
    div.style.display = 'flex';
    div.style.gap = '10px';
    div.style.marginBottom = '10px';
    div.style.alignItems = 'center';

    div.innerHTML = `
            <input type="text" name="other_images" class="form-control" placeholder="URL ảnh phụ...">
            <button type="button" class="btn-remove-url" onclick="this.parentElement.remove()">
                <i class="fa-solid fa-xmark"></i>
            </button>
        `;
    container.appendChild(div);
}
