let count = 0;

window.addEventListener("DOMContentLoaded", function () {
  // 체크박스 전체 토글 기능 S
  const checkAlls = document.getElementsByClassName("pk-check-all");
  const checkView = document.querySelector(".game-right .pokemonCount");
  for (const el of checkAlls) {
    el.addEventListener("click", function () {
      const { targetClass } = this.dataset;
      if (!targetClass) {
        // 토클할 체크박스의 클래스가 설정되지 않은 경우는 진행 X
        return;
      }

      const chks = document.getElementsByClassName(targetClass);
      count = 0;
      for (const chk of chks) {
        const pokemonCheck = document.getElementById(chk.id);
        chk.checked = this.checked;
        if (chk.checked) {
          if (pokemonCheck.id === "requiredTerms0") {
            // 1세대
            count += 151;
          }
          if (pokemonCheck.id === "requiredTerms1") {
            // 2세대
            count += 100;
          }
          if (pokemonCheck.id === "requiredTerms2") {
            // 3세대
            count += 135;
          }
          if (pokemonCheck.id === "requiredTerms3") {
            // 4세대
            count += 107;
          }
          if (pokemonCheck.id === "requiredTerms4") {
            // 5세대
            count += 156;
          }
          if (pokemonCheck.id === "requiredTerms5") {
            // 6세대
            count += 72;
          }
          if (pokemonCheck.id === "requiredTerms6") {
            // 7세대
            count += 88;
          }
          if (pokemonCheck.id === "requiredTerms7") {
            // 8세대
            count += 89;
          }
        }
      }
      checkView.innerHTML = String(count + "마리");
    });
  }

  const checked = document.getElementsByClassName("pk-terms");
  for (const element of checked) {
    element.addEventListener("click", function () {
      if (element.checked) {
        if (element.id === "requiredTerms0") {
          // 1세대

          count  += 151;
        }
        if (element.id === "requiredTerms1") {
          // 2세대
          count += 100;
        }
        if (element.id === "requiredTerms2") {
          // 3세대
          count += 135;
        }
        if (element.id === "requiredTerms3") {
          // 4세대
          count += 107;
        }
        if (element.id === "requiredTerms4") {
          // 5세대
          count += 156;
        }
        if (element.id === "requiredTerms5") {
          // 6세대
          count += 72;
        }
        if (element.id === "requiredTerms6") {
          // 7세대
          count += 88;
        }
        if (element.id === "requiredTerms7") {
          // 8세대
          count += 89;
        }
      } else {
        if (element.id === "requiredTerms0") {
          // 1세대
          count -= 151;
        }
        if (element.id === "requiredTerms1") {
          // 2세대
          count -= 100;
        }
        if (element.id === "requiredTerms2") {
          // 3세대
          count -= 135;
        }
        if (element.id === "requiredTerms3") {
          // 4세대
          count -= 107;
        }
        if (element.id === "requiredTerms4") {
          // 5세대
          count -= 156;
        }
        if (element.id === "requiredTerms5") {
          // 6세대
          count -= 72;
        }
        if (element.id === "requiredTerms6") {
          // 7세대
          count -= 88;
        }
        if (element.id === "requiredTerms7") {
          // 8세대
          count -= 89;
        }
      }
      checkView.innerHTML = String(count + "마리");
    });
  }
  // 체크박스 전체 토글 기능 E
});
