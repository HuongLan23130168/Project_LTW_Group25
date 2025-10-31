document.addEventListener("DOMContentLoaded", () => {
    const menuItems = document.querySelectorAll(".menu li");
    const pages = document.querySelectorAll(".page");

    // ========== CHUYỂN TAB ==========
    menuItems.forEach((item, index) => {
        item.addEventListener("click", () => {
            menuItems.forEach(i => i.classList.remove("active"));
            item.classList.add("active");

            pages.forEach(p => p.classList.remove("active"));

            if (item.innerText.trim() === "Đổi mật khẩu") {
                loadPasswordPage();
            } else {
                pages[index].classList.add("active");
            }
        });
    });

    // ========== SỬA HỒ SƠ ==========
    const editBtn = document.getElementById("editProfile");
    if (editBtn) {
        editBtn.addEventListener("click", function() {
            let btnText = this.textContent.trim();

            if (btnText === "Sửa" || btnText === "Sửa") {
                this.textContent = "Lưu";

                ["email", "fullName", "phone"].forEach(id => {
                    const span = document.getElementById(id);
                    const text = span.textContent.trim();
                    span.innerHTML = `<input class="editing" value="${text}">`;
                });

            } else {
                this.textContent = "Sửa";

                ["email", "fullName", "phone"].forEach(id => {
                    const span = document.getElementById(id);
                    const input = span.querySelector("input");
                    span.textContent = input.value.trim();
                });
            }
        });
    }

    // ========== SỬA ĐỊA CHỈ ==========
    document.addEventListener("click", function(e) {
        if (e.target.classList.contains("edit-address-btn")) {
            const btn = e.target;
            const block = btn.closest(".address-row").querySelector(".address");
            let text = block.textContent.replace("", "").trim();

            if (btn.textContent === "Sửa") {
                block.innerHTML = `<input class="editing" value="${text}">`;
                btn.textContent = "Lưu";
            } else {
                const input = block.querySelector("input");
                block.innerHTML = `${input.value.trim()} <span class="default">(Mặc định)</span>`;
                btn.textContent = "Sửa";
            }
        }
    });

    // ========== THÊM ĐỊA CHỈ ==========
    document.querySelector(".add-btn").addEventListener("click", () => {
        const addressBox = document.querySelector(".address-box");

        const newAddress = document.createElement("div");
        newAddress.className = "address-row";
        newAddress.innerHTML = `
            <div class="address">
                <input class="editing" placeholder="Nhập địa chỉ mới...">
            </div>
            <button class="edit-btn edit-address-btn">Lưu</button>
        `;

        addressBox.appendChild(newAddress);
    });

});

function loadPasswordPage() {
    fetch("changePass.html")
        .then(res => res.text())
        .then(html => {
            const passPage = document.getElementById("page-password");
            passPage.innerHTML = html;
            passPage.classList.add("active");

            const script = document.createElement("script");
            script.src = "../js/changePass.js";
            document.body.appendChild(script);
        });
}

