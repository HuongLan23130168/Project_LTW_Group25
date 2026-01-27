<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Mật khẩu mới</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/contentForm.css"/>
</head>

<body>
<style>
    @import url("https://fonts.googleapis.com/css?family=Fira+Sans");

    html,
    body {
        height: 100%;
        margin: 0;
        background-color: #E1E8EE;
        display: flex;
        align-items: center;
        justify-content: center;
        font-family: "Fira Sans", Helvetica, Arial, sans-serif;
        font-size: 14px;
    }

    .form-structor {
        background-color: #222;
        border-radius: 15px;
        height: 600px;
        width: 400px;
        position: relative;
        overflow: hidden;
    }

    .form-structor::after {
        content: "";
        opacity: 0.8;
        position: absolute;
        inset: 0;
        background: url("https://i.postimg.cc/ncBkxWWJ/bgr-login.jpg") no-repeat left bottom / 500px;
    }

    .switch-text {
        text-align: center;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
    }

    .switch-text .switch-btn {
        margin-top: 10px;
        color: #74512D;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.3s ease;
        display: inline-block;
    }

    .switch-text .switch-btn:hover {
        color: #A79277;
        text-decoration: underline;
    }
</style>

<div class="form-structor">
    <div class="content">
        <h2 class="form-title">Đặt lại mật khẩu</h2>
        <form action="${pageContext.request.contextPath}/reset-password" method="post">
            <div class="form-holder">
                <input type="password" name="newPassword" class="input" placeholder="Mật khẩu mới" required/>
                <input type="password" name="confirmPassword" class="input" placeholder="Xác nhận mật khẩu" required/>
            </div>
            <button type="submit" class="submit-btn" style="border:none; width:100%; cursor:pointer;">
                Cập nhật mật khẩu
            </button>
            <p class="switch-text">
                <a href="${pageContext.request.contextPath}/frontend/login.jsp" class="switch-btn"
                   style="text-decoration: none;">
                    &larr; Quay lại đăng nhập
                </a>
            </p>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    // Thông báo lỗi từ Servlet (ví dụ: mật khẩu không khớp)
    <c:if test="${not empty errorMessage}">
    Swal.fire({
        icon: 'error',
        title: 'Lỗi',
        text: '${errorMessage}',
        confirmButtonColor: '#74512D'
    });
    </c:if>

    // Kiểm tra Regex mật khẩu mạnh trước khi submit
    document.querySelector("form").addEventListener("submit", function (e) {
        const pass = this.querySelector('input[name="newPassword"]').value;
        const confirm = this.querySelector('input[name="confirmPassword"]').value;
        const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

        if (!regex.test(pass)) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: 'Mật khẩu yếu',
                html: 'Mật khẩu phải có ít nhất 8 ký tự, 1 chữ hoa, 1 số và 1 ký tự đặc biệt!',
                confirmButtonColor: '#74512D'
            });
        } else if (pass !== confirm) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Không khớp',
                text: 'Xác nhận mật khẩu phải giống mật khẩu mới!',
                confirmButtonColor: '#74512D'
            });
        }
    });
</script>
</body>

</html>