function toggleEdit() {
    const editForm = document.getElementById('editForm');
    if (editForm.classList.contains('hidden')) {
        editForm.classList.remove('hidden');
        setTimeout(() => editForm.classList.add('show'), 10);
    } else {
        editForm.classList.remove('show');
        setTimeout(() => editForm.classList.add('hidden'), 300);
    }
}

// Giữ nguyên đoạn thông báo thành công

const urlParams = new URLSearchParams(window.location.search);
const msg = urlParams.get('msg');
const detail = urlParams.get('detail');

if (msg === 'success') {
    alert('✅ Cập nhật thông tin thành công!');
} else if (msg === 'error') {
    let errorText = '❌ Có lỗi xảy ra khi lưu dữ liệu.';
    if (detail === 'data_too_long') {
        errorText += '\nNguyên nhân: Số điện thoại hoặc dữ liệu quá dài so với quy định.';
    } else if (detail === 'server_error') {
        errorText += '\nNguyên nhân: Lỗi hệ thống (Server Error).';
    }
    alert(errorText);

}