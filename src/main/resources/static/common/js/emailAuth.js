var commonLib = commonLib : {};

/**
* 이메일 인증 코드 관련
*
*/
commonLib.emailAuth = {
    timer: {
        seconds: 180, // 3분
        intervalId: null;
        reset(callback) { // 타이머 초기화
            this.stop();
            this.seconds = 180;
            if (typeof callback === 'function') {
                callback(this.seconds);
            }
        },
        stop(callback) { // 타이머 중지
            if (this.intervalId) {
                clearInterval(this.intervalId);
            }

            if (typeof callback === 'function') {
                callback(this.seconds);
            }
        },
        start(callback) { // 타이머 시작
            if (this.seconds < 1) return;
            this.stop();
            this.intervalId = setInterval(function() {
                const seconds = --commonLib.emailAuth.timer.seconds;
                if (typeof callback === 'function') {
                    callback(seconds);
                }
            }, 1000);

        },
    },
    /**
    * 인증 코드 전송
    *
    */
    sendCode(email) {
        const { ajaxLoad } = commonLib;
        ajaxLoad(`/api/email/auth/${email}`);

    },
    /**
    * 인증 코드 검증
    *
    */
    verify(authCode) {
        const { ajaxLoad } = commonLib;
        ajaxLoad(`/api/email/verify?authCode=${authCode}`);
    },
};

/**
* ajax 실패 시 처리
*/
function callbackAjaxFailure(err) {

}











