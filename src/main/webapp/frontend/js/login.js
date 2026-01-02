console.clear();

document.addEventListener("DOMContentLoaded", () => {
  const signupContainer = document.querySelector(".signup");
  const signinContainer = document.querySelector(".signin");

  // Nút "Đăng nhập" (ở form Signup) để trượt xuống
  const goToSigninBtn = document.getElementById("signin");
  // Nút "Đăng ký ngay" (ở form Signin) để trượt lên
  const goToSignupBtn = document.getElementById("signup-btn");

  // 1. Chuyển từ Signup sang Signin
  goToSigninBtn.addEventListener("click", () => {
    signupContainer.classList.add("slide-up");
    signinContainer.classList.remove("slide-up");
  });

  // 2. Chuyển từ Signin sang Signup
  goToSignupBtn.addEventListener("click", () => {
    signinContainer.classList.add("slide-up");
    signupContainer.classList.remove("slide-up");
  });

});