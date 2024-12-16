package org.koreait.global.advices;


import lombok.RequiredArgsConstructor;
import org.koreait.global.exceptions.CommonException;
import org.koreait.global.libs.Utils;
import org.koreait.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class CommonRestControllerAdvice {

    private final Utils utils;

    /**
     * ResponseEntity 응답 코드, 헤더, 바디를 설정.
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JSONData> errorHandler(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본 에러 코드 500

        Object message = e.getMessage();
        if (e instanceof CommonException commonException) {
            status = commonException.getStatus();

            // region 에러메시지 JSON 형태로 출력하는 방법.

            Map<String, List<String>> errorMessage = commonException.getErrorMessages();
            if (errorMessage != null) {
                message = errorMessage;
            } else {
                message = commonException.isErrorCode() ? utils.getMessage((String)message) : message;
            }

            // endregion
        }

        /**
         * JSONData로 하는 이유는 예측 가능하도록 형식 변경.
         */

        JSONData data = new JSONData();
        data.setSuccess(false);
        data.setStatus(status);
        data.setMessage(message);

        e.printStackTrace();
        // ResponseEntity는 응답코드와 바디 데이터를 상세하게 설정 가능.
        return ResponseEntity.status(status).body(data);

    }
}
