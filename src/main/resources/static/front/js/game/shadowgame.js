let count = 0;

window.addEventListener("DOMContentLoaded", function () {
  // 체크박스 전체 토글 기능 S
  const checkView = document.querySelector(".game-right .pokemonCount");

  const checked = document.getElementsByClassName("radio-game");
  const content = document.getElementById("description-content");
  const firstChecked = document.getElementById("requiredTerms0");
  firstChecked.checked = true;
  content.innerText = "그림자이미지, 타입, 설명, 도감번호가 나옵니다!";
  for (const element of checked) {
    element.addEventListener("click", function () {
      if (element.checked) {
        if (element.id === "requiredTerms0") {
          // 3세대
          count = 386;
          content.innerText = "그림자이미지, 타입, 설명, 도감번호가 나옵니다!";
        }
        if (element.id === "requiredTerms1") {
          // 5세대
          count = 649;
          content.innerText = "그림자이미지, 타입, 설명이 나옵니다!";
        }
        if (element.id === "requiredTerms2") {
          // 8세대
          count = 898;
          content.innerText = "그림자이미지, 타입이 나옵니다!";
        }
      }
      checkView.innerHTML = String(count + "마리");
    });
  }
  // 체크박스 전체 토글 기능 E
});
