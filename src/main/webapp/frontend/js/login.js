console.clear();

document.addEventListener("DOMContentLoaded", () => {
  const signupContainer = document.querySelector(".signup");
  const signinContainer = document.querySelector(".signin");

  // Nút "Đăng nhập"
  const goToSigninBtn = document.getElementById("signin");
  // Nút "Đăng ký"
  const goToSignupBtn = document.getElementById("signup-btn");

  // Chuyển Signup sang Signin
  goToSigninBtn.addEventListener("click", () => {
    signupContainer.classList.add("slide-up");
    signinContainer.classList.remove("slide-up");
  });

  // Chuyển Signin sang Signup
  goToSignupBtn.addEventListener("click", () => {
    signinContainer.classList.add("slide-up");
    signupContainer.classList.remove("slide-up");
  });

});