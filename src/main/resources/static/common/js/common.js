var commonLib = commonLib ?? {};
/**
 * 메타 태그 정보 조회
 * mode - rootUrl : <meta name="rootUrl"... />
 */
commonLib.getMeta = function (mode) {
  if (!mode) return;
  const el = document.querySelector(`meta[name='${mode}']`);
  return el?.content; // 옵셔널 체이닝 문법. 최근에 추가된 문법.
  // 만약 el이 없으면 undefined 가 나옴.
};
/**
 * 자바스크립트 만든 주소에 컨텍스트 경로 추가.
 *
 */

commonLib.url = function (url) {
  return `${commonLib.getMeta("rootUrl").replace("/", "")}${url}`;
};
/**
 * Ajax 요청 처리0
 * @params url : 요청 주소, http[s] : 외부 URL - 컨택스트 경로는 추가 X
 * @params callback : 응답 완료 후 후속 처리 콜백 함수
 * @params method : 요청 방식 - GET, POST, DELETE, PATCH ...
 * @params data : 요청 데이터(POST, PATCH, PUT... 바디가 있을 경우만 가능)
 * @params headers : 추가 요청 헤더
 */
commonLib.ajaxLoad = function (url, callback, method = "GET", data, headers) {
  if (!url) return;

  const { getMeta } = commonLib;
  const csrfHeader = getMeta("_csrf_header");
  const csrfToken = getMeta("_csrf");
  url = /^http[s]?:/.test(url)
    ? url
    : getMeta("rootUrl") + url.replace("/", "");

  headers = headers ?? {};
  headers[csrfHeader] = csrfToken;
  method = method.toUpperCase();

  const options = {
    method,
    headers,
  };

  if (data && ["POST", "PUT", "PATCH"].includes(method)) {
    // body 쪽 데이터 추가 가능.
    options.body = data instanceof FormData ? data : JSON.stringify(data);
  }

  return new Promise((resolve, reject) => {
    fetch(url, options)
      .then((res) => {
        if (res.status !== 204) return res.json();
        else {
          resolve();
        }
      })
      .then((json) => {
        if (json?.success) {
          // 응답 성공했을 때(처리 성공)
          if (typeof callback === "function") {
            // 콜백 함수가 정의된 경우
            callback(json.data);
          }

          resolve(json);

          return;
        }
        reject(json); // 처리 실패..
      })
      .catch((err) => {
        console.error(err);

        reject(err); // 응답 실패..
      });
  }); // 반환값을 Promise로....
};
// isAjax가 false면 item, isAjax가 true면 Ajax로
commonLib.popup = function(url, width = 350, height = 350, isAjax = false) {

}

window.addEventListener("DOMContentLoaded", function () {
  // 체크박스 전체 토글 기능 S
  const checkAlls = document.getElementsByClassName("check-all");
  for (const el of checkAlls) {
    el.addEventListener("click", function () {
      const { targetClass } = this.dataset;
      if (!targetClass) {
        // 토클할 체크박스의 클래스가 설정되지 않은 경우는 진행 X
        return;
      }

      const chks = document.getElementsByClassName(targetClass);
      for (const chk of chks) {
        chk.checked = this.checked;
      }
    });
  }
  const checkBoxes = document.getElementsByClassName("chk");
  const parent = document.getElementById("check-all");
  for (const checkBox of checkBoxes) {
    checkBox.addEventListener("click", function () {
      if (!checkBox.checked) {
        parent.checked = false;
        return;
      }
      for (const check of checkBoxes) {
        if (check.checked) {
          parent.checked = true;
        }
      }
    });
  }
  // 체크박스 전체 토글 기능 E
});

