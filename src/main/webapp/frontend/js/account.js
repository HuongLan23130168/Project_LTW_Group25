// document.addEventListener("DOMContentLoaded", () => {
//   const pages = document.querySelectorAll(".page");
//   const menuItems = document.querySelectorAll(".menu-account li[data-target]");
//
//   function showPage(selector) {
//     pages.forEach((p) => p.classList.remove("active"));
//     const page = document.querySelector(selector);
//     if (page) page.classList.add("active");
//   }
//
//   // Di chuột vào submenu cha -> mở submenu
//   document.querySelectorAll(".menu-account .has-submenu").forEach((parent) => {
//     parent.addEventListener("mouseenter", () => {
//       parent.classList.add("open");
//     });
//     parent.addEventListener("mouseleave", () => {
//       parent.classList.remove("open");
//     });
//   });
//
//   // Hover vào item -> active + show page
//   menuItems.forEach((item) => {
//     item.addEventListener("mouseenter", () => {
//       menuItems.forEach((i) => i.classList.remove("active"));
//       item.classList.add("active");
//
//       const target = item.getAttribute("data-target");
//       if (target) showPage(target);
//     });
//   });
//
//   // Order-tab giữ click
//   document.querySelectorAll(".order-tab").forEach((tab) => {
//     tab.addEventListener("click", () => {
//       document
//         .querySelectorAll(".order-tab")
//         .forEach((t) => t.classList.remove("active"));
//       tab.classList.add("active");
//       const status = tab.getAttribute("data-tab");
//       document.querySelectorAll(".order-card").forEach((order) => {
//         order.style.display =
//           status === "all" || order.dataset.status === status
//             ? "block"
//             : "none";
//       });
//     });
//   });
// });


// --- LOGIC TOGGLE EDIT PROFILE ---
function toggleProfileEdit() {
  const viewDiv = document.getElementById('profile-view');
  const editForm = document.getElementById('profile-edit');

  // Toggle class hidden
  if (editForm.classList.contains('hidden')) {
    // Hiện form sửa, ẩn view
    viewDiv.classList.add('hidden');
    editForm.classList.remove('hidden');
  } else {
    // Ẩn form sửa, hiện view
    editForm.classList.add('hidden');
    viewDiv.classList.remove('hidden');
  }
}

// --- LOGIC MODAL ADDRESS ---
function openAddressModal() {
  document.getElementById('modal-address').classList.add('show');
}

function closeAddressModal() {
  document.getElementById('modal-address').classList.remove('show');
}

// Đóng modal khi click ra ngoài vùng trắng
window.onclick = function(event) {
  const modal = document.getElementById('modal-address');
  if (event.target == modal) {
    closeAddressModal();
  }
}


// --- Logic Toggle Sửa Profile ---
function toggleProfileEdit() {
  const viewDiv = document.getElementById('profile-view');
  const editForm = document.getElementById('profile-edit');

  if (editForm.classList.contains('hidden')) {
    // Hiện form sửa
    viewDiv.classList.add('hidden');
    editForm.classList.remove('hidden');
  } else {
    // Ẩn form sửa
    editForm.classList.add('hidden');
    viewDiv.classList.remove('hidden');
  }
}

// --- Logic Modal Địa chỉ ---
function openAddressModal() {
  document.getElementById('modal-address').classList.add('show');
}

function closeAddressModal() {
  document.getElementById('modal-address').classList.remove('show');
}

// Đóng modal khi click ra vùng tối bên ngoài
window.onclick = function(event) {
  const modal = document.getElementById('modal-address');
  if (event.target == modal) {
    closeAddressModal();
  }
}
// --- Logic Modal Sửa Địa chỉ ---
function openEditModal(id, address, isDefault) {
  // 1. Điền dữ liệu cũ vào form modal
  document.getElementById('edit-id').value = id;
  document.getElementById('edit-address').value = address;

  // Checkbox: nếu isDefault == 1 thì tick, ngược lại bỏ tick
  document.getElementById('edit-isDefault').checked = (isDefault == 1);

  // 2. Hiện Modal
  document.getElementById('modal-edit-address').classList.add('show');
}

function closeEditModal() {
  document.getElementById('modal-edit-address').classList.remove('show');
}

// Xử lý đóng modal khi click ra ngoài (áp dụng cho cả 2 modal)
window.onclick = function(event) {
  const modalAdd = document.getElementById('modal-address');
  const modalEdit = document.getElementById('modal-edit-address');

  if (event.target == modalAdd) {
    closeAddressModal();
  }
  if (event.target == modalEdit) {
    closeEditModal();
  }
}
