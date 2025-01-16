window.addEventListener("DOMContentLoaded", function() {
    /* 메인 베너 S */
    new Swiper(".main-banner .banners", {
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
      },
      loop: true,
      autoplay: {
        delay: 3000,
      },
      speed: 1000,
    });
    /* 메인 베너 E */
});