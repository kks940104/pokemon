var commonLib = commonLib ?? {};
/**
 * 메타 태그 정보 조회
 * 사이트 정보가 담겨있는 태그
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
 * Ajax 요청 처리
 * @params url : 요청 주소, http[s] : 외부 URL - 컨택스트 경로는 추가 X
 * @params callback : 응답 완료 후 후속 처리 콜백 함수
 * @params method : 요청 방식 - GET, POST, DELETE, PATCH ...
 * @params data : 요청 데이터(POST, PATCH, PUT... 바디가 있을 경우만 가능)
 * @params headers : 추가 요청 헤더
 * @params isText : text인지 JSONData인지 확인.
 */
commonLib.ajaxLoad = function (url, callback, method = "GET", data, headers, isText = false) {
  if (!url) return;

  const { getMeta } = commonLib;
  const csrfHeader = getMeta("_csrf_header");
  const csrfToken = getMeta("_csrf");
  url = /^http[s]?:/.test(url) ? url : commonLib.url(url);

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
        if (res.status !== 204) return isText ? res.text() : res.json();
        else {
          resolve();
        }
      })
      .then((json) => {
        /* Text 형태일 땐 resolve로 바로 넣어주고 return */
        if (isText) {
            resolve(json);
            return;
        }

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
/**
* 레이어 팝업
*
*/
commonLib.popup = function(url, width = 350, height = 350, isAjax = false) {


/*
    let layerDim = document.getElementsByClassName("layer-dim");
    layerDim.forEach(el => el.parentElement.removeChild(el));

    let layerPopup = document.getElementsByClassName("layer-popup");
    layerPopup.forEach(el => el.parentElement.removeChild(el));
*/


    /* 레이어 팝업 요소 동적 추가 S */

    const layerEls = document.querySelectorAll(".layer-dim .layer-popup");
    layerEls.forEach(el => el.parentElement.removeChild(el));

    const layerDim = document.createElement("div");
    layerDim.className = "layer-dim";

    const layerPopup = document.createElement("div");
    layerPopup.className = "layer-popup";

    /* 레이어 팝업 가운데 배치 S */

    const xpos = (innerWidth - width) / 2;
    const ypos = (innerHeight - height) / 2;
    layerPopup.style.left = xpos + "px";
    layerPopup.style.top = ypos + "px";
    layerPopup.style.width = width + "px";
    layerPopup.style.height = height + "px";

    /* 레이어 팝업 가운데 배치 E */

    /* 레이어 팝업 컨텐츠 영역 추가 */
    const content = document.createElement("div");
    content.className = "layer-content";
    layerPopup.append(content);

    /* 레이어 팝업 닫기 버튼 추가 S */

    const button = document.createElement("button");
    const icon = document.createElement("i");
    button.className = "layer-close";
    button.type= "button"; /* 기본값은 submit로 변경해줘야함. */
    icon.className = "xi-close";
    button.append(icon);
    layerPopup.prepend(button);

    button.addEventListener("click", commonLib.popupClose);

    /* 레이어 팝업 닫기 버튼 추가 E */

    document.body.append(layerPopup);
    document.body.append(layerDim);



    /* 레이저 팝업 요소 동적 추가 E */

    /* 팝업 컨텐츠 로드 S */

    if (isAjax) { // 컨텐츠를 ajax로 로드
        const { ajaxLoad } = commonLib;
        ajaxLoad(url, null, 'GET', null, null, true)
            .then((text) => content.innerHTML = text);
    } else { // iframe으로 로드
        const iframe = document.createElement("iframe");
        iframe.width = width - 80;
        iframe.height = height - 80;
        iframe.frameBorder = 0;
        iframe.src = commonLib.url(url);
        content.append(iframe);
    }

    /* 팝업 컨텐츠 로드 E */
}
/*
* 레이어팝업 제거
*
*/
commonLib.popupClose = function() {
    const layerEls = document.querySelectorAll(".layer-dim, .layer-popup");
    layerEls.forEach(el => el.parentElement.removeChild(el));
};

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

  /* 팝업 버튼 클릭 처리 S */

  const showPopups = document.getElementsByClassName("show-popup");
  for(const el of showPopups) {
    el.addEventListener("click", function() {
       const { url, width, height } = this.dataset;
       commonLib.popup(url, width, height);
    });
  }

  /* 팝업 버튼 클릭 처리 E */
});

